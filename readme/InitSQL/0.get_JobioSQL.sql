select * from table(db2inst1.get_JobioSQL('ktlrep_20200526','kjb_ods_to_ods_hldtran_inc'))t

create schema DBO;  // 创建schema
drop table if exists dbo.Jobio_Version; // 版本控制表
create table dbo.Jobio_Version (useable varchar(10),version decimal(5,2));
insert into dbo.Jobio_Version values('Y','3.1'),('Y','3.2');
insert into dbo.Jobio_Version values('Y','3.3');
select * from dbo.Jobio_Version -- where version = 3.2

// 用户表
create table dbo.Jobio_User(id int, userName varchar(100), password varchar(100), optionTime timestamp, remark varchar(100), version decimal(5,2));
insert into dbo.Jobio_User(id,userName,password) values(1,'Jupiter','Jupiter');
select * from dbo.Jobio_User;

/*返回JobIOSql
 *版本号V3.1 
 *Jupiter.Lin 
 *2019-03-28
 */
--CREATE or replace FUNCTION db2inst1.dsxml_JobioSQL(v_Stype varchar(20),v_jobname  VARCHAR(108))
CREATE or replace FUNCTION db2inst1.get_JobioSQL(v_Stype varchar(20),v_jobname  VARCHAR(108))
	RETURNS TABLE(
	Stype varchar(20),
	JobType varchar(10),
	Joblocate varchar(200),
	Jobname VARCHAR(108),
	RECORD varchar(60),
	StageID varchar(50),
	StageName VARCHAR(108),
	StageType VARCHAR(28),
	InputPins varchar(200),
	OutputPins varchar(118),
	Server varchar(50),
	UserName varchar(50),
	Input_BEFORESQL varchar(32672),
	Input_SQL varchar(5000),
	Input_AFTERSQL varchar(32672),
	Output_BEFORESQL varchar(32672),
	Output_SQL varchar(32672),
	Output_AFTERSQL varchar(32672))
begin atomic 
  RETURN
