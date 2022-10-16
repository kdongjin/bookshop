drop database if exists bookstoreDB;
create database bookstoreDB;
use bookstoreDB;

drop table if exists publisherTBL;
CREATE TABLE IF NOT EXISTS `publisherTBL` (
  `no` int NOT NULL auto_increment,
  `name` CHAR(10) NOT NULL,
  `location` CHAR(20) NOT NULL,
  `phone` CHAR(12) NOT NULL,
  CONSTRAINT pk_publisherTBL_no PRIMARY KEY (`no`),
  CONSTRAINT unique_publisherTBL_name unique (`name`)
  ); 	
insert into publisherTBL values(1, '길벗', '서울 마포구', '02-332-0931');
insert into publisherTBL values(2, '한빛미디어', '서울 서대문구', '02-325-0384');
insert into publisherTBL values(3, '위키북스', '경기 파주시', '031-955-3658');

drop table if exists booksTBL;
CREATE TABLE IF NOT EXISTS `booksTBL` (
  `isbn` CHAR(20) NOT NULL,
  `title` CHAR(30) NOT NULL,
  `author` CHAR(10) NOT NULL,
  `publisher` CHAR(10) NOT NULL,
  `publishedDate` DATE NOT NULL,
  `price` INT NOT NULL,
  `stock` INT NOT NULL DEFAULT 0,
  CONSTRAINT pk_booksTBL_isbn PRIMARY KEY (`isbn`),
  CONSTRAINT fk_booksTBL_publisher FOREIGN KEY (`publisher`) REFERENCES `publisherTBL` (`name`)
  );
insert into booksTBL values('123-45-67890-12-3', '모두의 깃&깃허브', '강민철', '길벗',20220816, 20000, 5);
insert into booksTBL values('123-45-67890-12-0', '컴퓨터 구조', '강민철', '한빛미디어',20220716, 28000, 4);
insert into booksTBL values('123-45-67890-12-1', '파이썬기반금융', '이브스', '한빛미디어',20220930, 42000, 0);
insert into booksTBL values('123-45-67890-12-2', '이것이자바다', '신용권', '한빛미디어',20220905, 36000, 7);
insert into booksTBL values('123-45-67890-12-4', 'MySQL성능', '실비아', '위키북스',20220922, 28000, 1);

CREATE TABLE IF NOT EXISTS `deleteBooksTBL` (
  `isbn` CHAR(20) NOT NULL,
  `title` CHAR(30) NOT NULL,
  `author` CHAR(10) NOT NULL,
  `publisher` CHAR(10) NOT NULL,
  `publishedDate` DATE NOT NULL,
  `price` INT NOT NULL,
  `stock` INT NOT NULL DEFAULT 0,
  deleteDate datetime
);

-- index 
CREATE INDEX idx_booksTBL_title ON booksTBL (title);
CREATE INDEX idx_booksTBL_stock ON booksTBL (stock);
SHOW INDEX FROM booksTBL;

-- procedure
  DROP PROCEDURE IF EXISTS  proc_salesUpdate;
  DELIMITER //
  CREATE PROCEDURE proc_salesUpdate(
	in in_isbn CHAR(20),
    in in_sales int
    )
    BEGIN
		DECLARE new_stock int default 0;
        DECLARE old_stock int default 0;
        SELECT stock INTO old_stock
			FROM bookstbl
            WHERE isbn = in_isbn;
        SET new_stock = old_stock - in_sales;
		UPDATE bookstbl SET stock = new_stock WHERE isbn = in_isbn;
	END//
  DELIMITER ;
  
    -- trigger
  describe bookstbl;
  DROP trigger IF EXISTS trigger_deleteBookstbl;
  delimiter !!
  CREATE trigger trigger_deleteBookstbl
		after delete
		on bookstbl
		for each row
 	begin
 		INSERT INTO deleteBookstbl VALUES(OLD.ISBN, OLD.title, OLD.author, OLD.publisher, OLD.publishedDate, OLD.price, OLD.stock, now());
     end !!
    delimiter ;
  select * from deleteBookstbl;