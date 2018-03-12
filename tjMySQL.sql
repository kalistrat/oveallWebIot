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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- Дамп данных таблицы teljournal.device_status: ~1 rows (приблизительно)
DELETE FROM `device_status`;
/*!40000 ALTER TABLE `device_status` DISABLE KEYS */;
INSERT INTO `device_status` (`DEVICE_STATUS_ID`, `STATUS_DATE`, `SOLD_DEVICE_ID`, `STATUS_ID`) VALUES
	(1, '2018-03-11 17:03:58', 1, 1);
/*!40000 ALTER TABLE `device_status` ENABLE KEYS */;

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
CREATE DEFINER=`kalistrat`@`localhost` PROCEDURE `pAddNewUID`(
	IN `eUID` VARCHAR(50)


)
BEGIN

insert into sold_devices(uid,DATE_FROM)
values(eUID,sysdate());

END//
DELIMITER ;

-- Дамп структуры для таблица teljournal.sold_devices
CREATE TABLE IF NOT EXISTS `sold_devices` (
  `SOLD_DEVICE_ID` int(11) NOT NULL AUTO_INCREMENT,
  `UID` varchar(50) NOT NULL,
  `DATE_FROM` datetime DEFAULT NULL,
  `DEVICE_STATUS_ID` int(11) NOT NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='Проданные устройства';

-- Дамп данных таблицы teljournal.sold_devices: ~1 rows (приблизительно)
DELETE FROM `sold_devices`;
/*!40000 ALTER TABLE `sold_devices` DISABLE KEYS */;
INSERT INTO `sold_devices` (`SOLD_DEVICE_ID`, `UID`, `DATE_FROM`, `DEVICE_STATUS_ID`, `CURRENT_STATUS_CODE`, `USER_ID`, `SERVICE_ID`) VALUES
	(1, 'BRI-S23423BJB234', '2018-03-10 19:39:30', 1, 'OUTSIDE', NULL, NULL);
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
  `USER_MAIL` varchar(50) NOT NULL,
  `USER_PHONE` varchar(50) NOT NULL,
  `SERVER_ID` int(11) NOT NULL,
  PRIMARY KEY (`USER_ID`),
  KEY `FK_tj_users_user_web_servers` (`SERVER_ID`),
  CONSTRAINT `FK_tj_users_user_web_servers` FOREIGN KEY (`SERVER_ID`) REFERENCES `user_web_servers` (`SERVER_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='Общий справочник пользователей';

-- Дамп данных таблицы teljournal.tj_users: ~2 rows (приблизительно)
DELETE FROM `tj_users`;
/*!40000 ALTER TABLE `tj_users` DISABLE KEYS */;
INSERT INTO `tj_users` (`USER_ID`, `USER_LOGIN`, `USER_PASSWORD`, `USER_MAIL`, `USER_PHONE`, `SERVER_ID`) VALUES
	(1, 'k', '7', 'kalique@bk.ru', '89162664924', 1),
	(2, 'Oleg', '8', 'antipovoa@gmail.com', '89164646464', 1),
	(3, 'TestUser123', 'qwerty123', 'existing@mail.ru', '43534534534', 1);
/*!40000 ALTER TABLE `tj_users` ENABLE KEYS */;

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
	(1, 'http://localhost:8080/personal', 'http://localhost:8080/userWebService?wsdl', 2);
/*!40000 ALTER TABLE `user_web_servers` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
