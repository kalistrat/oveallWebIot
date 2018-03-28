-- --------------------------------------------------------
-- Хост:                         127.0.0.1
-- Версия сервера:               5.5.23 - MySQL Community Server (GPL)
-- Операционная система:         Win64
-- HeidiSQL Версия:              9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=99 DEFAULT CHARSET=utf8;

-- Дамп данных таблицы teljournal.device_status: ~97 rows (приблизительно)
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
	(24, '2018-03-14 17:34:41', 1, 2),
	(25, '2018-03-15 13:35:06', 1, 2),
	(26, '2018-03-15 13:48:20', 5, 2),
	(27, '2018-03-17 01:03:54', 2, 2),
	(28, '2018-03-17 19:05:03', 23, 2),
	(29, '2018-03-17 19:06:41', 23, 3),
	(30, '2018-03-17 19:08:48', 8, 2),
	(31, '2018-03-17 19:09:04', 8, 3),
	(32, '2018-03-17 19:13:25', 8, 1),
	(33, '2018-03-17 19:13:48', 8, 2),
	(34, '2018-03-17 19:18:54', 10, 2),
	(35, '2018-03-17 19:25:11', 6, 2),
	(36, '2018-03-17 19:27:31', 6, 3),
	(37, '2018-03-17 19:29:07', 10, 3),
	(38, '2018-03-17 19:30:12', 8, 3),
	(39, '2018-03-17 20:09:04', 6, 1),
	(40, '2018-03-17 20:11:25', 10, 1),
	(41, '2018-03-17 20:12:14', 8, 1),
	(42, '2018-03-17 20:12:51', 23, 1),
	(43, '2018-03-17 20:22:20', 2, 1),
	(44, '2018-03-17 20:22:35', 5, 1),
	(45, '2018-03-17 20:22:44', 1, 1),
	(46, '2018-03-17 20:24:22', 1, 2),
	(47, '2018-03-17 20:25:52', 2, 2),
	(48, '2018-03-17 20:27:22', 2, 3),
	(49, '2018-03-17 20:32:05', 2, 1),
	(50, '2018-03-17 20:32:26', 2, 2),
	(51, '2018-03-17 20:33:09', 2, 3),
	(52, '2018-03-17 20:35:32', 2, 1),
	(53, '2018-03-17 20:36:01', 2, 2),
	(54, '2018-03-17 20:43:41', 2, 3),
	(55, '2018-03-17 20:49:42', 2, 1),
	(56, '2018-03-17 20:50:08', 2, 2),
	(57, '2018-03-17 20:50:29', 2, 3),
	(58, '2018-03-17 20:53:46', 2, 1),
	(59, '2018-03-17 20:54:01', 2, 2),
	(60, '2018-03-17 20:54:13', 2, 3),
	(61, '2018-03-17 20:55:30', 2, 1),
	(62, '2018-03-17 20:55:36', 2, 2),
	(63, '2018-03-17 21:03:38', 1, 3),
	(64, '2018-03-17 21:18:54', 2, 3),
	(65, '2018-03-17 21:20:02', 6, 2),
	(66, '2018-03-17 21:21:00', 6, 3),
	(67, '2018-03-17 21:22:44', 8, 2),
	(68, '2018-03-17 21:22:55', 8, 3),
	(69, '2018-03-17 21:24:13', 9, 2),
	(70, '2018-03-17 21:24:33', 9, 3),
	(71, '2018-03-17 21:26:11', 11, 2),
	(72, '2018-03-17 21:26:33', 11, 3),
	(73, '2018-03-17 21:55:38', 10, 2),
	(74, '2018-03-17 21:55:57', 10, 3),
	(75, '2018-03-17 21:56:23', 5, 2),
	(76, '2018-03-17 21:56:42', 5, 3),
	(77, '2018-03-19 18:23:41', 24, 1),
	(78, '2018-03-19 21:26:39', 25, 1),
	(79, '2018-03-19 21:26:45', 26, 1),
	(80, '2018-03-19 21:26:46', 27, 1),
	(81, '2018-03-19 21:26:49', 28, 1),
	(82, '2018-03-19 21:26:50', 29, 1),
	(83, '2018-03-19 21:26:55', 30, 1),
	(84, '2018-03-19 21:26:56', 31, 1),
	(85, '2018-03-19 21:28:26', 25, 2),
	(86, '2018-03-19 21:29:05', 28, 2),
	(87, '2018-03-19 21:30:06', 7, 2),
	(88, '2018-03-19 21:31:57', 25, 3),
	(89, '2018-03-19 21:32:53', 28, 3),
	(90, '2018-03-19 21:33:41', 7, 3),
	(91, '2018-03-19 21:34:35', 7, 1),
	(92, '2018-03-19 21:35:17', 28, 1),
	(93, '2018-03-19 21:36:05', 25, 1),
	(94, '2018-03-28 22:58:53', 32, 1),
	(95, '2018-03-28 22:59:14', 33, 1),
	(96, '2018-03-28 22:59:15', 34, 1),
	(97, '2018-03-28 22:59:16', 35, 1),
	(98, '2018-03-28 22:59:17', 36, 1);
