CREATE SCHEMA IF NOT EXISTS `company`;

CREATE TABLE IF NOT EXISTS `company`.`employees` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL,
  `age` INT NOT NULL DEFAULT 0,
  `position` VARCHAR(255) NOT NULL,
  `salary` DECIMAL(16,2) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`));

INSERT INTO `employees` (name, age, position, salary)
VALUES ("Bob", 37, "Manager", 2000),
        ("Tom", 21, "Seller", 1000),
        ("Alice", 18, "Seller", 1000),
        ("Susan", 20, "Seller", 1000);
