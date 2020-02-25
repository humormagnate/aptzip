drop table aptzip.persistent_logins cascade;
drop table aptzip.tb_user_follow cascade;
drop table aptzip.tb_comment cascade;
drop table aptzip.tb_like cascade;
drop table aptzip.tb_board cascade;
drop table aptzip.tb_user cascade;
drop table aptzip.tb_notice cascade;
drop table aptzip.tb_apt cascade;
drop table aptzip.tb_role cascade;
drop table aptzip.tb_category cascade;

rollback;
commit;

delete from aptzip.tb_category where category_id = 7;

select * from aptzip.tb_user;
select * from aptzip.tb_confirmation_token;
select * from aptzip.tb_like;
select * from aptzip.tb_user_follow;
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

ALTER DATABASE aptzip CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;