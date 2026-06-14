CREATE DATABASE ems_db;
USE ems_db;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE employees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    department VARCHAR(50),
    salary DOUBLE
);

INSERT INTO users (username, password, role)
VALUES ('admin', 'admin123', 'Admin');

INSERT INTO users (username, password, role)
VALUES ('emp1', 'emp123', 'Employee');

INSERT INTO employees (name, email, department, salary)
VALUES
('Alice Smith', 'alice@example.com', 'IT', 75000),
('Bob Jones', 'bob@example.com', 'HR', 60000),
('Charlie Brown', 'charlie@example.com', 'Finance', 80000);
