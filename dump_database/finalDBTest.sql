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
  CONSTRAINT `meeting` FOREIGN KEY (`id_meeting`) REFERENCES `meetings` (`id_meeting`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user` FOREIGN KEY (`id_user`) REFERENCES `users` (`id_user`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invitation`
--

LOCK TABLES `invitation` WRITE;
/*!40000 ALTER TABLE `invitation` DISABLE KEYS */;
INSERT INTO `invitation` VALUES (11,2),(14,2),(1,3),(11,3),(14,3),(11,6),(14,6),(1,7),(6,7),(11,7),(13,7),(14,7),(1,8),(5,8),(6,8),(9,8),(11,8),(12,8),(13,8),(1,9),(5,9),(9,9),(11,9),(12,9),(13,9),(1,10),(5,10),(8,10),(9,10),(10,10),(1,11),(9,11),(13,11),(14,11),(9,12),(10,12),(1,13),(9,13),(11,13),(13,13),(1,14),(9,14),(13,14),(1,15),(4,15),(8,15),(8,17),(12,17),(1,18),(7,18),(11,18),(4,21),(6,21),(7,21),(8,21),(9,21),(10,21),(12,21),(14,21),(1,22),(4,22),(6,22),(7,22),(8,22),(14,22),(4,23),(10,23),(11,23),(12,23),(13,23);
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
  `title` varchar(60) NOT NULL,
  `date_time` datetime NOT NULL,
  `duration` int NOT NULL,
  `max_participants` int NOT NULL,
  `id_creator` int NOT NULL,
  PRIMARY KEY (`id_meeting`),
  UNIQUE KEY `id_meetings_UNIQUE` (`id_meeting`) /*!80000 INVISIBLE */,
  UNIQUE KEY `unique_set_title_date` (`title`,`date_time`,`duration`,`max_participants`,`id_creator`),
  KEY `id_creatore_idx` (`id_creator`) /*!80000 INVISIBLE */,
  CONSTRAINT `creator` FOREIGN KEY (`id_creator`) REFERENCES `users` (`id_user`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meetings`
--

LOCK TABLES `meetings` WRITE;
/*!40000 ALTER TABLE `meetings` DISABLE KEYS */;
INSERT INTO `meetings` VALUES (1,'Accountability C45B','2020-09-24 20:30:00',30,15,21),(13,'Azure Advisor Meeting','2020-09-04 16:00:00',45,9,22),(2,'Board of Directors','2020-08-05 10:00:00',60,4,21),(10,'End of July Meeting','2020-07-31 09:30:00',30,12,22),(5,'guests','2020-12-20 10:34:00',60,4,21),(12,'Meeting with AWS Advisor','2020-09-02 10:30:00',60,8,22),(7,'Old Meeting Not Visible 1','2019-06-04 05:34:00',123,12,23),(14,'Project Endline','2020-07-30 23:59:00',60,14,9),(6,'R00t Meeting','2020-08-21 14:30:00',128,35,23),(4,'Resource Planning 01-8-c534k3','2020-10-20 06:45:00',60,45,18),(3,'Resource Planning 01-8-c65k3','2020-08-01 06:35:00',134,5,18),(9,'Rocket Launch - Starship','2020-07-23 12:37:00',120,32,23),(8,'Server maintenance - August 2020','2020-08-07 21:30:00',60,6,23),(11,'Server Upgrade','2020-08-25 12:30:00',60,14,22);
/*!40000 ALTER TABLE `meetings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `temp_meetings`
--

DROP TABLE IF EXISTS `temp_meetings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `temp_meetings` (
  `title` varchar(60) NOT NULL,
  `date_time` datetime NOT NULL,
  `duration` int NOT NULL,
  `max_participants` int NOT NULL,
  `id_creator` int NOT NULL,
  `attempts` int NOT NULL,
  PRIMARY KEY (`title`,`id_creator`,`date_time`),
  KEY `creator_idx` (`id_creator`),
  KEY `creator_idxi` (`id_creator`),
  CONSTRAINT `creator_temp` FOREIGN KEY (`id_creator`) REFERENCES `users` (`id_user`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `temp_meetings`
--

LOCK TABLES `temp_meetings` WRITE;
/*!40000 ALTER TABLE `temp_meetings` DISABLE KEYS */;
/*!40000 ALTER TABLE `temp_meetings` ENABLE KEYS */;
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
  `displayed_name` varchar(60) NOT NULL,
  PRIMARY KEY (`id_user`),
  UNIQUE KEY `id_user_UNIQUE` (`id_user`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'lacus.Aliquam@sed.co.uk','917891','Jack Reed'),(2,'tincidunt@inlobortistellus.edu','578125','Wesley Knapp'),(3,'interdum.Curabitur.dictum@semper.com','766895','Elmo Valentine'),(6,'adipiscing.ligula@Aeneangravida.com','994192','Chancellor Sawyer'),(7,'at.sem@lectus.org','989958','Marvin Drake'),(8,'abcdef@wiki.it','304577','Jared Blankenship'),(9,'ac@massa.org','303703','Zeus SuperUser'),(10,'varius.et.euismod@neceleifend.co.uk','565592','Tarik Boyd'),(11,'ipsum@iaculisodio.co.uk','376030','Andrew Noel'),(12,'magna@dolor.edu','938427','Armand Shields'),(13,'Quisque.porttitor.eros@odiosagittissemper.net','856366','Peter Wilcox'),(14,'eu.elit.Nulla@fringillacursus.org','549514','Quamar Crane'),(15,'nulla@metus.edu','520481','Coby Oneal'),(17,'enim@accumsan.co.uk','764527','Asher Peters'),(18,'moonage@moon.co','620322','Kirk Reese'),(19,'tortor.at.risus@anteiaculis.org','414118','Jared Norris'),(20,'tellus@egetipsumSuspendisse.org','925382','Hakeem Hart'),(21,'alelisi@polimi.it','alessandrolisi','Alessandro Lisi'),(22,'marra@polimi.it','checksum','Gabriele Marra'),(23,'root@root.com','root','Utente Root'),(24,'stage@polimi.it','stage1234','Stagista');
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

-- Dump completed on 2020-07-21 23:18:54
