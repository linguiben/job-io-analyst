db2 connect to dwmot2 user db2inst1 using password
db2 "import from 'D:\Jupiter\workPlace\001.资料\012.dsjob_analysis\008.kettle_analysis\KTLXML.del' of del xml from 'D:\Jupiter\workPlace\001.资料\012.dsjob_analysis\008.kettle_analysis' insert into DSXML(dsName,dsxml)"
db2 terminate

--1.ktl_jobInfo
select y.*
from DSXML s,
xmltable('$DSXML/repository/jobs/job' 
columns
 jobName varchar(100) path './name'
,directory varchar(200) path './directory'
,job_version varchar(10)path './job_version'
,job_status varchar(2)path './job_status'
,created_user varchar(10)path './created_user'
,created_date varchar(20)path './created_date'
,modified_user varchar(10)path './modified_user'
,modified_date varchar(20)path './modified_date'
,notepad1 varchar(1000)path './notepads/notepad[1]/note'
,notepad2 varchar(1000)path './notepads/notepad[2]/note'
,notepad3 varchar(1000)path './notepads/notepad[3]/note'
,notepad4 varchar(1000)path './notepads/notepad[4]/note'
,notepad5 varchar(1000)path './notepads/notepad[5]/note'
,notepad6 varchar(1000)path './notepads/notepad[6]/note'
,notepad7 varchar(1000)path './notepads/notepad[7]/note'
,notepad8 varchar(1000)path './notepads/notepad[8]/note'
,description varchar(1000)path './description'
,extended_description varchar(2000)path './extended_description'
) as y
where s.dsName = 'ktlrep_20200520'
and y.jobName in('kjb_ods_to_ods_hldtran_inc')

--2.ktl_jobEntries
select y.*
from DSXML s,
xmltable('$DSXML/repository/jobs/job/entries/entry'
columns
 jobName varchar(100) path '../../name'
,job_directory varchar(100) path '../../directory'
,entryName varchar(200) path './name'
,type varchar(50) path './type'
,filename varchar(100) path './filename'
,tranSname varchar(100) path './transname'
,trans_object_id varchar(10) path './trans_object_id'
,directory varchar(100) path './directory'
,connection varchar(100) path './connection'
,sql varchar(32672) path './sql'
,sqlfilename varchar(100) path './sqlfilename'
,description varchar(1000) path './description'
) as y
where s.dsName = 'ktlrep_20200520'
and y.jobName in('kjb_ods_to_ods_hldtran_inc')
and y.job_directory <> '/'

--3.1.ktl_hops job/hops/hop
select y.*
from DSXML s,
xmltable('$DSXML/repository/jobs/job/hops/hop'
columns
 jobName varchar(100) path '../../name'
,job_directory varchar(100) path '../../directory'
,from varchar(100) path './from'
,to varchar(100) path './to'
,from_nr varchar(2) path './from_nr'
,to_nr varchar(2) path './to_nr'
,evaluation varchar(2) path './evaluation'
,unconditional varchar(2) path './unconditional'
,enabled varchar(2) path './enabled'
) as y
where s.dsName = 'ktlrep_20200520'
and y.jobName in('kjb_ods_to_ods_hldtran_inc')
and y.job_directory <> '/';
--3.2.transformation/order/hop
select y.*
from DSXML s,
xmltable('$DSXML/repository/transformations/transformation/order/hop'
columns
 transname varchar(100) path '../../info/name'
,trans_directory varchar(100) path '../../info/directory'
,from varchar(100) path './from'
,to varchar(100) path './to'
,enabled varchar(2) path './enabled'
,Value varchar(4000) path '.'
) as y
where s.dsName = 'ktlrep_20200520'
and y.transname in('ktr_hldtran','ktr_clnt_id')
and y.trans_directory <> '/';

