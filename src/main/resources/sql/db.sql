create table customer(
    id varchar(255) primary key,
    name varchar(255),
    phone int(255),
    address varchar(255)
);
create table item(
    itemID varchar(25) primary key ,
    itemName varchar(200),
    itemPrice decimal(10,2),
    itemQTY int(20)
)