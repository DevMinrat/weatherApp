CREATE TABLE users
(
    id       int primary key auto_increment,
    login    varchar(50) not null unique,
    password varchar(64) not null unique
)