/*!40000 ALTER TABLE `device_status` ENABLE KEYS */;

-- Дамп структуры для функция teljournal.faddNewUser
DELIMITER //
CREATE DEFINER=`kalistrat`@`localhost` FUNCTION `faddNewUser`(
	`eUserLogin` VARCHAR(50),
	`eUserPass` VARCHAR(150),
	`eUserMail` VARCHAR(150),
	`eUserPhone` VARCHAR(50)




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
set COUNT_USERS = COUNT_USERS + 1
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
BEGIN
return (
select count(*)
from sold_devices sd
where sd.UID = eUID
);
END//
DELIMITER ;

-- Дамп структуры для процедура teljournal.getDeviceArgs
DELIMITER //
CREATE DEFINER=`kalistrat`@`localhost` PROCEDURE `getDeviceArgs`(
	IN `eUID` VARCHAR(50),
	OUT `oDevStatus` VARCHAR(50),
	OUT `oWsURL` VARCHAR(250),
	OUT `oUserLogin` VARCHAR(50),
	OUT `oUserPass` VARCHAR(150)

)
BEGIN

select sd.CURRENT_STATUS_CODE
,uws.WEB_SERVICE_URL
,tu.USER_LOGIN
,tu.USER_PASSWORD
into oDevStatus
,oWsURL
,oUserLogin
,oUserPass
from sold_devices sd
left join tj_users tu on tu.USER_ID=sd.USER_ID
left join user_web_servers uws on uws.SERVER_ID=sd.SERVER_ID
where sd.UID = eUID;
 
END//
DELIMITER ;

-- Дамп структуры для функция teljournal.getUIDStatus
DELIMITER //
CREATE DEFINER=`kalistrat`@`localhost` FUNCTION `getUIDStatus`(
	`eUID` VARCHAR(50)
) RETURNS varchar(50) CHARSET utf8
    COMMENT 'проверка существования UID'
BEGIN
declare iCnt int;
declare i_status_code varchar(50);

select count(*) into iCnt
from sold_devices sd
where sd.UID = eUID;

if (iCnt = 1) then

	select sd.CURRENT_STATUS_CODE into i_status_code
	from sold_devices sd
	where sd.UID = eUID;

else 

	set i_status_code = '';

end if;

return i_status_code;

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

-- Дамп структуры для процедура teljournal.rollBackAddNewUser
DELIMITER //
CREATE DEFINER=`kalistrat`@`localhost` PROCEDURE `rollBackAddNewUser`(IN `eUserLog` VARCHAR(50))
BEGIN
delete from tj_users
where USER_LOGIN = eUserLog;
END//
DELIMITER ;

-- Дамп структуры для процедура teljournal.setUserEnvironment
DELIMITER //
CREATE DEFINER=`kalistrat`@`localhost` PROCEDURE `setUserEnvironment`()
BEGIN
update user_web_servers uws
set uws.PERSONAL_WEB_URL = 'http://snslog.ru/personal'
,uws.WEB_SERVICE_URL = 'http://snslog.ru/userWs/Integration?wsdl'
where uws.SERVER_ID = 1;
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
  `SERVER_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`SOLD_DEVICE_ID`),
  UNIQUE KEY `UID` (`UID`),
  KEY `FK_sold_devices_device_status` (`DEVICE_STATUS_ID`),
  KEY `FK_sold_devices_tj_users` (`USER_ID`),
  KEY `FK_sold_devices_user_web_servers` (`SERVER_ID`),
  CONSTRAINT `FK_sold_devices_device_status` FOREIGN KEY (`DEVICE_STATUS_ID`) REFERENCES `device_status` (`DEVICE_STATUS_ID`),
  CONSTRAINT `FK_sold_devices_tj_users` FOREIGN KEY (`USER_ID`) REFERENCES `tj_users` (`USER_ID`),
  CONSTRAINT `FK_sold_devices_user_web_servers` FOREIGN KEY (`SERVER_ID`) REFERENCES `user_web_servers` (`SERVER_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8 COMMENT='Проданные устройства';

-- Дамп данных таблицы teljournal.sold_devices: ~35 rows (приблизительно)
DELETE FROM `sold_devices`;
/*!40000 ALTER TABLE `sold_devices` DISABLE KEYS */;
INSERT INTO `sold_devices` (`SOLD_DEVICE_ID`, `UID`, `DATE_FROM`, `DEVICE_STATUS_ID`, `CURRENT_STATUS_CODE`, `USER_ID`, `SERVER_ID`) VALUES
	(1, 'BRI-S23423BJB234', '2018-03-10 19:39:30', 63, 'CONNECTED', 1, 1),
	(2, 'SEN-DF154LK55548', '2018-03-13 11:45:34', 64, 'CONNECTED', 1, 1),
	(3, 'RET-QWERTYQWERTY', '2018-03-13 13:55:38', 3, 'OUTSIDE', NULL, NULL),
	(5, 'SEN-I80827SF2CE7', '2018-03-13 14:44:11', 76, 'CONNECTED', 9, 1),
	(6, 'SEN-K4HUIIYW8RNG', '2018-03-13 14:44:33', 66, 'CONNECTED', 1, 1),
	(7, 'SEN-ZSOR9JD88JIF', '2018-03-13 14:44:36', 91, 'OUTSIDE', NULL, NULL),
	(8, 'BRI-II497O5YNUFA', '2018-03-13 14:44:41', 68, 'CONNECTED', 7, 1),
	(9, 'RET-41IC5IBP5F2U', '2018-03-13 14:44:47', 70, 'CONNECTED', 7, 1),
	(10, 'BRI-XGSA4ZYQTDYQ', '2018-03-13 14:44:50', 74, 'CONNECTED', 9, 1),
	(11, 'SEN-4NJLI60IJFIZ', '2018-03-13 14:44:52', 72, 'CONNECTED', 7, 1),
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
	(23, 'SEN-V6L6LHNCNLJQ', '2018-03-13 21:42:01', 42, 'OUTSIDE', NULL, NULL),
	(24, 'SEN-UVLD7YITSN1Z', '2018-03-19 18:23:41', 77, 'OUTSIDE', NULL, NULL),
	(25, 'BRI-DLOIN9335OHU', '2018-03-19 21:26:39', 93, 'OUTSIDE', NULL, NULL),
	(26, 'BRI-YFO20T2J6HHC', '2018-03-19 21:26:45', 79, 'OUTSIDE', NULL, NULL),
	(27, 'BRI-FMLD7DHZG2VG', '2018-03-19 21:26:46', 80, 'OUTSIDE', NULL, NULL),
	(28, 'RET-FCD05T177PCI', '2018-03-19 21:26:49', 92, 'OUTSIDE', NULL, NULL),
	(29, 'RET-M0RF8CMK8A28', '2018-03-19 21:26:50', 82, 'OUTSIDE', NULL, NULL),
	(30, 'SEN-CS3VEK79WZFD', '2018-03-19 21:26:54', 83, 'OUTSIDE', NULL, NULL),
	(31, 'SEN-1CBMEYOL5XZ4', '2018-03-19 21:26:56', 84, 'OUTSIDE', NULL, NULL),
	(32, 'MET-TM0TCLMP7PIO', '2018-03-28 22:58:53', 94, 'OUTSIDE', NULL, NULL),
	(33, 'MET-23JWEVCSXRB3', '2018-03-28 22:59:14', 95, 'OUTSIDE', NULL, NULL),
	(34, 'MET-6P9XGGNK8BT9', '2018-03-28 22:59:15', 96, 'OUTSIDE', NULL, NULL),
	(35, 'MET-0QNX2U54TZX5', '2018-03-28 22:59:16', 97, 'OUTSIDE', NULL, NULL),
	(36, 'MET-009710IFQ0BQ', '2018-03-28 22:59:17', 98, 'OUTSIDE', NULL, NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='Общий справочник пользователей';

-- Дамп данных таблицы teljournal.tj_users: ~5 rows (приблизительно)
DELETE FROM `tj_users`;
/*!40000 ALTER TABLE `tj_users` DISABLE KEYS */;
INSERT INTO `tj_users` (`USER_ID`, `USER_LOGIN`, `USER_PASSWORD`, `USER_MAIL`, `USER_PHONE`, `SERVER_ID`) VALUES
	(1, 'k', '7902699be42c8a8e46fbbb4501726517e86b22c56a189f7625a6da49081b2451', 'kalique@bk.ru', '89162664924', 1),
	(2, 'Oleg', '2c624232cdd221771294dfbb310aca000a0df6ac8b66b696d90ef06fdefb64a3', 'antipovoa@gmail.com', '89164646464', 1),
	(7, 'kalistrat', 'bf2c2edb653709e2213f47eb8ec36b1c051f1eb41a3b727af60c73be9ff7b5a3', 'kauredinas@mail.ru', '753753', 1),
	(9, 'semenovna', '4c4de41965012a94d6fcca5abbb792139e5f88f9149af99571be04d6b1afdcee', 'n7.semenov@gmail.com', '159951', 1),
	(11, 'akmakmakm', '6fee22b68be57b28b3d49a85ec2d979edfb1cbdd2c6a9d440dfe65f25c495fea', 'akminfo11@mail.ru', '159951159', 1);
/*!40000 ALTER TABLE `tj_users` ENABLE KEYS */;

-- Дамп структуры для процедура teljournal.updateSoldDeviceStatus
DELIMITER //
CREATE DEFINER=`kalistrat`@`localhost` PROCEDURE `updateSoldDeviceStatus`(
	IN `eUID` VARCHAR(50),
	IN `eUserLog` VARCHAR(50),
	IN `eStatusCode` VARCHAR(50)

)
    COMMENT 'меняет статус устройства'
BEGIN
declare i_sold_device_id int;
declare i_device_status_id int;
declare i_user_id int;
declare i_server_id int;
declare i_status_id int;

select sd.SOLD_DEVICE_ID into i_sold_device_id
from sold_devices sd
where sd.UID = eUID;

select u.USER_ID 
,u.SERVER_ID
into i_user_id
,i_server_id
from tj_users u
where u.USER_LOGIN = eUserLog;


select st.STATUS_ID into i_status_id
from status st
where st.STATUS_CODE = eStatusCode;

insert into device_status(
STATUS_DATE
,SOLD_DEVICE_ID
,STATUS_ID
) values (
sysdate()
,i_sold_device_id
,i_status_id
);

select LAST_INSERT_ID() into i_device_status_id;

if (eStatusCode = 'OUTSIDE') then

	update sold_devices
	set CURRENT_STATUS_CODE = eStatusCode
	,DEVICE_STATUS_ID = i_device_status_id
	,USER_ID = null
	,SERVER_ID = null
	where SOLD_DEVICE_ID = i_sold_device_id;
	
else 

	update sold_devices
	set CURRENT_STATUS_CODE = eStatusCode
	,DEVICE_STATUS_ID = i_device_status_id
	,USER_ID = i_user_id
	,SERVER_ID = i_server_id
	where SOLD_DEVICE_ID = i_sold_device_id;

end if;


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
	(1, 'http://localhost:8080/personal', 'http://localhost:8080/userWs/Integration?wsdl', 6);
/*!40000 ALTER TABLE `user_web_servers` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
