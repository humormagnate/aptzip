select * from apt.tb_user;
select * from apt.tb_board;
select * from apt.tb_role;

alter table apt.`tb_user` drop `role`;

drop table apt.tb_user;
drop table apt.tb_role;

insert into tb_role values('USER');
insert into tb_role values('ADMIN');