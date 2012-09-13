create database vertx;

-- switch to vertx

create user 'vertx'@'localhost' identified by 'vertx';

grant usage on *.* to vertx@localhost identified by 'vertx';

grant all privileges on vertx.* to vertx@localhost;

