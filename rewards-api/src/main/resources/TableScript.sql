drop database if exists rewards_db;

create database rewards_db;

use rewards_db;

drop table if exists customer;
drop table if exists customer_transaction;

create table customer(
    customer_id int auto_increment primary key,
    customer_name varchar(100) not null,
    email varchar(100)
);

create table customer_transaction(
    transaction_id int auto_increment primary key,
    customer_id int,
    amount decimal(10,2),
    transaction_date date,
    foreign key(customer_id) references customer(customer_id)
);

insert into customer(customer_name,email) values
('Vishwa','vishwa123@gmail.com'),
('chandru','chandru123@gmail.com'),
('arun','arun123@gmail.com'),
('karthik','karthik123@gmail.com'),
('rahul','rahul123@gmail.com');

insert into customer_transaction(customer_id,amount,transaction_date) values
(1,222.40,'2026-01-10'),
(1,154.20,'2026-02-10'),
(1,123.25,'2026-02-15'),
(1,76.90,'2026-02-16'),
(1,210.75,'2026-03-01'),
(1,95.50,'2026-03-05'),

(2,100.34,'2026-01-10'),
(2,144.34,'2026-02-10'),
(2,86.50,'2026-02-14'),
(2,199.99,'2026-02-20'),
(2,250.10,'2026-03-02'),

(3,200.55,'2026-01-10'),
(3,222.75,'2026-02-10'),
(3,178.30,'2026-02-18'),
(3,89.60,'2026-03-01'),

(4,120.00,'2026-01-12'),
(4,98.45,'2026-02-11'),
(4,305.20,'2026-02-25'),
(4,67.80,'2026-03-03'),

(5,140.90,'2026-01-15'),
(5,160.00,'2026-02-12'),
(5,75.25,'2026-02-28'),
(5,220.40,'2026-03-04');

select * from customer;
select * from customer_transaction;