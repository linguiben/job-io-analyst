CALL DBO.DROP_EXISTS('TABLE','JOB_RELATION','DBO');
CALL DBO.DROP_EXISTS('TABLE','JOB_LOCATION','DBO');
CREATE TABLE DBO.JOB_RELATION(userID varchar(20),datime timestamp,RID INTEGER,PNUMBER INTEGER,PREVIOUS VARCHAR(100),BNUMBER INTEGER,BEHIND VARCHAR(100),PATHLENGTH INTEGER,RNUMBER2 INTEGER,ISVALID INTEGER,JOBTYPE VARCHAR(10))IN USERSPACE1;
CREATE INDEX DBO.IDX1_JOB_RELATION ON DBO.JOB_RELATION (userID,datime,PREVIOUS ASC,BEHIND ASC,PATHLENGTH ASC)ALLOW REVERSE SCANS;
CREATE INDEX DBO.IDX2_JOB_RELATION ON DBO.JOB_RELATION(userID,datime,BEHIND ASC) ALLOW REVERSE SCANS;
CREATE TABLE DBO.JOB_LOCATION(userID varchar(20),datime timestamp,JOBNAME VARCHAR(100), X INTEGER, Y INTEGER, ISVALID INTEGER, JOBTYPE VARCHAR(10), JOBSTATUS INTEGER, COST TIME )IN DWSPACE INDEX IN DWIDXSPACE;
CREATE INDEX DBO.IDX1_JOB_LOCATION ON DBO.JOB_LOCATION(userID,datime,JOBNAME ASC) ALLOW REVERSE SCANS;
CREATE TABLE DBO.ETL_JOBINO_MQT AS( 
select stype,jobname,snd,schema,inofile,COUNT(1)Amount 
from dbo.etl_jobino 
where remark = 'independence' 
group by stype,jobname,snd,schema,inofile )
DATA INITIALLY DEFERRED --延迟初始化数据
REFRESH IMMEDIATE --REFRESH IMMEDIATE系统维护(需唯一键),REFRESH DEFERRED通过refresh table <tabname>刷新 
DISABLE QUERY OPTIMIZATION;
refresh table DBO.ETL_JOBINO_MQT;
create index DBO.idx1_ETL_JOBINO_MQT on DBO.ETL_JOBINO_MQT(STYPE,jobname,SND,SCHEMA,INOFILE);

