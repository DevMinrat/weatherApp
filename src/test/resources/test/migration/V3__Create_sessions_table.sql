CREATE TABLE sessions
(
    id        int primary key auto_increment,
    userId    int references users on delete cascade,
    expiresAt timestamp
)