--4.ktl_transInfo
select y.*
from DSXML s,
xmltable('$DSXML/repository/transformations/transformation' 
columns
 transname varchar(100) path './info/name'
,directory varchar(100) path './info/directory/text()'
,version varchar(10) path './info/trans_version/text()'
,trans_type varchar(10) path './info/trans_type/text()'
,trans_status varchar(10) path './info/trans_status/text()'
,created_user varchar(10)path './info/created_user'
,created_date varchar(20)path './info/created_date'
,modified_user varchar(10)path './info/modified_user'
,modified_date varchar(20)path './info/modified_date'
,description varchar(1000) path './info/description/text()'
,extended_description varchar(1000) path './info/extended_description/text()'
,notepad1 varchar(1000) path './notepads/notepad[1]/note'
,notepad2 varchar(1000) path './notepads/notepad[2]/note'
,notepad3 varchar(1000) path './notepads/notepad[3]/note'
,notepad4 varchar(1000) path './notepads/notepad[4]/note'
,notepad5 varchar(1000) path './notepads/notepad[5]/note'
,notepad6 varchar(1000) path './notepads/notepad[6]/note'
,notepad7 varchar(1000) path './notepads/notepad[7]/note'
,notepad8 varchar(1000) path './notepads/notepad[8]/note'
) as y
where s.dsName = 'ktlrep_20200520'
and y.transname in('ktr_hldtran','ktr_clnt_id')

--5.ktl_steps
select TRANSNAME,TRANS_DIRECTORY,STEPNAME,TYPE,CONNECTION,SCHEMA,TABLE
,case when Type in('TableOutput') then 'insert into '||decode(SCHEMA,'','.',SCHEMA)||'.'||TABLE 
 when Type in('ExcelOutput','TypeExitExcelWriterStep') then 'writeFile into '||trim(Table)||coalesce(remark,'')
 when Type in('ExcelInput') then 'readFile from '||trim(Table)
 when Type in('Update','InsertUpdate') then 'Update '||decode(SCHEMA,'','.',SCHEMA)||'.'||TABLE 
 else SQL END as SQL
,TRUNCATE,USE_BATCH,TABLENAME_IN_FIELD,TABLENAME_FIELD,SPECIFY_FIELDS
,get_ktl_column_names(y.fields)column_names,get_ktl_stream_names(y.fields)stream_names,DESCRIPTION
from DSXML s,
xmltable('$DSXML/repository/transformations/transformation/step'
columns
 transName varchar(100) path '../info/name'
,trans_directory varchar(100) path '../info/directory'
,stepName   varchar(100) path './name'
,type       varchar(50)  path './type' --TableOutput TableInput
,connection varchar(100) path './connection'
,schema     varchar(100) path './schema|lookup/schema'
,table      varchar(100) path './fn:string-join((table,lookup/table,file/name,file/extention[text()!=""]),".")'
,sql        varchar(4000) path './sql'
,truncate   varchar(10)  path './truncate'
,use_batch  varchar(10)  path './use_batch'
,tablename_in_field varchar(100) path './tablename_in_field'
,tablename_field    varchar(100) path './tablename_field'
,specify_fields     varchar(100) path './specify_fields'
,fields       xml path './fields' 
,description        varchar(4000) path './description'
,remark varchar(120) path '(template[../type="ExcelOutput"]|template[../type="TypeExitExcelWriterStep"])[filename/text()!=""]/fn:concat(";readfile from ",filename)'
) as y
where s.dsName = 'ktlrep_20200526'
and y.transname in('ktr_hldtran','ktr_clnt_id','ktr_rpt','ktr_Report_HLD')

