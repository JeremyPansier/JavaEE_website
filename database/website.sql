DROP TABLE IF EXISTS `Event`;
CREATE TABLE `Event` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `description` mediumtext NOT NULL,
  `filename` varchar(2000) NOT NULL,
  `authorId` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
INSERT INTO `Event` VALUES (1,'Évènement Modèle','Ceci est un évènement modèle pour tester les statistique sur le jeu de données','Javengers.jpg',1);

DROP TABLE IF EXISTS `Picture`;
CREATE TABLE `Picture` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `description` mediumtext NOT NULL,
  `filename` varchar(2000) NOT NULL,
  `authorId` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `Profile`;
CREATE TABLE `Profile` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `desription` mediumtext NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `User`;
CREATE TABLE `User` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
INSERT INTO `User` (`id`, `email`) VALUES
(1, 'notread@test.com'),
(2, 'accept@test.com'),
(3, 'decline@test.com');

DROP TABLE IF EXISTS `Author`;
CREATE TABLE `Author` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
INSERT INTO `Author` VALUES (1,'admin','$2a$12$DcHyZoLYDKQlyZaUaatAkOz7RZEEz2pztzI9V3JqlgLOQ0/EWxF4y');

DROP TABLE IF EXISTS `Guest`;
CREATE TABLE `Guest` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `eventId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `hash` varchar(255) DEFAULT NULL,
  `status` int(1) DEFAULT '0',
  `emailstatus` int(11) NOT NULL DEFAULT '0',
  `emaildate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
INSERT INTO `Guest` (`id`, `eventId`, `userId`, `hash`, `status`, `emailstatus`, `emaildate`) VALUES
(1, 1, 1, '6636D4B9742B791C3616E40D998056A224CB73D8', 0, 0, '2017-01-18 15:19:23'),
(2, 1, 2, '6636D4B9742B791C3616E40D998056A224CB73D8', 1, 1, '2017-01-18 15:19:23'),
(3, 1, 3, '6636D4B9742B791C3616E40D998056A224CB73D8', 2, 1, '2017-01-18 15:19:23');

DROP TABLE IF EXISTS `Visit`;
CREATE TABLE `Visit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(2000) NOT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
