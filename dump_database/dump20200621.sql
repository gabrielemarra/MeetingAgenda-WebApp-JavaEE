CREATE DATABASE  IF NOT EXISTS `gestione_riunioni` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `gestione_riunioni`;
-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: gestione_riunioni
-- ------------------------------------------------------
-- Server version	8.0.19

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `invito`
--

DROP TABLE IF EXISTS `invito`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invito` (
  `id_riunione` int NOT NULL,
  `id_utente` int NOT NULL,
  PRIMARY KEY (`id_riunione`,`id_utente`),
  KEY `utente_idx` (`id_utente`),
  CONSTRAINT `riunione` FOREIGN KEY (`id_riunione`) REFERENCES `riunioni` (`id_riunione`),
  CONSTRAINT `utente` FOREIGN KEY (`id_utente`) REFERENCES `utenti` (`id_utente`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invito`
--

LOCK TABLES `invito` WRITE;
/*!40000 ALTER TABLE `invito` DISABLE KEYS */;
INSERT INTO `invito` VALUES (2,1),(1,2),(2,3);
/*!40000 ALTER TABLE `invito` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `riunioni`
--

DROP TABLE IF EXISTS `riunioni`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `riunioni` (
  `id_riunione` int NOT NULL AUTO_INCREMENT,
  `titolo` varchar(256) NOT NULL,
  `data_ora` datetime NOT NULL,
  `durata` int NOT NULL,
  `max_partecipanti` int NOT NULL,
  `id_creatore` int NOT NULL,
  PRIMARY KEY (`id_riunione`),
  UNIQUE KEY `id_riunioni_UNIQUE` (`id_riunione`),
  KEY `id_creatore_idx` (`id_creatore`),
  CONSTRAINT `creatore` FOREIGN KEY (`id_creatore`) REFERENCES `utenti` (`id_utente`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `riunioni`
--

LOCK TABLES `riunioni` WRITE;
/*!40000 ALTER TABLE `riunioni` DISABLE KEYS */;
INSERT INTO `riunioni` VALUES (1,'Festa via citteri','2020-06-27 09:00:00',90,5,3),(2,'Meccanica','2020-08-23 07:54:23',24,10,4);
/*!40000 ALTER TABLE `riunioni` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `utenti`
--

DROP TABLE IF EXISTS `utenti`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `utenti` (
  `id_utente` int NOT NULL AUTO_INCREMENT,
  `email` varchar(256) NOT NULL,
  `password` varchar(256) NOT NULL,
  `nome_visualizzato` varchar(256) NOT NULL,
  PRIMARY KEY (`id_utente`),
  UNIQUE KEY `ID_utente_UNIQUE` (`id_utente`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utenti`
--

LOCK TABLES `utenti` WRITE;
/*!40000 ALTER TABLE `utenti` DISABLE KEYS */;
INSERT INTO `utenti` VALUES (1,'pippo@gmail.com','pluto','Pippo'),(2,'pluto@outlook.it','pippo','Pluto'),(3,'mainetti@polimit.it','mainetti','Lorenzo Mainetti'),(4,'alberto@polimi.it','alberto','Alberto Latino');
/*!40000 ALTER TABLE `utenti` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-06-21 16:46:51
