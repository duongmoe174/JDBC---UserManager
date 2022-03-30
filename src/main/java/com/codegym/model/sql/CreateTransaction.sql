use quanlyuser;
create table Permission (
    id int auto_increment primary key not null ,
    name varchar(50) not null
);

create table User_Permission (
    permission_id int,
    user_id int
);

insert into Permission(id, name) values(1, 'add');
insert into Permission(id, name) values(2, 'edit');
insert into Permission(id, name) values(3, 'delete');
insert into Permission(id, name) values(4, 'view');