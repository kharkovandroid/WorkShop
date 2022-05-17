DROP DATABASE IF EXISTS workshop;

/*--------------------------------*/
CREATE DATABASE workshop DEFAULT CHARACTER SET utf8mb4;
/*--------------------------------*/
USE workshop;

/*--------------------------------*/
CREATE TABLE IF NOT EXISTS workshop.role 
(id INT NOT NULL AUTO_INCREMENT,
  
 role VARCHAR(30) NOT NULL,
  
 PRIMARY KEY (id),
  
 UNIQUE INDEX role_UNIQUE (role ASC))

 DEFAULT CHARACTER SET = utf8mb4;


/*--------------------------------*/
CREATE TABLE IF NOT EXISTS workshop.user 
(id INT NOT NULL AUTO_INCREMENT,
  
 login VARCHAR(30) NOT NULL,
  
 password VARCHAR(32) NOT NULL,
  
 name VARCHAR(30) NOT NULL,
  
 role_id INT NOT NULL,
  
 PRIMARY KEY (id),
  
 UNIQUE INDEX login_UNIQUE (login ASC),
  
 INDEX fk_user_role_idx (role_id ASC),
  

 CONSTRAINT fk_user_role
    
 FOREIGN KEY (role_id)
    
 REFERENCES workshop.role (id)
    
 ON DELETE CASCADE
    
 ON UPDATE CASCADE)

 DEFAULT CHARACTER SET = utf8mb4;


/*--------------------------------*/
CREATE TABLE IF NOT EXISTS workshop.account
(id INT NOT NULL AUTO_INCREMENT,
 user_id INT NOT NULL,
 manager_id INT NOT NULL,
 payment DECIMAL(5, 2) DEFAULT 0 CHECK (payment >= 0),
 datetime TIMESTAMP DEFAULT NOW(),
 PRIMARY KEY (id),
 INDEX fk_account_user_idx (user_id ASC),
 INDEX fk_account_manager_idx (manager_id ASC),

 CONSTRAINT fk_account_user_user
 FOREIGN KEY (user_id) 
 REFERENCES workshop.user(id) 
 ON DELETE CASCADE 
 ON UPDATE CASCADE,

 CONSTRAINT fk_account_user_manager
 FOREIGN KEY (manager_id) 
 REFERENCES workshop.user(id) 
 ON DELETE CASCADE 
 ON UPDATE CASCADE)
 DEFAULT CHARACTER SET = utf8mb4;
/*--------------------------------*/
CREATE TABLE IF NOT EXISTS workshop.order
(id INT NOT NULL AUTO_INCREMENT,
 foreman_id INT NULL,
 user_id INT NOT NULL,
 manager_id INT NULL,
 description VARCHAR(30),
 cost DECIMAL(5, 2) DEFAULT 0 CHECK (cost >= 0),
 comment VARCHAR(300) NULL,
 datetime DATETIME DEFAULT NOW(),
 PRIMARY KEY (id),
 INDEX fk_order_foreman_idx (foreman_id ASC),
 INDEX fk_order_user_idx (user_id ASC),
 INDEX fk_order_manager_idx (manager_id ASC),

 CONSTRAINT fk_order_user_foreman
 FOREIGN KEY (foreman_id) 
 REFERENCES workshop.user(id) 
 ON DELETE CASCADE 
 ON UPDATE CASCADE,

 CONSTRAINT fk_order_user_user
 FOREIGN KEY (user_id) 
 REFERENCES workshop.user(id) 
 ON DELETE CASCADE 
 ON UPDATE CASCADE, 

 CONSTRAINT fk_order_user_manager
 FOREIGN KEY (manager_id) 
 REFERENCES workshop.user(id) 
 ON DELETE CASCADE 
 ON UPDATE CASCADE)
 DEFAULT CHARACTER SET = utf8mb4;
/*--------------------------------*/
CREATE TABLE IF NOT EXISTS workshop.status 
(id INT NOT NULL AUTO_INCREMENT,
  
 type VARCHAR(30) NOT NULL,
  
 PRIMARY KEY (id))

 DEFAULT CHARACTER SET = utf8mb4;


/*--------------------------------*/
CREATE TABLE IF NOT EXISTS workshop.order_status
(order_id INT NOT NULL,
 status_id INT NOT NULL,
 manager_foreman_id INT NOT NULL,
 datetime TIMESTAMP DEFAULT NOW(),
 PRIMARY KEY (order_id, status_id),
 INDEX fk_order_status_manager_foreman_idx (manager_foreman_id ASC),

 CONSTRAINT fk_order_status_order_id
 FOREIGN KEY (order_id) 
 REFERENCES workshop.order(id) 
 ON DELETE CASCADE 
 ON UPDATE CASCADE,

 CONSTRAINT fk_order_status_status_id
 FOREIGN KEY (status_id) 
 REFERENCES workshop.status(id) 
 ON DELETE CASCADE 
 ON UPDATE CASCADE,

 CONSTRAINT fk_order_status_user_manager_foreman
 FOREIGN KEY (manager_foreman_id) 
 REFERENCES workshop.user(id) 
 ON DELETE CASCADE 
 ON UPDATE CASCADE)
 DEFAULT CHARACTER SET = utf8mb4;
