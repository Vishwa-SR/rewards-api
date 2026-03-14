drop database if exists rewards_db;

create database rewards_db;

use rewards_db;

drop table if exists customer;
drop table if exists customer_transaction;

create table customer(
    customer_id int auto_increment primary key,
    customer_name varchar(100) not null
);

create table customer_transaction(
    transaction_id int auto_increment primary key,
    customer_id int,
    amount decimal(10,2),
    transaction_date date,
    foreign key(customer_id) references customer(customer_id)
);

insert into customer(customer_name) values
('Vishwa'),
('chandru'),
('arun'),
('karthik'),
('rahul');

insert into customer_transaction(customer_id,amount,transaction_date) values

(1,120.50,'2025-01-02'),
(2,75.20,'2025-01-04'),
(3,210.00,'2025-01-06'),
(4,180.75,'2025-01-09'),
(5,95.30,'2025-01-11'),
(1,240.60,'2025-01-14'),
(2,88.40,'2025-01-16'),
(3,132.25,'2025-01-19'),
(4,165.90,'2025-01-23'),
(5,220.10,'2025-01-27'),

(1,150.00,'2025-02-01'),
(2,99.80,'2025-02-03'),
(3,178.60,'2025-02-06'),
(4,134.75,'2025-02-09'),
(5,210.25,'2025-02-11'),
(1,80.40,'2025-02-14'),
(2,165.30,'2025-02-17'),
(3,140.00,'2025-02-19'),
(4,300.50,'2025-02-23'),
(5,190.90,'2025-02-26'),

(1,205.60,'2026-01-03'),
(2,118.40,'2026-01-05'),
(3,160.20,'2026-01-07'),
(4,95.75,'2026-01-10'),
(5,175.90,'2026-01-12'),
(1,142.30,'2026-01-15'),
(2,225.10,'2026-01-18'),
(3,189.50,'2026-01-22'),
(4,130.00,'2026-01-25'),
(5,240.75,'2026-01-29'),

(1,178.25,'2026-02-02'),
(2,160.40,'2026-02-04'),
(3,210.90,'2026-02-06'),
(4,98.20,'2026-02-09'),
(5,145.75,'2026-02-12'),
(1,220.60,'2026-02-15'),
(2,135.10,'2026-02-18'),
(3,175.45,'2026-02-21'),
(4,260.80,'2026-02-24'),
(5,199.99,'2026-02-27');

select * from customer;
select * from customer_transaction;