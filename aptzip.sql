drop table aptzip.tb_apt;
drop table aptzip.tb_user;
drop table aptzip.tb_role;
drop table aptzip.tb_board;
drop table aptzip.persistent_logins;


select * from aptzip.tb_user;
select * from aptzip.tb_board;
select * from aptzip.tb_comment;
select * from aptzip.tb_board_comments;
select * from aptzip.tb_role;
select * from aptzip.tb_apt;
select * from aptzip.tb_notice;
select * from aptzip.persistent_logins;


CREATE TABLE tb_persistent_logins (
	username VARCHAR(100) not null, 
  series VARCHAR(64) primary key, 
  token VARCHAR(64) not null, 
  last_used timestamp not null
);
