#Run this script as a DB admin user (usually root)
#then create a new "localhost" login user:
# username = demouser
#password = demo
#and assign it the SELECT, DELETE and INSERT grants on the 'shortener_example' schema.


CREATE SCHEMA `shortener_example` ;

CREATE TABLE `shortener_example`.`URL_Cache` (
  `longForm` VARCHAR(255) NOT NULL,
  `shortForm` VARCHAR(45) NOT NULL,
  `url_id` INT NOT NULL,
  PRIMARY KEY (`url_id`, `longForm`),
  UNIQUE INDEX `longForm_UNIQUE` (`longForm` ASC),
  UNIQUE INDEX `shortForm_UNIQUE` (`shortForm` ASC));

