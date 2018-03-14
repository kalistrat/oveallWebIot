-- --------------------------------------------------------
-- Хост:                         127.0.0.1
-- Версия сервера:               5.5.23 - MySQL Community Server (GPL)
-- ОС Сервера:                   Win32
-- HeidiSQL Версия:              9.3.0.4984
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Дамп структуры для таблица teljournal.device_status
CREATE TABLE IF NOT EXISTS `device_status` (
  `DEVICE_STATUS_ID` int(11) NOT NULL AUTO_INCREMENT,
  `STATUS_DATE` datetime DEFAULT NULL,
  `SOLD_DEVICE_ID` int(11) NOT NULL,
  `STATUS_ID` int(11) NOT NULL,
  PRIMARY KEY (`DEVICE_STATUS_ID`),
  KEY `FK_DEVICE_STATUS_sold_devices` (`SOLD_DEVICE_ID`),
  KEY `FK_device_status_status` (`STATUS_ID`),
  CONSTRAINT `FK_DEVICE_STATUS_sold_devices` FOREIGN KEY (`SOLD_DEVICE_ID`) REFERENCES `sold_devices` (`SOLD_DEVICE_ID`),
  CONSTRAINT `FK_device_status_status` FOREIGN KEY (`STATUS_ID`) REFERENCES `status` (`STATUS_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

-- Дамп данных таблицы teljournal.device_status: ~22 rows (приблизительно)
DELETE FROM `device_status`;
/*!40000 ALTER TABLE `device_status` DISABLE KEYS */;
INSERT INTO `device_status` (`DEVICE_STATUS_ID`, `STATUS_DATE`, `SOLD_DEVICE_ID`, `STATUS_ID`) VALUES
	(1, '2018-03-11 17:03:58', 1, 1),
	(2, '2018-03-13 11:45:59', 2, 1),
	(3, '2018-03-13 13:55:38', 3, 1),
	(5, '2018-03-13 14:44:11', 5, 1),
	(6, '2018-03-13 14:44:33', 6, 1),
	(7, '2018-03-13 14:44:36', 7, 1),
	(8, '2018-03-13 14:44:41', 8, 1),
	(9, '2018-03-13 14:44:47', 9, 1),
	(10, '2018-03-13 14:44:50', 10, 1),
	(11, '2018-03-13 14:44:52', 11, 1),
	(12, '2018-03-13 14:48:19', 12, 1),
	(13, '2018-03-13 14:48:24', 13, 1),
	(14, '2018-03-13 14:48:24', 14, 1),
	(15, '2018-03-13 14:48:25', 15, 1),
	(16, '2018-03-13 14:48:26', 16, 1),
	(17, '2018-03-13 14:48:27', 17, 1),
	(18, '2018-03-13 14:48:28', 18, 1),
	(19, '2018-03-13 15:14:16', 19, 1),
	(20, '2018-03-13 15:14:17', 20, 1),
	(21, '2018-03-13 15:14:18', 21, 1),
	(22, '2018-03-13 15:14:19', 22, 1),
	(23, '2018-03-13 21:42:01', 23, 1),
	(24, '2018-03-14 17:34:41', 1, 2);
/*!40000 ALTER TABLE `device_status` ENABLE KEYS */;


-- Дамп структуры для функция teljournal.faddNewUser
DELIMITER //
CREATE DEFINER=`kalistrat`@`localhost` FUNCTION `faddNewUser`(`eUserLogin` VARCHAR(50), `eUserPass` VARCHAR(150), `eUserMail` VARCHAR(150), `eUserPhone` VARCHAR(50)



) RETURNS varchar(250) CHARSET utf8
BEGIN
declare i_server_id int;
declare i_ws_url varchar(250);

select min(us.SERVER_ID) into i_server_id
from user_web_servers us
where us.COUNT_USERS in (
select min(uws.COUNT_USERS)
from user_web_servers uws
);

insert into tj_users(
USER_LOGIN
,USER_PASSWORD
,USER_MAIL
,USER_PHONE
,SERVER_ID
) values (
eUserLogin
,eUserPass
,eUserMail
,eUserPhone
,i_server_id
);

update user_web_servers
set COUNT_USERS = uws.COUNT_USERS + 1
where SERVER_ID = i_server_id;

select uws.WEB_SERVICE_URL into i_ws_url
from user_web_servers uws
where uws.SERVER_ID = i_server_id;

return i_ws_url;
END//
DELIMITER ;


-- Дамп структуры для функция teljournal.fGetUserPassSha
DELIMITER //
CREATE DEFINER=`kalistrat`@`localhost` FUNCTION `fGetUserPassSha`(`eUserLog` VARCHAR(50)) RETURNS varchar(150) CHARSET utf8
BEGIN
return(
select tu.USER_PASSWORD
from tj_users tu
where tu.USER_LOGIN = eUserLog
);
END//
DELIMITER ;


-- Дамп структуры для функция teljournal.fisExistsUserLogin
DELIMITER //
CREATE DEFINER=`kalistrat`@`localhost` FUNCTION `fisExistsUserLogin`(
	`eLogin` varchar(150)


) RETURNS int(11)
begin

return(
select case when count(*)>0 then 1 else 0 end
from tj_users u
where u.USER_LOGIN = eLogin
);

end//
DELIMITER ;


-- Дамп структуры для функция teljournal.fisExistsUserMail
DELIMITER //
CREATE DEFINER=`kalistrat`@`localhost` FUNCTION `fisExistsUserMail`(
	`eMail` varchar(150)


) RETURNS int(11)
begin

return(
select case when count(*)>0 then 1 else 0 end
from tj_users u
where u.USER_MAIL = eMail
);

end//
DELIMITER ;


-- Дамп структуры для функция teljournal.fisUIDExists
DELIMITER //
CREATE DEFINER=`kalistrat`@`localhost` FUNCTION `fisUIDExists`(`eUID` VARCHAR(50)) RETURNS int(11)
    COMMENT 'проверка существования UID'
BEGIN
return(
select count(*)
from sold_devices sd
where sd.UID = eUID
);
END//
DELIMITER ;


-- Дамп структуры для функция teljournal.getUserWebServerUrl
DELIMITER //
CREATE DEFINER=`kalistrat`@`localhost` FUNCTION `getUserWebServerUrl`(
	`eUserLog` VARCHAR(50)

) RETURNS varchar(150) CHARSET utf8
    COMMENT 'проверка логина'
BEGIN
declare iCntUsers int;
declare iUserWebServerUrl varchar(150);

select count(*) into iCntUsers
from tj_users u
join user_web_servers uws on uws.SERVER_ID=u.SERVER_ID
where u.USER_LOGIN = eUserLog;

if (iCntUsers = 1) then
	select uws.PERSONAL_WEB_URL into iUserWebServerUrl
	from tj_users u
	join user_web_servers uws on uws.SERVER_ID=u.SERVER_ID
	where u.USER_LOGIN = eUserLog;
else 
	set iUserWebServerUrl = '';
end if;

return iUserWebServerUrl;
END//
DELIMITER ;


-- Дамп структуры для процедура teljournal.pAddNewUID
DELIMITER //
CREATE DEFINER=`kalistrat`@`localhost` PROCEDURE `pAddNewUID`(IN `eUID` VARCHAR(50)


)
BEGIN
declare i_sold_device_id int;
declare i_device_status_id int;

insert into sold_devices(uid,DATE_FROM)
values(eUID,sysdate());

select LAST_INSERT_ID() into i_sold_device_id;

insert into device_status(
STATUS_DATE
,SOLD_DEVICE_ID
,STATUS_ID
) values(
sysdate()
,i_sold_device_id
,1
);
	
select LAST_INSERT_ID() into i_device_status_id;

update sold_devices
set DEVICE_STATUS_ID = i_device_status_id
,CURRENT_STATUS_CODE = 'OUTSIDE'
where SOLD_DEVICE_ID = i_sold_device_id;

END//
DELIMITER ;


-- Дамп структуры для таблица teljournal.sold_devices
CREATE TABLE IF NOT EXISTS `sold_devices` (
  `SOLD_DEVICE_ID` int(11) NOT NULL AUTO_INCREMENT,
  `UID` varchar(50) NOT NULL,
  `DATE_FROM` datetime DEFAULT NULL,
  `DEVICE_STATUS_ID` int(11) DEFAULT NULL,
  `CURRENT_STATUS_CODE` varchar(50) DEFAULT NULL,
  `USER_ID` int(11) DEFAULT NULL,
  `SERVICE_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`SOLD_DEVICE_ID`),
  UNIQUE KEY `UID` (`UID`),
  KEY `FK_sold_devices_device_status` (`DEVICE_STATUS_ID`),
  KEY `FK_sold_devices_tj_users` (`USER_ID`),
  KEY `FK_sold_devices_user_web_services` (`SERVICE_ID`),
  CONSTRAINT `FK_sold_devices_device_status` FOREIGN KEY (`DEVICE_STATUS_ID`) REFERENCES `device_status` (`DEVICE_STATUS_ID`),
  CONSTRAINT `FK_sold_devices_tj_users` FOREIGN KEY (`USER_ID`) REFERENCES `tj_users` (`USER_ID`),
  CONSTRAINT `FK_sold_devices_user_web_services` FOREIGN KEY (`SERVICE_ID`) REFERENCES `user_web_services` (`SERVICE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8 COMMENT='Проданные устройства';

-- Дамп данных таблицы teljournal.sold_devices: ~22 rows (приблизительно)
DELETE FROM `sold_devices`;
/*!40000 ALTER TABLE `sold_devices` DISABLE KEYS */;
INSERT INTO `sold_devices` (`SOLD_DEVICE_ID`, `UID`, `DATE_FROM`, `DEVICE_STATUS_ID`, `CURRENT_STATUS_CODE`, `USER_ID`, `SERVICE_ID`) VALUES
	(1, 'BRI-S23423BJB234', '2018-03-10 19:39:30', 24, 'AWAINTING', 7, NULL),
	(2, 'SEN-DF154LK55548', '2018-03-13 11:45:34', 2, 'OUTSIDE', NULL, NULL),
	(3, 'RET-QWERTYQWERTY', '2018-03-13 13:55:38', 3, 'OUTSIDE', NULL, NULL),
	(5, 'SEN-I80827SF2CE7', '2018-03-13 14:44:11', 5, 'OUTSIDE', NULL, NULL),
	(6, 'SEN-K4HUIIYW8RNG', '2018-03-13 14:44:33', 6, 'OUTSIDE', NULL, NULL),
	(7, 'SEN-ZSOR9JD88JIF', '2018-03-13 14:44:36', 7, 'OUTSIDE', NULL, NULL),
	(8, 'BRI-II497O5YNUFA', '2018-03-13 14:44:41', 8, 'OUTSIDE', NULL, NULL),
	(9, 'RET-41IC5IBP5F2U', '2018-03-13 14:44:47', 9, 'OUTSIDE', NULL, NULL),
	(10, 'BRI-XGSA4ZYQTDYQ', '2018-03-13 14:44:50', 10, 'OUTSIDE', NULL, NULL),
	(11, 'SEN-4NJLI60IJFIZ', '2018-03-13 14:44:52', 11, 'OUTSIDE', NULL, NULL),
	(12, 'SEN-AMJMGY0NVZ4C', '2018-03-13 14:48:19', 12, 'OUTSIDE', NULL, NULL),
	(13, 'SEN-YKVTYW95NO39', '2018-03-13 14:48:23', 13, 'OUTSIDE', NULL, NULL),
	(14, 'SEN-WE0S29LRGDF0', '2018-03-13 14:48:24', 14, 'OUTSIDE', NULL, NULL),
	(15, 'SEN-S6J625OK232F', '2018-03-13 14:48:25', 15, 'OUTSIDE', NULL, NULL),
	(16, 'SEN-9NIUT35XMBYQ', '2018-03-13 14:48:26', 16, 'OUTSIDE', NULL, NULL),
	(17, 'SEN-WQK9YDRIUQU9', '2018-03-13 14:48:27', 17, 'OUTSIDE', NULL, NULL),
	(18, 'SEN-ZQE5DF5AR6LD', '2018-03-13 14:48:28', 18, 'OUTSIDE', NULL, NULL),
	(19, 'SEN-F0YJVG7ZNYEC', '2018-03-13 15:14:16', 19, 'OUTSIDE', NULL, NULL),
	(20, 'SEN-TN6ZOETAM1OV', '2018-03-13 15:14:17', 20, 'OUTSIDE', NULL, NULL),
	(21, 'SEN-9TIV4KE7R1XW', '2018-03-13 15:14:18', 21, 'OUTSIDE', NULL, NULL),
	(22, 'SEN-0VR7E2V90Z19', '2018-03-13 15:14:19', 22, 'OUTSIDE', NULL, NULL),
	(23, 'SEN-V6L6LHNCNLJQ', '2018-03-13 21:42:01', 23, 'OUTSIDE', NULL, NULL);
/*!40000 ALTER TABLE `sold_devices` ENABLE KEYS */;


-- Дамп структуры для таблица teljournal.status
CREATE TABLE IF NOT EXISTS `status` (
  `STATUS_ID` int(11) NOT NULL AUTO_INCREMENT,
  `STATUS_CODE` varchar(50) NOT NULL,
  `STATUS_NAME` varchar(50) NOT NULL,
  PRIMARY KEY (`STATUS_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='Справочник статусов';

-- Дамп данных таблицы teljournal.status: ~3 rows (приблизительно)
DELETE FROM `status`;
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
INSERT INTO `status` (`STATUS_ID`, `STATUS_CODE`, `STATUS_NAME`) VALUES
	(1, 'OUTSIDE', 'Неподключено'),
	(2, 'AWAINTING', 'Ожидает подключения'),
	(3, 'CONNECTED', 'Подключено');
/*!40000 ALTER TABLE `status` ENABLE KEYS */;


-- Дамп структуры для таблица teljournal.tj_users
CREATE TABLE IF NOT EXISTS `tj_users` (
  `USER_ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_LOGIN` varchar(50) NOT NULL,
  `USER_PASSWORD` varchar(150) NOT NULL,
  `USER_MAIL` varchar(150) NOT NULL,
  `USER_PHONE` varchar(50) NOT NULL,
  `SERVER_ID` int(11) NOT NULL,
  PRIMARY KEY (`USER_ID`),
  UNIQUE KEY `USER_LOGIN` (`USER_LOGIN`),
  UNIQUE KEY `USER_MAIL` (`USER_MAIL`),
  KEY `FK_tj_users_user_web_servers` (`SERVER_ID`),
  CONSTRAINT `FK_tj_users_user_web_servers` FOREIGN KEY (`SERVER_ID`) REFERENCES `user_web_servers` (`SERVER_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='Общий справочник пользователей';

-- Дамп данных таблицы teljournal.tj_users: ~4 rows (приблизительно)
DELETE FROM `tj_users`;
/*!40000 ALTER TABLE `tj_users` DISABLE KEYS */;
INSERT INTO `tj_users` (`USER_ID`, `USER_LOGIN`, `USER_PASSWORD`, `USER_MAIL`, `USER_PHONE`, `SERVER_ID`) VALUES
	(1, 'k', '7', 'kalique@bk.ru', '89162664924', 1),
	(2, 'Oleg', '8', 'antipovoa@gmail.com', '89164646464', 1),
	(3, 'TestUser123', 'qwerty123', 'existing@mail.ru', '43534534534', 1),
	(7, 'kalistrat', 'bf2c2edb653709e2213f47eb8ec36b1c051f1eb41a3b727af60c73be9ff7b5a3', 'kauredinas@mail.ru', '753753', 1);
/*!40000 ALTER TABLE `tj_users` ENABLE KEYS */;


-- Дамп структуры для процедура teljournal.updateSoldDeviceStatus
DELIMITER //
CREATE DEFINER=`kalistrat`@`localhost` PROCEDURE `updateSoldDeviceStatus`(IN `eUID` VARCHAR(50), IN `eUserLog` VARCHAR(50))
    COMMENT 'меняет статус устройства'
BEGIN
declare i_sold_device_id int;
declare i_device_status_id int;
declare i_user_id int;

select sd.SOLD_DEVICE_ID into i_sold_device_id
from sold_devices sd
where sd.UID = eUID;

select u.USER_ID into i_user_id
from tj_users u
where u.USER_LOGIN = eUserLog;

insert into device_status(
STATUS_DATE
,SOLD_DEVICE_ID
,STATUS_ID
) values (
sysdate()
,i_sold_device_id
,2
);

select LAST_INSERT_ID() into i_device_status_id;

update sold_devices sd
set CURRENT_STATUS_CODE = 'AWAINTING'
,DEVICE_STATUS_ID = i_device_status_id
,USER_ID = i_user_id
where SOLD_DEVICE_ID = i_sold_device_id;

END//
DELIMITER ;


-- Дамп структуры для таблица teljournal.user_web_servers
CREATE TABLE IF NOT EXISTS `user_web_servers` (
  `SERVER_ID` int(11) NOT NULL AUTO_INCREMENT,
  `PERSONAL_WEB_URL` varchar(150) NOT NULL,
  `WEB_SERVICE_URL` varchar(150) NOT NULL,
  `COUNT_USERS` int(11) NOT NULL,
  PRIMARY KEY (`SERVER_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='Веб-сервера пользователей';

-- Дамп данных таблицы teljournal.user_web_servers: ~1 rows (приблизительно)
DELETE FROM `user_web_servers`;
/*!40000 ALTER TABLE `user_web_servers` DISABLE KEYS */;
INSERT INTO `user_web_servers` (`SERVER_ID`, `PERSONAL_WEB_URL`, `WEB_SERVICE_URL`, `COUNT_USERS`) VALUES
	(1, 'http://localhost:8777/personal', 'http://localhost:8777/userWs/Integration?wsdl', 4);
/*!40000 ALTER TABLE `user_web_servers` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
