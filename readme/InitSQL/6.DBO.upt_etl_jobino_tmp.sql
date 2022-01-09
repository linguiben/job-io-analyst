/*upt_etl_jobino_tmp*/
CREATE OR REPLACE PROCEDURE DBO.upt_etl_jobino_tmp(IN v_stype varchar(50))
begin
--1.维护序列作业
merge into dbo.etl_jobino_tmp a
using dsxml_StageInfo b on b.record = 'JSJobActivity' and b.key in('Jobname') and a.stype = b.stype and a.jobname = b.value
when matched and a.stype = v_stype
then update set a.SEQJOBNAME = b.jobname
else ignore;

--2.表名使用大写
update(
select * from dbo.etl_jobino_tmp 
where stype = v_stype
and jobtype in('dsjob','ktljob')
and inofile not like '%.%' 
and inofile not like '%/%' 
and inofile not like 'hf%' 
and inofile not like '%#%' 
and schema not like '%path'
)set INOFILE = upper(INOFILE);

--3.删除已经存在的,schema=DBO的(冗余)记录：
delete from(
	select * from dbo.etl_jobino_tmp a where stype = v_stype and schema in('DBO','dbo')
	and exists(
		select 1 from (select * from dbo.etl_jobino_tmp where stype = v_stype and schema in('#v_jb_ods_schema#','#v_jb_dw_schema#','#v_jb_speed_schema#','#v_jb_speedin_schema#'))b
		where a.stype = b.stype and a.jobname = b.jobname and a.ino = b.ino and a.snd = b.snd and a.inofile = b.inofile)
);

--4.删除session表
delete from (select * from dbo.etl_Jobino_tmp where schema in('SESSION','session'));

--5.1.删除schema错误的
delete from(
	select * from dbo.etl_jobino_tmp a 
	where stype = v_stype
	and snd = '#v_jb_ods_svr#' 
	and schema = '#v_jb_dw_schema#'
	and exists(
		select 1 from dbo.etl_jobino_tmp b where a.stype = b.stype and a.jobname = b.jobname and a.ino = b.ino and a.snd = b.snd and a.inofile = b.inofile
		and b.schema = '#v_jb_ods_schema#')
);
--5.2.删除schema错误的
delete from(
	select * from dbo.etl_jobino_tmp a 
	where stype = v_stype
	and snd = '#v_jb_dw_svr#' 
	and schema = '#v_jb_ods_schema#'
	and exists(
		select 1 from dbo.etl_jobino_tmp b where a.stype = b.stype and a.jobname = b.jobname and a.ino = b.ino and a.snd = b.snd and a.inofile = b.inofile
		and b.schema = '#v_jb_dw_schema#')
);

--6、--isvalid -> 0
         --select * from dbo.etl_jobino_tmp a where exists(select 1 from dbo.etl_jobino b where a.jobname = b.jobname and b.isvalid = 0)and stype = 'bus';
update dbo.etl_jobino_tmp a set ISVALID = 1;
update(select * from dbo.etl_jobino_tmp a
where not exists(select 1 from dbo.iControl_joblist b where coalesce(a.SEQJOBNAME,a.jobname) = b.name)
and stype = v_stype
and JOBLOCATE <> '\Jobs\datadown\xzb'
and jobname not like 'boruntime%'
and jobname not like 'chk%'
and jobname not like 'pro%'
and jobtype in('dsjob','ktljob')
and isvalid = 1)
set ISVALID = 0;

--7、--job输出、输入同一临时表(独占表)
declare global temporary table session.temp1(jobname varchar(128),inofile varchar(50))not logged with replace on commit preserve rows;
create index session.idx_temp1 on session.temp1(jobname,inofile)ALLOW REVERSE SCANS;
insert into session.temp1 
select jobname,inofile from dbo.etl_jobino_tmp where isvalid = 1 and stype = v_stype and (upper(inofile) like '%TEMP%' or inofile like '%TMP%')
group by jobname,inofile having count(distinct ino) > 1;--select count(1) from session.temp1 
update dbo.etl_jobino_tmp a --标记独占的表
set remark = 'independence'
where exists(select 1 from session.temp1 b where a.jobname = b.jobname and a.inofile = b.inofile)
and ino = 'out';

end!

--adpp特殊处理
delete from (select * from dbo.etl_jobino_tmp where stype = 'ADPP' and inofile in('__subquery1__','DUAL','dual','||ACCFACTOR_NAME','||MATRIX_NAME'));
update dbo.etl_jobino_tmp set INOFILE = upper(INOFILE) where stype = 'ADPP';
update(select * from dbo.etl_jobino_tmp where stype = 'adpp_20200825' and inofile like '%.csv')set INOFILE = replace(INOFILE,'/','')
select * from dbo.etl_jobino_tmp where stype = 'ADPP'
