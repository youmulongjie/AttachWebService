-- 
create table ATTACH_UPLOAD
(
  ID            NUMBER not null primary key,
  ORIGINAL_NAME VARCHAR2(200),
  ALIAS_NAME    VARCHAR2(1000),
  YEAR          NUMBER,
  MONTH         NUMBER,
  DAY           NUMBER,
  PATH          VARCHAR2(200),
  STATUS        VARCHAR2(2) default 1,
  UPLOAD_DATE   DATE default sysdate not null
);
-- Add comments to the table 
comment on table ATTACH_UPLOAD
  is '附件上传表（测试）';
-- Add comments to the columns 
comment on column ATTACH_UPLOAD.ID
  is '主键ID';
comment on column ATTACH_UPLOAD.ORIGINAL_NAME
  is '原名称';
comment on column ATTACH_UPLOAD.ALIAS_NAME
  is '别名（id+原名称）';
comment on column ATTACH_UPLOAD.YEAR
  is '年';
comment on column ATTACH_UPLOAD.MONTH
  is '月';
comment on column ATTACH_UPLOAD.DAY
  is '日';
comment on column ATTACH_UPLOAD.PATH
  is '路径';
comment on column ATTACH_UPLOAD.STATUS
  is '状态（1：可用；0：不可用）默认为1';
comment on column ATTACH_UPLOAD.UPLOAD_DATE
  is '上传时间';

create sequence ATTACH_UPLOAD_SEQ;
