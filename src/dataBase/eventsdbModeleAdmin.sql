-- phpMyAdmin SQL Dump
-- version 4.1.4
-- http://www.phpmyadmin.net
--
-- Client :  127.0.0.1
-- Généré le :  Mer 25 Janvier 2017 à 14:24
-- Version du serveur :  5.6.15-log
-- Version de PHP :  5.4.24

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données :  `eventsdb`
--

-- --------------------------------------------------------

--
-- Structure de la table `author`
--

CREATE TABLE IF NOT EXISTS `author` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=12 ;

--
-- Contenu de la table `author`
--

INSERT INTO `author` (`id`, `username`, `password`) VALUES
(1, 'admin', '$2a$12$DcHyZoLYDKQlyZaUaatAkOz7RZEEz2pztzI9V3JqlgLOQ0/EWxF4y');

-- --------------------------------------------------------

--
-- Structure de la table `events`
--

CREATE TABLE IF NOT EXISTS `events` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `description` mediumtext NOT NULL,
  `fileName` varchar(2000) NOT NULL,
  `idAuthor` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=74 ;

--
-- Contenu de la table `events`
--

INSERT INTO `events` (`id`, `title`, `description`, `fileName`, `idAuthor`) VALUES
(1, 'Evenement Modele', 'ceci est un evenement modele pour tester les statistique sur le jeu de donnees', '', 1);

-- --------------------------------------------------------

--
-- Structure de la table `subscribers`
--

CREATE TABLE IF NOT EXISTS `subscribers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idEvent` int(11) NOT NULL,
  `idUser` int(11) NOT NULL,
  `hashCode` varchar(255) DEFAULT NULL,
  `isPresent` int(1) DEFAULT '0',
  `mailStatus` int(11) NOT NULL DEFAULT '0',
  `sendingDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=51 ;

--
-- Contenu de la table `subscribers`
--

INSERT INTO `subscribers` (`id`, `idEvent`, `idUser`, `hashCode`, `isPresent`, `mailStatus`, `sendingDate`) VALUES
(40, 1, 22, '6636D4B9742B791C3616E40D998056A224CB73D8', 1, 1, '2017-01-18 15:19:23'),
(41, 1, 23, 'BD05839E8C73F67D55F011E6060602E584467389', 2, 1, '2017-01-18 15:19:25'),
(42, 1, 24, '17472AAA813FB7C3CA0AD8D666F2BF9884E7B359', 2, 1, '2017-01-18 15:19:27'),
(43, 1, 25, 'A155E1634BC336FFD56F261BAE8803FCCAF9F499', 0, 1, '2017-01-18 15:19:29'),
(44, 1, 26, 'AC27D56E13859EFBAD62D9BDE687096780540F1A', 0, 0, '2017-01-18 15:19:31'),
(49, 1, 28, 'BDFBD01C172C0C7E07123BD5E890BA1BEEC574E7', 0, 0, '2017-01-19 16:55:00'),
(50, 1, 29, 'B3C3F1CF3549534F638E83131D67FCCE1A17EE9C', 0, 0, '2017-01-20 08:59:37');

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=31 ;

--
-- Contenu de la table `users`
--

INSERT INTO `users` (`id`, `email`) VALUES
(29, 'erge@erg.fr'),
(28, 'erf@erf.fr'),
(26, 'test@test.com'),
(25, 'pascalcunin@hotmail.com'),
(24, 'pascaljeremym2i@hotmail.com'),
(23, 'pascaljeremym2i@gmail.com'),
(22, 'jeremypansier@gmail.com');

-- --------------------------------------------------------

--
-- Structure de la table `visites`
--

CREATE TABLE IF NOT EXISTS `visites` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(8192) NOT NULL,
  `IPAddress` varchar(255) DEFAULT NULL,
  `visitDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=306 ;

--
-- Contenu de la table `visites`
--

INSERT INTO `visites` (`id`, `url`, `IPAddress`, `visitDate`) VALUES
(305, 'http://localhost:8080/http-jsp-servlet/VisitStatServlet', '127.0.0.1', '2017-01-25 13:24:16'),
(304, 'http://localhost:8080/http-jsp-servlet/EventCreationServlet', '127.0.0.1', '2017-01-25 13:24:08'),
(303, 'http://localhost:8080/http-jsp-servlet/EventStatServlet', '127.0.0.1', '2017-01-25 13:24:06'),
(302, 'http://localhost:8080/http-jsp-servlet/images/icone_interrogation.png', '127.0.0.1', '2017-01-25 13:24:04'),
(301, 'http://localhost:8080/http-jsp-servlet/images/icon_delete.png', '127.0.0.1', '2017-01-25 13:24:04'),
(300, 'http://localhost:8080/http-jsp-servlet/FilesServlet/', '127.0.0.1', '2017-01-25 13:24:04'),
(299, 'http://localhost:8080/http-jsp-servlet/images/icone_valider.png', '127.0.0.1', '2017-01-25 13:24:04'),
(298, 'http://localhost:8080/http-jsp-servlet/EventLinkServlet', '127.0.0.1', '2017-01-25 13:24:04'),
(297, 'http://localhost:8080/http-jsp-servlet/EventsListServlet', '127.0.0.1', '2017-01-25 13:24:03'),
(296, 'http://localhost:8080/http-jsp-servlet/LoginServlet', '127.0.0.1', '2017-01-25 13:24:02'),
(295, 'http://localhost:8080/http-jsp-servlet/css/eventCreation.css', '127.0.0.1', '2017-01-25 13:23:54'),
(294, 'http://localhost:8080/http-jsp-servlet/LoginServlet', '127.0.0.1', '2017-01-25 13:23:54');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
