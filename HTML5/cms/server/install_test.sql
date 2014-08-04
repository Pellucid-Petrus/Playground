CREATE DATABASE testdb;
CREATE USER 'testuser'@'localhost' IDENTIFIED BY 'testpass';
USE testdb;
GRANT ALL ON testdb.* TO 'testuser'@'localhost';
