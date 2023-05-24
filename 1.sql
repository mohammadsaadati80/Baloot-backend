-- SHOW GRANTS FOR 'root'@'localhost';-- 
-- CREATE USER 'root'@'localhost' IDENTIFIED BY '12345678';
-- GRANT ALL PRIVILEGES ON baloot_db.* TO 'your_username'@'localhost';
-- FLUSH PRIVILEGES;
ALTER USER 'root'@'localhost' IDENTIFIED BY '12345678';
GRANT ALL PRIVILEGES ON baloot_db.* TO 'root'@'localhost';
FLUSH PRIVILEGES;
CREATE DATABASE baloot_db;