/*with t as( --stage基本信息(server/sequeuejob)
select JOBNAME,RECORD,STAGEID
,max(case when RECORD||Key in('HashedFileStageDirectory','ODBCStageDSN')then trim(substr(Value,1,50)) end) as Server
,max(case when RECORD||Key in('ODBCStageUserName') then trim(substr(Value,1,50)) end) as UserName
from dsxml_StageInfo
where stype = v_Stype and jobname = (select jobname from dsxml_jobinfo where jobtype <> 3 and stype = v_Stype and jobname = v_jobname)
and RECORD in('CustomStage','HashedFileStage','ODBCStage','SeqFileStage','ContainerStage','TransformerStage')
group by JOBNAME,RECORD,STAGEID)*/
with t as( --stage基本信息(server/sequeuejob)
select a.CATEGORY,a.JOBNAME,b.RECORD,b.STAGEID
,max(decode(b.Key,'StageType',b.Value)) as StageType
,max(case when b.RECORD||b.Key in('ODBCStageDSN','CustomStageSERVER','CustomStageDBNAME','CustomStageDATABASE','HashedFileStageDirectory')then b.Value end) as Server
,max(case when b.RECORD||b.Key in('ODBCStageUserName','CustomStageUSERNAME','CustomStageUSERID') then b.Value end) as UserName
from dsxml_jobinfo a,dsxml_StageInfo b
where a.stype = b.stype and a.jobname = b.jobname 
and a.jobtype <> 3
and a.stype = v_Stype and a.jobname = v_jobname
and b.RECORD in('CustomStage','HashedFileStage','ODBCStage','SeqFileStage','UDBLoad')--,'ContainerStage','TransformerStage')
group by a.CATEGORY,a.JOBNAME,b.RECORD,b.STAGEID)
,p as( --parallel作业
select b.stype,b.CATEGORY,b.jobname,b.RECORD,b.StageID,a.StageName StageName,a.StageType,a.INPUTPINS,a.OUTPUTPINS,y.Server,y.Username,y.Password,y.ReadMode,y.WriteMode,y.GenerateSQL,y.TableScope
,y.BeforeSQL,decode(y.GENERATESQL,'0',y.sql,decode(coalesce(a.OutputPins,y.ReadMode),null,'insert into ','select * from ')||y.TableName)sql,y.AfterSQL
from(select j.Stype,j.CATEGORY,j.Jobname,x.RECORD,x.StageID,x.Key,xmlparse(document value)PXML from dsxml_StageInfo x,dsxml_jobinfo j where j.stype = x.stype and j.jobname = x.jobname and j.jobtype in(0,3) and j.stype = v_Stype and j.jobname = v_jobname and x.KEY = 'XMLProperties')b
inner join dsxml_CustomStage a on a.stype = v_Stype and b.jobname = a.jobname and b.Stageid = a.Stageid
,xmltable('$PXML/Properties'
columns
 Server varchar(50) path 'Connection/(Database|Server|DataSource|URL)[1]'
,Username varchar(50) path 'Connection/Username'
,Password varchar(50) path 'Connection/Password'
,ReadMode varchar(2) path 'Usage/ReadMode'
,WriteMode varchar(2) path 'Usage/WriteMode'
,GenerateSQL varchar(2) path 'Usage/GenerateSQL'
,TableName varchar(100) path 'Usage/TableName/text()'
,TableScope varchar(50) path 'Usage/TableName/TableScope'
,BeforeSQL varchar(32672) path 'Usage/BeforeAfter/(before|BeforeSQL)[1]/text()'
,SQL varchar(32672) path 'Usage/SQL/(SelectStatement|InsertStatement|UpdateStatement|DeleteStatement|User-definedSQL)[1]/child::text()'
,AfterSQL varchar(32672) path 'Usage/BeforeAfter/(after|AfterSQL)[1]/text()'
,ReadFromFileSelect varchar(256) path 'Usage/SQL/SelectStatement/ReadFromFileSelect'
)as y)
--INPUTPINS
,InputPins as(
select Jobname,RECORD,StageID INPUTPINS
,max(case when RECORD='CustomInput' and Key='TABLE'          then Value                           end )as "TABLE"
,max(case when RECORD='CustomInput' and Key='TABLESPACE'     then trim(substr(Value,1,20))   end )as TABLESPACE
,max(case when RECORD='CustomInput' and Key='PARTKEY'        then trim(substr(Value,1,10))   end )as PARTKEY
,max(case when RECORD='CustomInput' and Key='TARGETTABLE'    then trim(substr(Value,1,100))  end )as TARGETTABLE
,max(case when RECORD='CustomInput' and Key='DROPTABLE'      then trim(substr(Value,1,6))    end )as DROPTABLE
,max(case when RECORD='CustomInput' and Key='DROPDDL'        then trim(substr(Value,1,100))  end )as DROPDDL
,max(case when RECORD='CustomInput' and Key='GENDROPDDL'     then trim(substr(Value,1,6))    end )as GENDROPDDL
,max(case when RECORD='CustomInput' and Key='CREATETABLE'    then trim(substr(Value,1,6))    end )as CREATETABLE
,max(case when RECORD='CustomInput' and Key='CREATEDDL'      then trim(substr(Value,1,4000)) end )as CREATEDDL
,max(case when RECORD='CustomInput' and Key='GENCREATEDDL'   then trim(substr(Value,1,6))    end )as GENCREATEDDL
,max(case when RECORD='CustomInput' and Key='CONTBEFORESQL'  then trim(substr(Value,1,6))    end )as CONTBEFORESQL
,max(case when RECORD='CustomInput' and Key='BEFORESQL'      then Value                      end )as BEFORESQL
,max(case when RECORD='CustomInput' and Key='INPUTMODE'      then trim(substr(Value,1,50))   end )as INPUTMODE
,max(case when RECORD='CustomInput' and Key='GENSQL'         then trim(substr(Value,1,20))   end )as GENSQL
,max(case when RECORD='CustomInput' and Key='USERSQL'        then trim(substr(Value,1,5000)) end )as USERSQL
,max(case when RECORD='CustomInput' and Key='CONTAFTERSQL'   then trim(substr(Value,1,6))    end )as CONTAFTERSQL
,max(case when RECORD='CustomInput' and Key='AFTERSQL'       then Value                      end )as AFTERSQL
,max(case when RECORD='CustomInput' and Key='PARRARYSIZE'    then trim(substr(Value,1,50))   end )as PARRARYSIZE
,max(case when RECORD='CustomInput' and Key='TRANSSIZE'      then trim(substr(Value,1,50))   end )as TRANSSIZE
from dsxml_StageInfo 
where stype = v_Stype and RECORD in('CustomInput')  --CustomInput
and Key in('TABLE','TABLESPACE','PARTKEY','TARGETTABLE','DROPTABLE','DROPDDL','GENDROPDDL','CREATETABLE','CREATEDDL','GENCREATEDDL','CONTBEFORESQL','BEFORESQL','INPUTMODE','GENSQL','USERSQL','CONTAFTERSQL','AFTERSQL','PARRARYSIZE','TRANSSIZE')
and jobname in(select jobname from t)
group by Jobname,RECORD,StageID
union all
select b.Jobname,b.RECORD,a.INPUTPINS
,max(case when RECORD='CustomStage' and B.Key='TABLE'          then Value                      end )as "TABLE"
,max(case when RECORD='CustomStage' and B.KEY='USETABLESPACE'  then trim(substr(Value,1,20))   end )as TABLESPACE
,null as PARTKEY
,max(case when RECORD='CustomStage' and B.Key='TABLENAME'      then trim(substr(Value,1,100))  end )as TARGETTABLE
,null as DROPTABLE
,null as DROPDDL
,null as GENDROPDDL
,null as CREATETABLE
,null as CREATEDDL
,null as GENCREATEDDL
,null as CONTBEFORESQL
,null as BEFORESQL
,max(case when RECORD='CustomStage' and B.Key='LOADMODE'       then trim(substr(Value,1,50))   end )as INPUTMODE
,'No' as GENSQL
,max(case when RECORD='CustomStage' and B.Key='TABLENAME' then 'insert into '||trim(substr(Value,1,100)) end )USERSQL
,null as CONTAFTERSQL
,null as AFTERSQL
,null as PARRARYSIZE
,null as TRANSSIZE
from dsxml_StageInfo b 
inner join dsxml_CustomStage a on b.jobname = a.jobname and b.Stageid = a.Stageid 
where b.stype = v_Stype and a.stype = v_Stype 
and b.RECORD = 'CustomStage' 
and a.StageType = 'UDBLoad'  --UDBLoad
and b.jobname in(select jobname from t)
group by b.Jobname,b.RECORD,a.INPUTPINS
union all
select Jobname,RECORD,StageID INPUTPINS
,max(case when RECORD='CustomInput' and Key='TABLE'          then Value                      end )as "TABLE"
,null as TABLESPACE
,null as PARTKEY
,max(case when RECORD='CustomInput' and Key='TableName'      then trim(substr(Value,1,100))  end )as TARGETTABLE
,null as DROPTABLE
,max(case when RECORD='CustomInput' and Key='DDLDrop'        then trim(substr(Value,1,100))  end )as DROPDDL
,null as GENDROPDDL
,null as CREATETABLE
,max(case when RECORD='CustomInput' and Key='DDLCreate'      then trim(substr(Value,1,4000)) end )as CREATEDDL
,null as GENCREATEDDL
,null as CONTBEFORESQL
,null as BEFORESQL
,null as INPUTMODE
,'No' as GENSQL
,max(case when Key='SqlInsert' then Value   end )as USERSQL
,null as CONTAFTERSQL
,null as AFTERSQL
,max(case when RECORD='CustomInput' and Key='ArraySize'       then trim(substr(Value,1,50))   end )as PARRARYSIZE
,max(case when RECORD='CustomInput' and Key='TransactionSize' then trim(substr(Value,1,50))   end )as TRANSSIZE
from dsxml_StageInfo 
where stype = v_Stype 
and RECORD in('ODBCInput')  --ODBCInput
and Key in('TableName','Action','SqlInsert','SqlUpdate','SqlDelete','DDLCreate','DDLDrop','Isolation','TransactionSize','ArraySize')
and jobname in(select jobname from t)
group by Jobname,RECORD,StageID 
union all
select Jobname,RECORD,StageID INPUTPINS
,null as "TABLE"
,null as TABLESPACE
,null as PARTKEY
,null as TARGETTABLE
,null as DROPTABLE
,null as DROPDDL
,null as GENDROPDDL
,null as CREATETABLE
,null as CREATEDDL
,null as GENCREATEDDL
,null as CONTBEFORESQL
,null as BEFORESQL
,null as INPUTMODE
,'No' as GENSQL
,max(case when Key='FileName' then 'insert into '||trim(substr(Value,1,100))end)as USERSQL
,null as CONTAFTERSQL
,null as AFTERSQL
,null as PARRARYSIZE
,null as TRANSSIZE
from dsxml_StageInfo 
where stype = v_Stype
and RECORD in('HashedInput','SeqInput')  --HashedInput/SeqInput
and Key in('FileName')
and jobname in(select jobname from t)
group by Jobname,RECORD,StageID 
)
--OUTPUTPINS
,OutputPins as(
select Jobname,RECORD,StageID OUTPUTPINS
,max(case when RECORD='CustomOutput' and Key='TABLE'         then trim(substr(Value,1,100))  end )as "TABLE"
,max(case when RECORD='CustomOutput' and Key='SOURCETABLES'  then trim(substr(Value,1,100))  end )as SOURCETABLES
,max(case when RECORD='CustomOutput' and Key='WHERE_CLAUSE'  then trim(substr(Value,1,200))  end )as WHERE_CLAUSE
,max(case when RECORD='CustomOutput' and Key='OTHER_CLAUSES' then trim(substr(Value,1,50))   end )as OTHER_CLAUSES
,max(case when RECORD='CustomOutput' and Key='CONTBEFORESQL' then trim(substr(Value,1,6))    end )as CONTBEFORESQL
,max(case when RECORD='CustomOutput' and Key='BEFORESQL'     then Value                      end )as BEFORESQL
,max(case when RECORD='CustomOutput' and Key='GENSQL'        then trim(substr(Value,1,20))   end )as GENSQL
,max(case when RECORD='CustomOutput' and Key='USERSQL'       then Value                      end )as USERSQL
,max(case when RECORD='CustomOutput' and Key='SQLBUILDERSQL' then trim(substr(Value,1,100))  end )as SQLBUILDERSQL
,max(case when RECORD='CustomOutput' and Key='FULLYGENSQL'   then trim(substr(Value,1,100))  end )as FULLYGENSQL
,max(case when RECORD='CustomOutput' and Key='CONTAFTERSQL'  then trim(substr(Value,1,6))    end )as CONTAFTERSQL
,max(case when RECORD='CustomOutput' and Key='AFTERSQL'      then Value                      end )as AFTERSQL
,max(case when RECORD='CustomOutput' and Key='PREFETCH'      then trim(substr(Value,1,50))   end )as PREFETCH
from dsxml_StageInfo 
where stype = v_Stype
and RECORD in('CustomOutput')  --CustomOutput
and Key in('TABLE','SOURCETABLES','WHERE_CLAUSE','OTHER_CLAUSES','CONTBEFORESQL','BEFORESQL','GENSQL','USERSQL','SQLBUILDERSQL','FULLYGENSQL','CONTAFTERSQL','AFTERSQL','PREFETCH')
and jobname in(select jobname from t)
group by Jobname,RECORD,StageID 
union all
select Jobname,RECORD,StageID OUTPUTPINS
,max(case when Key='TABLE'         then trim(substr(Value,1,100))  end )as "TABLE"
,max(case when Key='SOURCETABLES'  then trim(substr(Value,1,100))  end )as SOURCETABLES
,max(case when Key='WHERE_CLAUSE'  then trim(substr(Value,1,200))  end )as WHERE_CLAUSE
,max(case when Key='OTHER_CLAUSES' then trim(substr(Value,1,50))   end )as OTHER_CLAUSES
,max(case when Key='CONTBEFORESQL' then trim(substr(Value,1,6))    end )as CONTBEFORESQL
,max(case when Key='BEFORESQL'     then Value                      end )as BEFORESQL
,'No'                                                                   as GENSQL
,max(case when Key='SqlPrimary'    then Value                      end )as USERSQL
,max(case when Key='SQLBUILDERSQL' then trim(substr(Value,1,100))  end )as SQLBUILDERSQL
,max(case when Key='FULLYGENSQL'   then trim(substr(Value,1,100))  end )as FULLYGENSQL
,max(case when Key='CONTAFTERSQL'  then trim(substr(Value,1,6))    end )as CONTAFTERSQL
,max(case when Key='AFTERSQL'      then Value                      end )as AFTERSQL
,max(case when Key='PREFETCH'      then trim(substr(Value,1,50))   end )as PREFETCH
from dsxml_StageInfo 
where stype = v_Stype
and RECORD in('ODBCOutput')  --ODBCOutput
and Key in('SqlPrimary')
and jobname in(select jobname from t)
group by Jobname,RECORD,StageID 
union all
select Jobname,RECORD,StageID OUTPUTPINS
,null as "TABLE"
,null as SOURCETABLES
,null as WHERE_CLAUSE
,null as OTHER_CLAUSES
,null as CONTBEFORESQL
,null as BEFORESQL
,'No' as GENSQL
,max(case when Key='FileName' then 'select * from '||trim(substr(Value,1,100))end)as USERSQL
,null as SQLBUILDERSQL
,null as FULLYGENSQL
,null as CONTAFTERSQL
,null as AFTERSQL
,null as PREFETCH
from dsxml_StageInfo 
where stype = v_Stype
and RECORD in('HashedOutput','SeqOutput')  --HashedOutput/SeqOutput
and Key in('FileName')
and jobname in(select jobname from t)
group by Jobname,RECORD,StageID 
)
select v_Stype,'dsjob' JobType,CATEGORY,t.JOBNAME,t.RECORD,t.STAGEID,a.StageName StageName,a.StageType,a.Inputpins,a.Outputpins,t.Server,t.UserName
--,(select coalesce(trim(left(v.Value,50)),t.Server)     Server from dsxml_StageProperty v where v.stype = v_Stype and v.RECORD = 'CustomStage' and t.jobname = v.jobname and t.Stageid = v.Stageid and v.Key in('SERVER','DBNAME','DATABASE'))
--,(select coalesce(trim(left(v.Value,50)),t.UserName) UserName from dsxml_StageProperty v where v.stype = v_Stype and v.RECORD = 'CustomStage' and t.jobname = v.jobname and t.Stageid = v.Stageid and v.Key in('USERNAME','USERID'))
,in.BEFORESQL Input_BEFORESQL
,decode(in.GENSQL,'Yes','insert into '||coalesce(in.TARGETTABLE,in."TABLE"),in.USERSQL)as Input_SQL  
,in.AFTERSQL Input_AFTERSQL 
,out.BEFORESQL Output_BEFORESQL
,decode(out.GENSQL,'Yes','select * from '||coalesce(out.SOURCETABLES,out."TABLE"),out.USERSQL)as Output_SQL
,out.AFTERSQL Output_AFTERSQL
from t 
left join dsxml_CustomStage a on a.stype = v_Stype and t.jobname = a.jobname and t.Stageid = a.Stageid
left join InputPins in on a.Jobname = in.Jobname and in.InputPins in(select str from table(db2inst1.dsxml_OutputPins(a.InputPins)))
left join OutputPins out on a.Jobname = out.Jobname and out.OutputPins in(select str from table(db2inst1.dsxml_OutputPins(a.OutputPins)))
union all
select stype,'dsjob' JobType,CATEGORY,jobname,record,stageid,stagename,stagetype,inputpins,outputpins,server,username,null,null,null,beforesql Output_BEFORESQL,sql Output_SQL,aftersql Output_AFTERSQL from p
--left join dsxml_HashedFileStage b on t.jobname = b.jobname and t.Stageid = b.Stageid
--left join dsxml_ODBCStage c on t.jobname = c.jobname and t.Stageid = c.Stageid
--left join dsxml_SeqFileStage d on t.jobname = d.jobname and t.Stageid = d.Stageid
--left join dsxml_ContainerStage e on t.jobname = e.jobname and t.Stageid = e.Stageid
--left join dsxml_TransformerStage f on t.jobname = f.jobname and t.Stageid = f.Stageid
union all
--11.kjb.sql
select a.stype,'ktljob' JobType,a.JOB_DIRECTORY,a.jobname,a.ENTRYNAME,a.TRANSNAME,null,a.TYPE,null,null,a.CONNECTION,null,null,null,null,null,a.SQL,null
from ktl_jobEntries a
where exists(select 1 from ktl_hops b where a.stype = b.stype and a.Jobname = b.Jobname and a.JOB_DIRECTORY = b.JOB_DIRECTORY and a.ENTRYNAME in(b.FROM,b.TO) and b.ENABLED = 'Y')
and a.STYPE = v_Stype and a.Jobname = v_jobname --and a.Job_Directory = '/hld/kjb_ods_to_ods_hldtran_inc'
union all
--12.ktr.sql
select c.stype,'ktljob' JobType,a.JOB_DIRECTORY,a.jobname,null,c.TRANSNAME,c.STEPNAME,c.TYPE,null,null,c.CONNECTION,null,null,null,null,null,c.SQL,null
from ktl_jobEntries a,ktl_steps c 
where a.stype = c.stype and a.ENTRYNAME = c.TRANSNAME and a.DIRECTORY = c.TRANS_DIRECTORY
and   exists(select 1 from ktl_hops b where c.stype = b.stype and c.TRANSNAME = b.TRANSNAME and c.TRANS_DIRECTORY = b.TRANS_DIRECTORY and c.STEPNAME in(b.FROM,b.TO) and b.ENABLED = 'Y')
and a.STYPE = v_Stype and a.Jobname = v_jobname --and a.Job_Directory = '/hld/kjb_ods_to_ods_hldtran_inc'
;
end!