/*--------------------------------*/
CREATE TABLE IF NOT EXISTS workshop.list_status
(role_id INT NOT NULL,
 status_from_id INT NOT NULL,
 status_to_id INT NOT NULL,
 PRIMARY KEY (status_from_id, status_to_id),
 INDEX fk_list_status_role_idx (role_id ASC),

 CONSTRAINT fk_list_status_role_id
 FOREIGN KEY (role_id) 
 REFERENCES workshop.role(id) 
 ON DELETE CASCADE 
 ON UPDATE CASCADE,

 CONSTRAINT fk_list_status_status_from_id
 FOREIGN KEY (status_from_id) 
 REFERENCES workshop.status(id) 
 ON DELETE CASCADE 
 ON UPDATE CASCADE,

 CONSTRAINT fk_list_status_status_to_id
 FOREIGN KEY (status_to_id) 
 REFERENCES workshop.status(id) 
 ON DELETE CASCADE 
 ON UPDATE CASCADE)
 DEFAULT CHARACTER SET = utf8mb4;
/*--------------------------------*/
/*--------------------------------*/
/*--------------------------------*/
insert into role (role) values ('менеджер');/*1*/
insert into role (role) values ('пользователь');/*2*/
insert into role (role) values ('мастер');

/*3*/
/*--------------------------------*/
insert into user (login, password, name, role_id) 
values
('mehlogin', '15927d9ab6ea93229b4f22a561664ec1', 'Мехайлов Алексей Петрович', 1);/*password - meh*/
insert into user (login, password, name, role_id) 
values
('mevlogin', '3671768d0593382cd6cf23552e08db2d', 'Мевеев Виктор Львович', 1);/*password - mev*/
insert into user (login, password, name, role_id) 
values
('pollogin', '627a1f8f3e1f8a2a0cbb9aedc33ade8c', 'Польских Игорь Викторович', 2);/*password - pol*/
insert into user (login, password, name, role_id) 
values
('potlogin', 'ecbfde1394f2dfe02d50081d8a35dba6', 'Потаев Олег Анатольевич', 2);/*password - pot*/
insert into user (login, password, name, role_id) 
values
('mallogin', '749dfe7c0cd3b291ec96d0bb8924cb46', 'Малых Виктор Сергеевич', 3);/*password - mal*/
insert into user (login, password, name, role_id) 
values
('matlogin', '4a258d930b7d3409982d727ddbb4ba88', 'Матюшенко Сидор Петрович', 3);/*password - mat*/
/*--------------------------------*/

SELECT SLEEP(1);


insert into account (user_id, manager_id, payment) values (3, 1, 10);

SELECT SLEEP(1);

insert into account (user_id, manager_id, payment) values (3, 1, 20);


SELECT SLEEP(1);

insert into account (user_id, manager_id, payment) values (3, 1, 30);


SELECT SLEEP(1);

insert into account (user_id, manager_id, payment) values (3, 1, 40);

SELECT SLEEP(1);

insert into account (user_id, manager_id, payment) values (3, 1, 50);

SELECT SLEEP(1);

insert into account (user_id, manager_id, payment) values (3, 1, 60);

SELECT SLEEP(1);

insert into account (user_id, manager_id, payment) values (3, 1, 70);

SELECT SLEEP(1);

insert into account (user_id, manager_id, payment) values (3, 1, 80);

SELECT SLEEP(1);

insert into account (user_id, manager_id, payment) values (3, 1, 90);

SELECT SLEEP(1);

insert into account (user_id, manager_id, payment) values (3, 1, 100);

SELECT SLEEP(1);

insert into account (user_id, manager_id, payment) values (3, 1, 110);




insert into account (user_id, manager_id, payment) values (4, 2, 15);

SELECT SLEEP(1);

insert into account (user_id, manager_id, payment) values (4, 2, 25);

SELECT SLEEP(1);

insert into account (user_id, manager_id, payment) values (4, 2, 35);

SELECT SLEEP(1);

insert into account (user_id, manager_id, payment) values (4, 2, 45);

SELECT SLEEP(1);

insert into account (user_id, manager_id, payment) values (4, 2, 55);

SELECT SLEEP(1);

insert into account (user_id, manager_id, payment) values (4, 2, 65);

SELECT SLEEP(1);

insert into account (user_id, manager_id, payment) values (4, 2, 75);

SELECT SLEEP(1);

insert into account (user_id, manager_id, payment) values (4, 2, 85);

SELECT SLEEP(1);

insert into account (user_id, manager_id, payment) values (4, 2, 95);

SELECT SLEEP(1);

insert into account (user_id, manager_id, payment) values (4, 2, 105);

SELECT SLEEP(1);

insert into account (user_id, manager_id, payment) values (4, 2, 115);

SELECT SLEEP(1);

/*--------------------------------*/

insert into status (type) values ('в работе');/*1*/
insert into status (type) values ('выполнено');/*2*/


