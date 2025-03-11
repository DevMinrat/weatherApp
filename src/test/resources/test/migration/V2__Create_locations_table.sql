CREATE TABLE locations
(
    id        int primary key auto_increment,
    name      varchar(100) not null,
    userId    int references users on delete cascade,
    latitude  decimal      not null,
    longitude decimal      not null
)