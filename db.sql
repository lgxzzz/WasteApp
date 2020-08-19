create table User
(
   USER_ID varchar(20) primary key,
   USER_NAME varchar(50),
   USER_PASSWORD varchar(20),
   USER_EMAIL varchar(20),
   USER_ROLE varchar(50)
);
create table Waste
(
   WASTE_ID varchar(20) primary key,
   WASTE_NAME varchar(50),
   WASTE_TYPE varchar(20),
   WASTE_DES varchar(20),
   USER_ID varchar(20),
   WASTE_BARCODE varchar(20),
   WASTE_SCORE varchar(20),
   CREAT_TIME BIGINT
);
insert into User values('9787302164289','amon','123','123@qq.com',"Administrator");
insert into User values('9787121060953','bob','456','456@361.com',"User");