/*1.create kettle tables*/
CALL DBO.DROP_EXISTS('TABLE','ktl_jobInfo','DB2INST1'); 
CALL DBO.DROP_EXISTS('TABLE','ktl_jobEntries','DB2INST1');
CALL DBO.DROP_EXISTS('TABLE','ktl_hops','DB2INST1');
CALL DBO.DROP_EXISTS('TABLE','ktl_transInfo','DB2INST1');
CALL DBO.DROP_EXISTS('TABLE','ktl_steps','DB2INST1');
Create table ktl_jobInfo   (stype varchar(20),jobName varchar(100),directory varchar(200),job_version varchar(10),job_status varchar(2),created_user varchar(10),created_date varchar(20),modified_user varchar(10),modified_date varchar(20),notepad1 varchar(1000),notepad2 varchar(1000),notepad3 varchar(1000),notepad4 varchar(1000),notepad5 varchar(1000),notepad6 varchar(1000),notepad7 varchar(1000),notepad8 varchar(1000),description varchar(1000),extended_description varchar(2000))in dwspace index in dwidxspace;
Create table ktl_jobEntries(stype varchar(20),jobName varchar(100),job_directory varchar(200),entryName varchar(100),type varchar(50),filename varchar(100),tranSname varchar(100),trans_object_id varchar(10),directory varchar(100),connection varchar(100),sql varchar(32672),sqlfilename varchar(100),description varchar(1000))in dwspace index in dwidxspace;
Create table ktl_hops      (stype varchar(20),jobName varchar(108),job_directory varchar(200),transName varchar(100),trans_directory varchar(100),from varchar(100),to varchar(100),enabled varchar(50),from_nr varchar(2),to_nr varchar(2),evaluation varchar(2),unconditional varchar(2))in dwspace index in dwidxspace;
Create table ktl_transInfo (stype varchar(20),transName varchar(100),directory varchar(200),version varchar(10),trans_type varchar(10),trans_status varchar(10),created_user varchar(10),created_date varchar(50),modified_user varchar(10),modified_date varchar(50),description varchar(1000),extended_description varchar(1000),notepad1 varchar(1000),notepad2 varchar(1000),notepad3 varchar(1000),notepad4 varchar(1000),notepad5 varchar(1000),notepad6 varchar(1000),notepad7 varchar(1000),notepad8 varchar(1000))in dwspace index in dwidxspace;
Create table ktl_steps     (stype varchar(20),transName varchar(100),trans_directory varchar(100),stepName varchar(50),type varchar(50),connection varchar(100),schema varchar(100),table varchar(100),sql varchar(32672),truncate varchar(10),use_batch varchar(10),tablename_in_field varchar(100),tablename_field varchar(100),specify_fields varchar(100),column_names varchar(4000),stream_names varchar(4000),description varchar(6000))in dwspace index in dwidxspace;
create index idx1_ktl_jobInfo on ktl_jobInfo(stype,Jobname)allow reverse scans;
create index idx1_ktl_jobEntries on ktl_jobEntries(stype,Jobname,entryName)allow reverse scans;
create index idx1_ktl_hops on ktl_hops(stype,Jobname,transName)allow reverse scans;
create index idx1_ktl_transInfo on ktl_transInfo(stype,transName)allow reverse scans;
create index idx1_ktl_steps on ktl_steps(stype,transName)allow reverse scans;

/*2. create functions ??$SXML/fields/field??*/
create or replace function db2inst1.get_ktl_column_names(sxml xml)
returns varchar(4000)
begin atomic 
return 
with t(sxml) as(values (sxml))
	select substr(XMLCAST(XMLGROUP(','||x.column_name AS column_name) as varchar(4000)),2)column_name
	from t,xmltable('$SXML/field' columns
	column_name varchar(100) path './column_name|name'
	)as x;
end!
create or replace function db2inst1.get_ktl_stream_names(sxml xml)
returns varchar(4000)
begin atomic 
return 
with t(sxml) as(values (sxml))
	select substr(XMLCAST(XMLGROUP(','||x.stream_name AS stream_name) as varchar(4000)),2)stream_name
	from t,xmltable('$SXML/field' columns
	stream_name varchar(100) path './stream_name'
	)as x;