insert into status (type) values ('ожидает на оплату');/*3*/

insert into status (type) values ('оплачено');/*4*/

insert into status (type) values ('отменено');/*5*/

insert into status (type) values ('final');/*6*/
/*--------------------------------*/

insert into list_status (role_id, status_from_id, status_to_id)
values (3, 1, 2);
insert into list_status (role_id, status_from_id, status_to_id)
values (1, 1, 5);
insert into list_status (role_id, status_from_id, status_to_id)
values (1, 2, 3);
insert into list_status (role_id, status_from_id, status_to_id)
values (1, 2, 5);
insert into list_status (role_id, status_from_id, status_to_id)
values (1, 3, 4);
insert into list_status (role_id, status_from_id, status_to_id)
values (1, 3, 5);
insert into list_status (role_id, status_from_id, status_to_id)
values (1, 5, 6);
insert into list_status (role_id, status_from_id, status_to_id)
values (1, 4, 6);
/*--------------------------------*/

insert into workshop.order (foreman_id, user_id, manager_id, 
description, cost, comment)
values (NULL, 3, NULL, 'Ремонт ручки', 0, NULL);/*id = 1*/
SELECT SLEEP(1);

insert into workshop.order (foreman_id, user_id, manager_id, 
description, cost, comment)
values (5, 3, 1, 'Ремонт карандаша', 25, NULL);/*id = 2*/
SELECT SLEEP(1);

insert into workshop.order (foreman_id, user_id, manager_id, 
description, cost, comment)
values (5, 3, 1, 'Ремонт зонта', 30, NULL);/*id = 3*/
SELECT SLEEP(1);

insert into workshop.order (foreman_id, user_id, manager_id, 
description, cost, comment)
values (5, 3, 1, 'Ремонт окна', 35, NULL);/*id = 4*/
SELECT SLEEP(1);

insert into workshop.order (foreman_id, user_id, manager_id, 
description, cost, comment)
values (6, 3, 1, 'Ремонт стола', 40, NULL);/*id = 5*/
SELECT SLEEP(1);

insert into workshop.order (foreman_id, user_id, manager_id, 
description, cost, comment)
values (6, 4, 2, 'Ремонт лампочки', 45, 'Лампочка отремонтирована');/*id = 6*/
SELECT SLEEP(1);

insert into workshop.order (foreman_id, user_id, manager_id, 
description, cost, comment)
values (6, 4, 2, 'Ремонт мыши', 50, NULL);/*id = 7*/
SELECT SLEEP(1);

insert into workshop.order (foreman_id, user_id, manager_id, 
description, cost, comment)
values (6, 4, 2, 'Ремонт колонки', 55, NULL);/*id = 8*/
SELECT SLEEP(1);

/*--------------------------------*/

insert into order_status (order_id, status_id, manager_foreman_id)
values (3, 1, 5);/*order_id = 3*/
SELECT SLEEP(1);

insert into order_status (order_id, status_id, manager_foreman_id)
values (4, 1, 5);/*order_id = 4*/
SELECT SLEEP(1);
insert into order_status (order_id, status_id, manager_foreman_id)
values (4, 5, 1);/*order_id = 4*/
SELECT SLEEP(1);

insert into order_status (order_id, status_id, manager_foreman_id)
values (5, 1, 6);/*order_id = 5*/
SELECT SLEEP(1);
insert into order_status (order_id, status_id, manager_foreman_id)
values (5, 2, 6);/*order_id = 5*/
SELECT SLEEP(1);

insert into order_status (order_id, status_id, manager_foreman_id)
values (6, 1, 6);/*order_id = 6*/
SELECT SLEEP(1);
insert into order_status (order_id, status_id, manager_foreman_id)
values (6, 2, 6);/*order_id = 6*/
SELECT SLEEP(1);
insert into order_status (order_id, status_id, manager_foreman_id)
values (6, 3, 2);/*order_id = 6*/
SELECT SLEEP(1);

insert into order_status (order_id, status_id, manager_foreman_id)
values (7, 1, 6);/*order_id = 7*/
SELECT SLEEP(1);
insert into order_status (order_id, status_id, manager_foreman_id)
values (7, 2, 6);/*order_id = 7*/
SELECT SLEEP(1);
insert into order_status (order_id, status_id, manager_foreman_id)
values (7, 3, 2);/*order_id = 7*/
SELECT SLEEP(1);
insert into order_status (order_id, status_id, manager_foreman_id)
values (7, 5, 2);/*order_id = 7*/
SELECT SLEEP(1);

insert into order_status (order_id, status_id, manager_foreman_id)
values (8, 1, 6);/*order_id = 8*/
SELECT SLEEP(1);
insert into order_status (order_id, status_id, manager_foreman_id)
values (8, 2, 6);/*order_id = 8*/
SELECT SLEEP(1);
insert into order_status (order_id, status_id, manager_foreman_id)
values (8, 3, 2);/*order_id = 8*/
SELECT SLEEP(1);
insert into order_status (order_id, status_id, manager_foreman_id)
values (8, 4, 2);/*order_id = 8*/
