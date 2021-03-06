/*
 The MIT License (MIT)

 Copyright (c) 2014 Patrick Wilson

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */

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

