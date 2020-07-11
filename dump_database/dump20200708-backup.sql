-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: meeting-agenda
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
-- Table structure for table `invitation`
--

DROP TABLE IF EXISTS `invitation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invitation` (
  `id_meeting` int NOT NULL,
  `id_user` int NOT NULL,
  PRIMARY KEY (`id_meeting`,`id_user`),
  KEY `utente_idx` (`id_user`),
  CONSTRAINT `meeting` FOREIGN KEY (`id_meeting`) REFERENCES `meetings` (`id_meeting`) ON DELETE CASCADE,
  CONSTRAINT `user` FOREIGN KEY (`id_user`) REFERENCES `users` (`id_user`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invitation`
--

LOCK TABLES `invitation` WRITE;
/*!40000 ALTER TABLE `invitation` DISABLE KEYS */;
INSERT INTO `invitation` VALUES (2,3),(3,3),(5,3),(6,3),(9,3),(11,3),(13,3),(16,3),(18,3),(20,3),(1,4),(4,4),(5,4),(7,4),(9,4),(11,4),(14,4),(16,4),(18,4),(1,5),(3,5),(5,5),(7,5),(9,5),(12,5),(14,5),(16,5),(19,5),(1,6),(2,6),(5,6),(7,6),(10,6),(12,6),(14,6),(17,6),(19,6),(1,7),(2,7),(3,7),(4,7),(5,7),(8,7),(10,7),(12,7),(15,7),(17,7),(19,7),(4,8),(6,8),(8,8),(10,8),(13,8),(15,8),(17,8),(20,8),(1,9),(2,9),(3,9),(6,9),(8,9),(11,9),(13,9),(15,9),(18,9),(20,9);
/*!40000 ALTER TABLE `invitation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `meetings`
--

DROP TABLE IF EXISTS `meetings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meetings` (
  `id_meeting` int NOT NULL AUTO_INCREMENT,
  `title` varchar(256) NOT NULL,
  `date_time` datetime NOT NULL,
  `duration` int NOT NULL,
  `max_participants` int NOT NULL,
  `id_creator` int NOT NULL,
  PRIMARY KEY (`id_meeting`),
  UNIQUE KEY `id_meetings_UNIQUE` (`id_meeting`),
  KEY `id_creatore_idx` (`id_creator`),
  CONSTRAINT `creator` FOREIGN KEY (`id_creator`) REFERENCES `users` (`id_user`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meetings`
--

LOCK TABLES `meetings` WRITE;
/*!40000 ALTER TABLE `meetings` DISABLE KEYS */;
INSERT INTO `meetings` VALUES (1,'Festa via citterio','2020-06-27 09:00:00',90,5,3),(2,'Meccanica','2020-08-23 07:54:23',24,10,4),(3,'Party in Liguria','2020-08-01 21:30:00',180,132,6),(4,'Progettazione Santorini','2020-07-09 16:30:21',30,5,6),(5,'Test Meeting','2020-07-08 16:30:21',60,10,9),(6,'Random','2020-07-10 00:00:21',60,4,3),(7,'Lampada','2020-07-11 00:00:21',23,5,3),(8,'Festa in spiaggia','2020-07-12 00:00:21',23,5,4),(9,'Stadio SanSiro','2020-07-13 00:00:21',90,800000,4),(10,'Geronimo Stilton Show','2020-07-14 00:00:21',65,5,5),(11,'Benedetta Parodi Masterchef','2020-07-15 00:00:21',40,5,5),(12,'Amazon Locker','2020-07-17 00:00:21',20,5,6),(13,'Functional Training','2020-07-16 00:00:21',30,5,7),(14,'Corsetta post trauma','2020-07-22 00:00:21',23,5,7),(15,'Birra Moretti','2020-07-16 00:00:23',34,5,7),(16,'Esercizio Fisica Quantistica','2020-07-06 20:00:21',45,5,8),(17,'Pingball al pc','2020-07-08 00:23:21',23,5,8),(18,'Riso Scotti Gerry','2020-05-30 00:00:21',34,5,8),(19,'Guardiani della galassia','2020-06-28 00:00:21',56,5,9),(20,'Io sono gROOT','2020-07-22 00:00:21',23,5,9);
/*!40000 ALTER TABLE `meetings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id_user` int NOT NULL AUTO_INCREMENT,
  `email` varchar(256) NOT NULL,
  `password` varchar(256) NOT NULL,
  `displayed_name` varchar(256) NOT NULL,
  PRIMARY KEY (`id_user`),
  UNIQUE KEY `id_user_UNIQUE` (`id_user`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (3,'mainetti@polimit.it','mainetti','Lorenzo Mainetti'),(4,'alberto@polimi.it','alberto','Alberto Latino'),(5,'tommaso@gmail.com','tommaso','Tommaso Pomodoro'),(6,'gabriele@gmail.com','gabriele','Gabriele Checksum'),(7,'alessandro@gmail.com','alessandro','Alessandro Lisi'),(8,'matteo@poli.it','matteo','Matteo Mattew'),(9,'root@root.com','root','Root User');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-07-08 22:30:22
