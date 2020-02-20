drop table aptzip.tb_apt;
drop table aptzip.tb_user;
drop table aptzip.tb_role;
drop table aptzip.tb_board;
drop table aptzip.tb_category;
drop table aptzip.persistent_logins;

rollback;
commit;

delete from aptzip.tb_category where category_id = 7;

select * from aptzip.tb_user;
select * from aptzip.tb_user_follow;
insert into aptzip.tb_user_follow (from_user_id, to_user_id, create_date) values (2, 1, now());
insert into aptzip.tb_user_follow (from_user_id, to_user_id, create_date) values (1, 2, now());
select * from aptzip.tb_board;
select * from aptzip.tb_category;
select * from aptzip.tb_comment;
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
ALTER TABLE tb_user ADD UNIQUE (email);
