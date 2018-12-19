-- phpMyAdmin SQL Dump
-- version 4.7.9
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: 28-Nov-2018 às 00:01
-- Versão do servidor: 5.7.21
-- PHP Version: 5.6.35

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `pd`
--

-- --------------------------------------------------------

--
-- Estrutura da tabela `utilizador`
--

DROP TABLE IF EXISTS `utilizador`;
CREATE TABLE IF NOT EXISTS `utilizador` (
  `utilizador_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `estado` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `portaTCP` int(11) NOT NULL,
  `portaUDP` int(11) NOT NULL,
  `enderecoIP` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`utilizador_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Extraindo dados da tabela `utilizador`
--

INSERT INTO `utilizador` (`utilizador_id`, `username`, `password`, `estado`, `portaTCP`, `portaUDP`, `enderecoIP`) VALUES
(2, 'Tony', '123', 'Indefinido', 0, 0, '0');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