end!
/*
create or replace function db2inst1.get_ktl_column_names(sxml xml)
returns varchar(4000)
begin atomic 
return 
with t(sxml) as(values (sxml))
	select substr(XMLCAST(XMLGROUP(','||x.column_name AS column_name) as varchar(4000)),2)column_name
	from t,xmltable('$SXML/field' columns
	column_name varchar(100) path './(column_name|name)'
	)as x;
end!
create or replace function db2inst1.get_ktl_stream_names(sxml xml)
returns varchar(4000)
begin atomic 
return 
with t(sxml) as(values (sxml))
	select substr(XMLCAST(XMLGROUP(','||x.stream_name AS stream_name) as varchar(4000)),2)stream_name
	from t,xmltable('$SXML/field' columns
	stream_name varchar(100) path './(stream_name|rename)'
	)as x;
end!
*/
----
values db2inst1.get_ktl_stream_names(xmlparse(document '
    <fields>
        <field>
          <column_name>CHDRNUM</column_name>
          <stream_name>CHDRNUM</stream_name>
        </field>
        <field>
          <column_name>AMT</column_name>
          <stream_name>AMT</stream_name>
        </field>
    </fields>'))!
values db2inst1.get_ktl_column_names(xmlparse(document '
    <fields>
        <field>
          <column_name>CHDRNUM</column_name>
          <stream_name>CHDRNUM</stream_name>
        </field>
        <field>
          <column_name>AMT</column_name>
          <stream_name>AMT</stream_name>
        </field>
    </fields>'))!


--***exec: 维护xml分析
call DBO.P_ktljobinfo('ktlrep_20200526')!

/*3.proc: 维护kettle_anlysis涉及到的数据*/
CREATE OR REPLACE PROCEDURE DBO.P_ktljobinfo(IN v_stype varchar(50))
BEGIN
	EXECUTE IMMEDIATE 'SET INTEGRITY FOR db2inst1.ktl_jobInfo    ALL IMMEDIATE UNCHECKED';
	EXECUTE IMMEDIATE 'SET INTEGRITY FOR db2inst1.ktl_jobEntries ALL IMMEDIATE UNCHECKED';
	EXECUTE IMMEDIATE 'SET INTEGRITY FOR db2inst1.ktl_hops       ALL IMMEDIATE UNCHECKED';
	EXECUTE IMMEDIATE 'SET INTEGRITY FOR db2inst1.ktl_transInfo  ALL IMMEDIATE UNCHECKED';
	EXECUTE IMMEDIATE 'SET INTEGRITY FOR db2inst1.ktl_steps      ALL IMMEDIATE UNCHECKED';
	delete from db2inst1.ktl_jobInfo    where stype = v_stype;
	delete from db2inst1.ktl_jobEntries where stype = v_stype;
	delete from db2inst1.ktl_hops       where stype = v_stype;
	delete from db2inst1.ktl_transInfo  where stype = v_stype;
	delete from db2inst1.ktl_steps      where stype = v_stype;
	commit;
--1.ktl_jobInfo
insert into ktl_jobInfo(stype,JOBNAME,DIRECTORY,JOB_VERSION,JOB_STATUS,CREATED_USER,CREATED_DATE,MODIFIED_USER,MODIFIED_DATE,NOTEPAD1,NOTEPAD2,NOTEPAD3,NOTEPAD4,NOTEPAD5,NOTEPAD6,NOTEPAD7,NOTEPAD8,DESCRIPTION,EXTENDED_DESCRIPTION)
select v_stype as stype,y.*
from DSXML s,
xmltable('$DSXML/repository/jobs/job' 
columns
 jobName varchar(100) path './name'
,directory varchar(200) path './directory'
,job_version varchar(10)path './job_version'
,job_status varchar(2)path './job_status'
,created_user varchar(10)path './created_user'
,created_date varchar(20)path './created_date'
,modified_user varchar(10)path './modified_user'
,modified_date varchar(20)path './modified_date'
,notepad1 varchar(1000)path './notepads/notepad[1]/note'
,notepad2 varchar(1000)path './notepads/notepad[2]/note'
,notepad3 varchar(1000)path './notepads/notepad[3]/note'
,notepad4 varchar(1000)path './notepads/notepad[4]/note'
,notepad5 varchar(1000)path './notepads/notepad[5]/note'
,notepad6 varchar(1000)path './notepads/notepad[6]/note'
,notepad7 varchar(1000)path './notepads/notepad[7]/note'
,notepad8 varchar(1000)path './notepads/notepad[8]/note'
,description varchar(1000)path './description'
,extended_description varchar(2000)path './extended_description'
) as y
where s.dsName = v_stype;
--2.ktl_jobEntries
insert into ktl_jobEntries(stype,JOBNAME,JOB_DIRECTORY,ENTRYNAME,TYPE,FILENAME,TRANSNAME,TRANS_OBJECT_ID,DIRECTORY,CONNECTION,SQL,SQLFILENAME,DESCRIPTION)
select v_stype as stype,y.*
from DSXML s,
xmltable('$DSXML/repository/jobs/job/entries/entry'
columns
 jobName varchar(100) path '../../name'
,job_directory varchar(100) path '../../directory'
,entryName varchar(200) path './name'
,type varchar(50) path './type'
,filename varchar(100) path './filename'
,tranSname varchar(100) path './transname'
,trans_object_id varchar(10) path './trans_object_id'
,directory varchar(100) path './directory'
,connection varchar(100) path './connection'
,sql varchar(32672) path './sql'
,sqlfilename varchar(100) path './sqlfilename'
,description varchar(1000) path './description'
) as y
where s.dsName = v_stype;
--3.1.job/hops/hop : ktl_hops
insert into ktl_hops(stype,JOBNAME,JOB_DIRECTORY,FROM,TO,FROM_NR,TO_NR,EVALUATION,UNCONDITIONAL,ENABLED)
select v_stype as stype,y.*
from DSXML s,
xmltable('$DSXML/repository/jobs/job/hops/hop'
columns
 jobName varchar(100) path '../../name'
,job_directory varchar(100) path '../../directory'
,from varchar(100) path './from'
,to varchar(100) path './to'
,from_nr varchar(2) path './from_nr'
,to_nr varchar(2) path './to_nr'
,evaluation varchar(2) path './evaluation'
,unconditional varchar(2) path './unconditional'
,enabled varchar(2) path './enabled'
) as y
where s.dsName = v_stype;
--3.2.transformation/order/hop : ktl_hops
insert into ktl_hops(stype,TRANSNAME,TRANS_DIRECTORY,FROM,TO,ENABLED)
select v_stype as stype,y.*
from DSXML s,
xmltable('$DSXML/repository/transformations/transformation/order/hop'
columns
 transname varchar(100) path '../../info/name'
,trans_directory varchar(100) path '../../info/directory'
,from varchar(100) path './from'
,to varchar(100) path './to'
,enabled varchar(2) path './enabled'
) as y
where s.dsName = v_stype;
--4.ktl_transInfo
insert into ktl_transInfo(stype,TRANSNAME,DIRECTORY,VERSION,TRANS_TYPE,TRANS_STATUS,CREATED_USER,CREATED_DATE,MODIFIED_USER,MODIFIED_DATE,DESCRIPTION,EXTENDED_DESCRIPTION,NOTEPAD1,NOTEPAD2,NOTEPAD3,NOTEPAD4,NOTEPAD5,NOTEPAD6,NOTEPAD7,NOTEPAD8)
select v_stype as stype,y.*
from DSXML s,
xmltable('$DSXML/repository/transformations/transformation' 
columns
 transname varchar(100) path './info/name'
,directory varchar(100) path './info/directory/text()'
,version varchar(10) path './info/trans_version/text()'
,trans_type varchar(10) path './info/trans_type/text()'
,trans_status varchar(10) path './info/trans_status/text()'
,created_user varchar(10)path './info/created_user'
,created_date varchar(20)path './info/created_date'
,modified_user varchar(10)path './info/modified_user'
,modified_date varchar(20)path './info/modified_date'
,description varchar(1000) path './info/description/text()'
,extended_description varchar(1000) path './info/extended_description/text()'
,notepad1 varchar(1000) path './notepads/notepad[1]/note'
,notepad2 varchar(1000) path './notepads/notepad[2]/note'
,notepad3 varchar(1000) path './notepads/notepad[3]/note'
,notepad4 varchar(1000) path './notepads/notepad[4]/note'
,notepad5 varchar(1000) path './notepads/notepad[5]/note'
,notepad6 varchar(1000) path './notepads/notepad[6]/note'
,notepad7 varchar(1000) path './notepads/notepad[7]/note'
,notepad8 varchar(1000) path './notepads/notepad[8]/note'
) as y
where s.dsName = v_stype;
--5.ktl_steps
insert into ktl_steps(stype,TRANSNAME,TRANS_DIRECTORY,STEPNAME,TYPE,CONNECTION,SCHEMA,TABLE,SQL,TRUNCATE,USE_BATCH,TABLENAME_IN_FIELD,TABLENAME_FIELD,SPECIFY_FIELDS,COLUMN_NAMES,STREAM_NAMES,DESCRIPTION)
select v_stype as stype,TRANSNAME,TRANS_DIRECTORY,STEPNAME,TYPE,CONNECTION,SCHEMA,TABLE
,case when Type in('TableOutput') then 'insert into '||decode(SCHEMA,'','.',SCHEMA)||'.'||TABLE 
 when Type in('ExcelOutput','TypeExitExcelWriterStep') then 'writeFile into '||trim(Table)||coalesce(remark,'')
 when Type in('ExcelInput') then 'readFile from '||trim(Table)
 when Type in('Update','InsertUpdate') then 'Update '||decode(SCHEMA,'','.',SCHEMA)||'.'||TABLE 
 else SQL END as SQL
,TRUNCATE,USE_BATCH,TABLENAME_IN_FIELD,TABLENAME_FIELD,SPECIFY_FIELDS,get_ktl_column_names(y.fields)column_names,get_ktl_stream_names(y.fields)stream_names,DESCRIPTION
from DSXML s,
xmltable('$DSXML/repository/transformations/transformation/step'
columns
 transName varchar(100) path '../info/name'
,trans_directory varchar(100) path '../info/directory'
,stepName   varchar(100) path './name'
,type       varchar(50)  path './type' --TableOutput TableInput ExcelOutput ExcelInput
,connection varchar(100) path './connection'
,schema     varchar(100) path './schema|lookup/schema'
,table      varchar(100) path './fn:string-join((table,lookup/table,file/name,file/extention[text()!=""]),".")'
,sql        varchar(4000) path './sql'
,truncate   varchar(10)  path './truncate'
,use_batch  varchar(10)  path './use_batch'
,tablename_in_field varchar(100) path './tablename_in_field'
,tablename_field    varchar(100) path './tablename_field'
,specify_fields     varchar(100) path './specify_fields'
,fields             xml			 path './fields'
,description        varchar(4000) path './description'
,remark varchar(120) path '(template[../type="ExcelOutput"]|template[../type="TypeExitExcelWriterStep"])[filename/text()!=""]/fn:concat(";readfile from ",filename)'
) as y
where s.dsName = v_stype;
	EXECUTE IMMEDIATE 'SET INTEGRITY FOR db2inst1.ktl_jobInfo    OFF READ ACCESS';
	EXECUTE IMMEDIATE 'SET INTEGRITY FOR db2inst1.ktl_jobEntries OFF READ ACCESS';
	EXECUTE IMMEDIATE 'SET INTEGRITY FOR db2inst1.ktl_hops       OFF READ ACCESS';
	EXECUTE IMMEDIATE 'SET INTEGRITY FOR db2inst1.ktl_transInfo  OFF READ ACCESS';
	EXECUTE IMMEDIATE 'SET INTEGRITY FOR db2inst1.ktl_steps      OFF READ ACCESS';
END!

/*查看kettle的sql*/
--1.kjb.sql
select a.stype,a.jobname,a.ENTRYNAME,a.TRANSNAME,null,a.TYPE,null,null,a.CONNECTION,null,null,null,null,null,a.SQL,null
from ktl_jobEntries a
where exists(select 1 from ktl_hops b where a.stype = b.stype and a.Jobname = b.Jobname and a.JOB_DIRECTORY = b.JOB_DIRECTORY and a.ENTRYNAME in(b.FROM,b.TO) and b.ENABLED = 'Y')
and a.STYPE = 'ktlrep_20200526' and a.Jobname = 'kjb_ods_to_ods_hldtran_inc' and a.Job_Directory = '/hld/kjb_ods_to_ods_hldtran_inc'
union all
--2.ktr.sql
select c.stype,a.jobname,null,c.TRANSNAME,c.STEPNAME,c.TYPE,null,null,c.CONNECTION,null,null,null,null,null,c.SQL,null
from ktl_jobEntries a,ktl_steps c 
where a.stype = c.stype and a.ENTRYNAME = c.TRANSNAME and a.DIRECTORY = c.TRANS_DIRECTORY
and   exists(select 1 from ktl_hops b where c.stype = b.stype and c.TRANSNAME = b.TRANSNAME and c.TRANS_DIRECTORY = b.TRANS_DIRECTORY and c.STEPNAME in(b.FROM,b.TO) and b.ENABLED = 'Y')
and a.STYPE = 'ktlrep_20200526' and a.Jobname = 'kjb_ods_to_ods_hldtran_inc' and a.Job_Directory = '/hld/kjb_ods_to_ods_hldtran_inc'