--维护序列作业
merge into dbo.etl_jobino_tmp a
using dsxml_StageInfo b on b.record = 'JSJobActivity' and b.key in('Jobname') and a.stype = b.stype and a.jobname = b.value
when matched 
then update set a.SEQJOBNAME = b.jobname
else ignore;
--更新作业目录
update dbo.etl_jobino_tmp a
set JOBLOCATE = (select CATEGORY from dsxml_JobInfo b where a.stype = b.stype and a.jobname = b.jobname);

--表名使用大写
update(
select * from dbo.etl_jobino_tmp 
where stype = 'bus_20190328'
and jobtype = 'dsjob' 
and inofile not like '%.%' 
and inofile not like '%/%' 
and inofile not like 'hf%' 
and inofile not like '%#%' 
and schema not like '%path'
)set INOFILE = upper(INOFILE);
--已经存在的,schema=DBO的记录：
delete from dbo.etl_jobino_tmp a where stype = 'bus_20190328' and schema in('DBO','dbo')
and exists(
	select 1 from (select * from dbo.etl_jobino_tmp where stype = 'bus_20190328' and schema in('#v_jb_ods_schema#','#v_jb_dw_schema#','#v_jb_speed_schema#','#v_jb_speedin_schema#'))b
	where a.stype = b.stype and a.jobname = b.jobname and a.ino = b.ino and a.snd = b.snd and a.inofile = b.inofile);
