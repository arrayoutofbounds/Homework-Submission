create table Answer (_id bigint not null, body varchar(255) not null, HOMEWORK_ID bigint not null, USER_ID bigint not null, primary key (_id))
create table Homework (_id bigint not null, duedate date, question varchar(255) not null, title varchar(255) not null, primary key (_id))
create table Student (Type varchar(255), _id bigint not null, primary key (_id))
create table Teacher (Type varchar(255), _id bigint not null, primary key (_id))
create table User (_id bigint not null, firstName varchar(255) not null, lastName varchar(255) not null, primary key (_id))
alter table Answer add constraint FK_h49j7dph8ppdu04rcvftn44jy foreign key (HOMEWORK_ID) references Homework
alter table Answer add constraint FK_adpk2ruhg1j0p2jffnegwwrs2 foreign key (USER_ID) references User
alter table Student add constraint FK_a5et0v6lvsu91ie9jftm5lokk foreign key (_id) references User
alter table Teacher add constraint FK_e8u65687lcl772x9qkl3jjhcg foreign key (_id) references User
create sequence hibernate_sequence start with 1 increment by 1
