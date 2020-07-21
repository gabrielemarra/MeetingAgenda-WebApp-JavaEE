-- MySQL dump 10.13  Distrib 8.0.19, for macos10.15 (x86_64)
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
-- Dumping data for table `invitation`
--

LOCK TABLES `invitation` WRITE;
/*!40000 ALTER TABLE `invitation` DISABLE KEYS */;
INSERT INTO `invitation` VALUES (1,3),(2,4),(2,5),(1,7),(1,8),(5,8),(1,9),(5,9),(1,10),(5,10),(1,11),(1,13),(1,14),(1,15),(4,15),(1,18),(4,21),(1,22),(4,22),(4,23);
/*!40000 ALTER TABLE `invitation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `meetings`
--

LOCK TABLES `meetings` WRITE;
/*!40000 ALTER TABLE `meetings` DISABLE KEYS */;
INSERT INTO `meetings` VALUES (1,'Accountability C45B','2020-09-24 20:30:00',30,15,21),(2,'Board of Directors','2020-08-05 10:00:00',60,4,21),(5,'guests','2020-12-20 10:34:00',60,4,21),(4,'Resource Planning 01-8-c534k3','2020-10-20 06:45:00',60,45,18),(3,'Resource Planning 01-8-c65k3','2020-08-01 06:35:00',134,5,18);
/*!40000 ALTER TABLE `meetings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `temp_meetings`
--

LOCK TABLES `temp_meetings` WRITE;
/*!40000 ALTER TABLE `temp_meetings` DISABLE KEYS */;
/*!40000 ALTER TABLE `temp_meetings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'lacus.Aliquam@sed.co.uk','917891','Jack Reed'),(2,'tincidunt@inlobortistellus.edu','578125','Wesley Knapp'),(3,'interdum.Curabitur.dictum@semper.com','766895','Elmo Valentine'),(4,'mi.Aliquam@ridiculus.edu','901038','Drew Lowery'),(5,'Donec@ornareliberoat.com','310099','Coby Kirkland'),(6,'adipiscing.ligula@Aeneangravida.com','994192','Chancellor Sawyer'),(7,'at.sem@lectus.org','989958','Marvin Drake'),(8,'abcdef@wiki.it','304577','Jared Blankenship'),(9,'ac@massa.org','303703','Zeus Schroeder'),(10,'varius.et.euismod@neceleifend.co.uk','565592','Tarik Boyd'),(11,'ipsum@iaculisodio.co.uk','376030','Andrew Noel'),(12,'magna@dolor.edu','938427','Armand Shields'),(13,'Quisque.porttitor.eros@odiosagittissemper.net','856366','Peter Wilcox'),(14,'eu.elit.Nulla@fringillacursus.org','549514','Quamar Crane'),(15,'nulla@metus.edu','520481','Coby Oneal'),(16,'tortor.at@aliquetmagna.edu','502046','Xanthus Tucker'),(17,'enim@accumsan.co.uk','764527','Asher Peters'),(18,'moonage@moon.co','620322','Kirk Reese'),(19,'tortor.at.risus@anteiaculis.org','414118','Jared Norris'),(20,'tellus@egetipsumSuspendisse.org','925382','Hakeem Hart'),(21,'alelisi@polimi.it','alessandrolisi','Alessandro Lisi'),(22,'marra@polimi.it','checksum','Gabriele Marra'),(23,'root@root.com','root','Utente Root');
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

-- Dump completed on 2020-07-21 22:56:19