--删除session表
delete from (select * from dbo.etl_Jobino_tmp where schema in('SESSION','session'));
--schema错误的
delete from dbo.etl_jobino_tmp a 
where stype = 'bus_20190328' 
and snd = '#v_jb_ods_svr#' 
and schema = '#v_jb_dw_schema#'
and exists(
	select 1 from dbo.etl_jobino_tmp b where a.stype = b.stype and a.jobname = b.jobname and a.ino = b.ino and a.snd = b.snd and a.inofile = b.inofile
	and b.schema = '#v_jb_ods_schema#');
--schema错误的
delete from dbo.etl_jobino_tmp a 
where stype = 'bus_20190328' 
and snd = '#v_jb_dw_svr#' 
and schema = '#v_jb_ods_schema#'
and exists(
	select 1 from dbo.etl_jobino_tmp b where a.stype = b.stype and a.jobname = b.jobname and a.ino = b.ino and a.snd = b.snd and a.inofile = b.inofile
	and b.schema = '#v_jb_dw_schema#');
--查看是否需要删除
select * from dbo.etl_Jobino_tmp where inofile like '#%'
	
SET INTEGRITY FOR dbo.etl_jobino     ALL IMMEDIATE UNCHECKED;
update(
select * from dbo.etl_jobino where jobname in('jb_dwn_s01_hday_d_all','jb_dwn_s01_htot_d_all')
)set ISVALID = 0;
SET INTEGRITY FOR dbo.etl_jobino     OFF READ ACCESS;

