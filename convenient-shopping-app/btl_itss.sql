

create database btl ; 
use btl ; 

create table user_account  (
id int auto_increment PRIMARY KEY,
username VARCHAR(50) NOT NULL,
email VARCHAR(255),
time_email_verified TIMESTAMP,
password VARCHAR(8) NOT NULL,
status INT NOT NULL,
avatar VARCHAR(255) ,
fullname NVARCHAR(255),
gender VARCHAR(255),
address VARCHAR(255),
descriptions text,
create_at TIMESTAMP default CURRENT_TIMESTAMP,
update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
); 



create table food(
id int auto_increment PRIMARY KEY,
name NVARCHAR(255) NOT NULL,
poster_link VARCHAR(2555),
descriptions text,
status INT NOT NULL,
user_id INT,
buy_at TIMESTAMP,
address_buy VARCHAR(255),
measure  VARCHAR(10),
quantity INT,
create_at TIMESTAMP default CURRENT_TIMESTAMP,
update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

create table dish(
id int auto_increment PRIMARY KEY,
name NVARCHAR(255) NOT NULL,
cook_date TIMESTAMP,
descriptions TEXT,
status INT NOT NULL,
expired TIMESTAMP,
create_at TIMESTAMP default CURRENT_TIMESTAMP,
update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);



create table recipe(
id int auto_increment PRIMARY KEY,
dish_id INT NOT NULL,
descriptions TEXT NOT NULL,
create_at TIMESTAMP default CURRENT_TIMESTAMP,
update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
foreign key (dish_id) references dish(id)
); 

create table recipe_food(
recipe_id INT NOT NULL, 
food_id INT NOT NULL, 
foreign key (recipe_id) references recipe(id) , 
foreign key(food_id) references food(id)
);



create table group_list (
id int auto_increment PRIMARY KEY, 
group_leader INT NOT NULL , 
name VARCHAR(255) NOT NULL, 
create_at TIMESTAMP default CURRENT_TIMESTAMP , 
update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP , 
foreign key (group_leader) references user_account(id)
);

create table group_member(
group_id int not null , 
user_id int not null, 
foreign key (group_id) references user_account(id), 
foreign key (user_id) references group_list(id)
); 

create table group_food( 
group_id INT not null, 
food_id INT not null, 
foreign key	(group_id) references group_list(id), 
foreign key (food_id) references food(id)
); 

create table favorite( 
user_id int not null, 
recipe_id int not null, 
foreign key (user_id) references user_account(id), 
foreign key (recipe_id) references recipe(id)
); 


show tables;