CREATE Or replace PROCEDURE DBO.job_location_v3_1(In jbtype_in int, In sourceOrTarget int,In jb_in varchar(4096),In v_userID varchar(20) default null,In v_datime timestamp default null)
/*
call DBO.job_location_v3_1(1,0,'ECHDPF');
select * from syscat.procedures where procname= 'JOB_LOCATION'
version:3.0 modify: Jupiter 2021-04-23 
jbtype_in业务类型(1/2 bus/fin),sourceOrTarget(0/1 血源分析or影响分析),jb_in(要分析的job/表)
*/
 DYNAMIC RESULT SETS 1 --最大返回集个数
  P1: BEGIN
	DECLARE RID int; --循环次数
	DECLARE BNUMBER int; --每个父节点的子节点的行号
	DECLARE pathLength int; --路径长度
    DECLARE maxPathLength int; --最大路径长度(用于叶子节点的输出文件)
	DECLARE endFlag INT; --循环结束标记
	DECLARE stype_in varchar(50); --(业务)系统类型
	--DECLARE v_sql varchar(500);
	--DECLARE C1 CURSOR WITH RETURN FOR select a.*,b.x,b.y,row_number()over(partition by BEHIND order by PATHLENGTH)rn from dbo.job_relation a inner join dbo.job_location b on a.BEHIND = b.JOBNAME order by RID,PREVIOUS,BNUMBER;  --定义返回集
	--
	--jb_in;  --根节点
	set RID = 0;
	set BNUMBER = 0;
	set pathLength = 1;
	set endFlag = 0;
	set stype_in = decode(jbtype_in,1,'bus',2,'fin',3,'adpp',4,'zbx',5,'ssis'); --业务类型
	set v_userID = coalesce(v_userID,current CLIENT_WRKSTNNAME);
	set v_datime = coalesce(v_datime,current timestamp);
	--set v_sql = 'CALL SYSPROC.ADMIN_CMD(''import from /dev/null of del replace into dbo.job_relation'')';
	--execute immediate v_sql;
	--set v_sql = 'CALL SYSPROC.ADMIN_CMD(''import from /dev/null of del replace into dbo.job_location'')';
	--execute immediate v_sql;
	delete from dbo.job_relation where UserID = v_userID and datime = v_datime;
	delete from dbo.job_location where UserID = v_userID and datime = v_datime;
	commit;

	--独占表:自己清空且写入的临时表表(out+truncate/out+%temp)
	/*declare global temporary table session.temp1(jobname varchar(128),snd varchar(50),schema varchar(50),inofile varchar(50))not logged with replace on commit preserve rows;
	create index session.idx_temp1 on session.temp1(jobname,snd,schema,inofile)ALLOW REVERSE SCANS;
	insert into session.temp1
	--select jobname,inofile from dbo.etl_jobino where ISVALID <> 0 and (inofile like '%TEMP%' or inofile like '%TMP%') group by jobname,inofile having count(distinct ino) > 1;
	select distinct jobname,snd,schema,inofile from dbo.etl_jobino where stype = stype_in and remark = 'independence';*/

	IF(sourceOrTarget=1) THEN --1
	/*<影响分析*/
		--插入根节点,ALL为全部作业
		IF(upper(jb_in)<>'ALL') THEN
			insert into dbo.job_relation(UserID,datime,RID,pnumber,BNUMBER,previous,behind,pathLength,ISVALID,JOBTYPE)
			with u(jobname)AS(values (jb_in)),
			t(id,jobname1,jobname) as(
			select locate(',',jobname,1) id,substr(jobname,1,coalesce(NULLIF(locate(',',jobname,1)-1,-1),length(jobname)))jobname1,right(jobname,length(jobname)-coalesce(NULLIF(locate(',',jobname,1),0),length(jobname)))jobname from u --递归起点
			union all
			select id+locate(',',jobname,1) id,substr(jobname,1,coalesce(NULLIF(locate(',',jobname,1)-1,-1),length(jobname)))jobname1,right(jobname,length(jobname)-coalesce(NULLIF(locate(',',jobname,1),0),length(jobname)))jobname from t where jobname <> ''
			)select distinct v_UserID,v_datime,RID,1 as pnumber,1 as BNUMBER,'start' as previous,case when trim(t.jobname1)=b.jobname then b.jobname else b.SND||'.'||b.SCHEMA||'.'||b.INOFILE end as behind,1 as pathLength,b.ISVALID,case when trim(t.jobname1)=b.jobname then b.JOBTYPE else 'BlankJob' end as JOBTYPE --非jb,记为BlankJob
			from t
			inner join dbo.etl_jobino b on b.stype = stype_in
			where (upper(t.jobname1) = upper(b.jobname) or upper(t.jobname1) = upper(b.inofile));  --兼容输入jb和tab
			If(exists(select 1 from dbo.job_relation where JOBTYPE = 'BlankJob' and UserID = v_userID and datime = v_datime)) THEN
				set RID = RID + 1;
				set pathLength = pathLength + 1;
				--BlankJob的输出jb
				insert into dbo.job_relation(UserID,datime,RID,pnumber,previous,behind,pathLength,BNUMBER,ISVALID,JOBTYPE)
				select distinct v_UserID,v_datime,1 as RID,dense_rank()over(order by b.behind) as pnumber,b.behind as previous,a.JOBNAME as behind,2 as pathLength,dense_rank()over(partition by b.behind order by a.jobname) as BNUMBER,a.ISVALID,a.JOBTYPE
				from dbo.etl_jobino a
				inner join dbo.job_relation b on a.SND||'.'||a.SCHEMA||'.'||a.INOFILE = b.behind and b.UserID = v_userID and b.datime = v_datime
				where a.ino = 'in' and a.stype = stype_in;
			End If;
		ELSE
			insert into dbo.job_relation(UserID,datime,RID,pnumber,BNUMBER,previous,behind,pathLength,ISVALID,JOBTYPE) --插入没有父节点的节点
			select distinct v_UserID,v_datime,RID,1,1,'start',trim(jobname),1,ISVALID,JOBTYPE from dbo.etl_jobino
			where stype = stype_in --and jobname not like '%boruntime%.sh%'
			and jobname not in(
			select distinct trim(b.jobname) from dbo.etl_jobino a ,dbo.etl_jobino b --有父节点的节点
			where a.stype = stype_in and b.stype = stype_in and a.ino = 'out' and b.ino = 'in' and a.SND = b.SND
			and a.inofile = b.inofile and a.SCHEMA = b.SCHEMA and a.jobname <> b.jobname --and b.jobname not like '%boruntime%.sh%'
			);
		END IF;
	/* */
	while (endFlag<>(select count(1) from dbo.job_relation where UserID = v_userID and datime = v_datime)) do --记录数没变化则退出循环
	set RID = RID + 1;
	set pathLength = pathLength + 1;
	set endFlag=(select count(1) from dbo.job_relation where UserID = v_userID and datime = v_datime);  --记录数
		--插入该节点的子节点
		insert into dbo.job_relation(UserID,datime,RID,pnumber,previous,behind,pathLength,BNUMBER,ISVALID,JOBTYPE)
		select distinct v_UserID,v_datime,RID,dense_rank()over(order by c.rnumber) as pnumber,trim(a.jobname) as previous,trim(b.jobname) as behind,pathLength,dense_rank()over(partition by a.jobname order by b.jobname) as BNUMBER,a.ISVALID*b.ISVALID as ISVALID,b.JOBTYPE
		from dbo.etl_jobino a ,dbo.etl_jobino b,(select avg(a.pnumber*100+bnumber) rnumber,behind from dbo.job_relation a where a.UserID = v_userID and a.datime = v_datime and not exists(select PREVIOUS from dbo.job_relation b where a.behind = b.PREVIOUS and b.UserID = v_userID and b.datime = v_datime)group by behind)c
		where a.stype = stype_in and b.stype = stype_in and a.stype = b.stype and a.ino = 'out' and b.ino = 'in' and a.SND||'.'||a.SCHEMA||'.'||a.INOFILE = b.SND||'.'||b.SCHEMA||'.'||b.INOFILE
			--自己清空且写入的临时表表(out+truncate/out+%temp)，不作为外部输入
			--and not exists(select 1 from session.temp1 c where b.jobname = c.jobname and b.SND||'.'||b.SCHEMA||'.'||b.INOFILE = c.SND||'.'||c.SCHEMA||'.'||c.INOFILE)
			and not exists(select 1 from DBO.ETL_JOBINO_MQT c where c.stype = stype_in and b.jobname = c.jobname and b.SND||'.'||b.SCHEMA||'.'||b.INOFILE = c.SND||'.'||c.SCHEMA||'.'||c.INOFILE)
			--还未搜索子节点的节点
			and a.jobname = c.behind
			--and exists(select 1 from dbo.job_relation d where a.jobname = d.behind and not exists(select PREVIOUS from dbo.job_relation b where d.behind = b.PREVIOUS ))
		and a.jobname <> b.jobname --and b.jobname not like '%boruntime%.sh%'
		;
	 end while;
		insert into dbo.job_relation(UserID,datime,RID,pnumber,previous,behind,pathLength,BNUMBER,ISVALID,JOBTYPE)
        select v_UserID,v_datime,RID,BNUMBER,b.behind as previous,a.SND||'.'||a.SCHEMA||'.'||a.INOFILE as behind,b.pathLength+1,dense_rank()over(partition by a.jobname order by a.INOFILE),ISVALID,'TAB'
        from dbo.etl_jobino a ,(select behind,min(pathLength)pathLength,avg(BNUMBER)BNUMBER from dbo.job_relation where UserID = v_userID and datime = v_datime group by behind)b
        where a.stype = stype_in and ino = 'out'
		and a.jobname = b.behind
		and (a.INOFILE like '%DIMN%' or a.INOFILE like '%FACT%' or a.INOFILE like '%SUM' or a.INOFILE like '%PF');
	--commit;
	/*影响分析>*/
	ELSEIF(sourceOrTarget=0) THEN --1
	/*<血缘分析*/
	set RID = 99;
	set pathLength = 99;
		--插入叶子
		insert into dbo.job_relation(UserID,datime,RID,pnumber,previous,behind,pathLength,BNUMBER,ISVALID,JOBTYPE)
		--select RID,dense_rank()over(order by jobname),TRIM(jobname),TRIM(inofile),pathLength,1 from dbo.etl_jobino where ISVALID <> 0 and inofile = jobname and ino = 'out';
		with u(jobname)AS(values (jb_in)),
		t(id,jobname1,jobname) as(
		select locate(',',jobname,1) id,substr(jobname,1,coalesce(NULLIF(locate(',',jobname,1)-1,-1),length(jobname)))jobname1,right(jobname,length(jobname)-coalesce(NULLIF(locate(',',jobname,1),0),length(jobname)))jobname from u --递归起点
		union all
		select id+locate(',',jobname,1) id,substr(jobname,1,coalesce(NULLIF(locate(',',jobname,1)-1,-1),length(jobname)))jobname1,right(jobname,length(jobname)-coalesce(NULLIF(locate(',',jobname,1),0),length(jobname)))jobname from t where jobname <> ''
		)select distinct v_UserID,v_datime,RID,dense_rank()over(order by jobname)pnumber,TRIM(jobname)previous,TRIM(inofile)behind,pathLength,1 BNUMBER,ISVALID,'TAB' JOBTYPE
		from dbo.etl_jobino a
		where stype = stype_in and ino = 'out'--and inofile = jobname;
		and exists(select 1 from t where upper(a.inofile) = upper(t.jobname1) or upper(a.jobname) = upper(t.jobname1));
		--插入父节点
		/* */ while (endFlag<>(select count(1) from dbo.job_relation where UserID = v_userID and datime = v_datime)) do --记录数没变化则结束
		set RID = RID - 1;
		set pathLength = pathLength - 1;
		set endFlag=(select count(1) from dbo.job_relation where UserID = v_userID and datime = v_datime);  --记录数
		insert into dbo.job_relation(UserID,datime,RID,pnumber,previous,behind,pathLength,BNUMBER,ISVALID,JOBTYPE)
		select distinct v_UserID,v_datime,RID,dense_rank()over(order by a.jobname),trim(a.jobname),trim(b.jobname),pathLength,dense_rank()over(partition by a.jobname order by b.jobname),b.ISVALID,b.JOBTYPE
		from dbo.etl_jobino a ,dbo.etl_jobino b
		where a.stype = stype_in and b.stype = stype_in and a.ino = 'out' and b.ino = 'in' and a.SND = b.SND
		and a.inofile = b.inofile and a.SCHEMA = b.SCHEMA
			--自己清空且写入的临时表表(out+truncate/out+%temp)，不作为外部输入
		--and not exists(select 1 from session.temp1 c where b.jobname = c.jobname and b.inofile = c.inofile and b.SCHEMA = c.SCHEMA)
		and not exists(select 1 from DBO.ETL_JOBINO_MQT c where c.stype = stype_in and b.jobname = c.jobname and b.inofile = c.inofile and b.SCHEMA = c.SCHEMA)
			--还未搜索父节点的节点
		and b.jobname in(select PREVIOUS from dbo.job_relation a where a.UserID = v_userID and a.datime = v_datime and not exists(select PREVIOUS from dbo.job_relation b where a.PREVIOUS = b.behind and b.UserID = v_userID and b.datime = v_datime))
		--and not exists(select 1 from dbo.job_relation c where a.jobname = c.previous and b.jobname = c.behind)
		and a.jobname <> b.jobname --and b.jobname not like '%boruntime%.sh%'
		;
		/* */ end while;
		--没有有效(isvalid=1)父节点的，置其父为start
		insert into dbo.job_relation(UserID,datime,RID,pnumber,previous,behind,pathLength,BNUMBER,ISVALID,JOBTYPE)
		select distinct v_UserID,v_datime,1,dense_rank()over(order by a.PREVIOUS),'start',PREVIOUS,1,dense_rank()over(order by a.PREVIOUS)
		,(select max(ISVALID) from dbo.etl_jobino b where a.PREVIOUS = b.jobname)ISVALID
		,(select max(JOBTYPE) from dbo.etl_jobino b where a.PREVIOUS = b.jobname)JOBTYPE
		from dbo.job_relation a
		where a.UserID = v_userID and a.datime = v_datime
		and not exists(select 1 from dbo.job_relation b
		                 where b.UserID = v_userID and b.datime = v_datime
						 and a.PREVIOUS = b.behind
			             and exists(select 1 from dbo.etl_jobino c where b.PREVIOUS = c.jobname and c.isvalid = 1));
		update dbo.job_relation set PATHLENGTH = dense_rank()over(order by RID) where UserID = v_userID and datime = v_datime;  --更新搜索路径
	--commit;
	/*血缘分析>*/
	ELSE

	END IF;            --1
	
	merge into dbo.job_relation a using (
	select u.BEHIND,u.PATHLENGTH,row_number()over(partition by u.PATHLENGTH order by u.PNUMBER,u.BEHIND)rrnn
	from (
	select t.*,row_number()over(partition by BEHIND order by PATHLENGTH ,PNUMBER)rn from(
		select avg(PNUMBER*1.0)PNUMBER,BEHIND,PATHLENGTH from dbo.job_relation t where t.UserID = v_userID and t.datime = v_datime group by BEHIND,PATHLENGTH)t
	--where t.BEHIND = 'boruntime_agencyreport_d.sh'
	)u where u.rn = 1 --取最小路径长度的第一条
	)b on a.BEHIND = b.BEHIND and a.UserID = v_userID and a.datime = v_datime
	when matched then update set a.RNUMBER2 = b.RRNN
	else ignore;

	--commit;
	--写入坐标  y*2
	insert into dbo.job_location(UserID,datime,jobname,x,y,ISVALID,JOBTYPE)
	with t as(select * from ( --取每个有效BEHIND的最小路径长度(x)
	select a.*,row_number()over(partition by BEHIND order by ISVALID desc,PATHLENGTH asc)rn from dbo.job_relation a where a.UserID = v_userID and a.datime = v_datime
	)b where b.rn = 1)
	select v_UserID,v_datime,a.BEHIND,a.PATHLENGTH*300-200 x,a.RNUMBER2*b.yn/2 y,a.ISVALID,a.JOBTYPE
	from t a,
	--计算出相邻的(y)长度,注意每个x的(y)长度可能不同
	(select PATHLENGTH,(select max(RNUMBER2+1)from t)*50/max(RNUMBER2+1) yn from t group by PATHLENGTH)b
	where a.PATHLENGTH = b.PATHLENGTH
	order by x,y;
	commit;
	--OPEN C1; --打开游标接收返回集
  END P1!