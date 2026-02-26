-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: shuttle.proxy.rlwy.net    Database: railway
-- ------------------------------------------------------
-- Server version	9.4.0

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
-- Table structure for table `fuente_estatica`
--

DROP TABLE IF EXISTS `fuente_estatica`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fuente_estatica` (
  `ruta_dataset` varchar(255) NOT NULL,
  `estado_procesado` enum('NO_PROCESADO','PROCESADO') DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `tipo_fuente` enum('ESTATICA') DEFAULT NULL,
  PRIMARY KEY (`ruta_dataset`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fuente_estatica`
--

LOCK TABLES `fuente_estatica` WRITE;
/*!40000 ALTER TABLE `fuente_estatica` DISABLE KEYS */;
INSERT INTO `fuente_estatica` VALUES ('datos_6.csv','PROCESADO','datos_6','ESTATICA'),('hecho_para_fabri.csv','PROCESADO','hecho_para_fabri','ESTATICA'),('hechos_de_transito.csv','PROCESADO','hechos_de_transito','ESTATICA'),('hechos_para_juan.csv','PROCESADO','hechos_para_juan','ESTATICA'),('hechos_para_manu.csv','PROCESADO','hechos_para_manu','ESTATICA'),('hechos_para_ruka.csv','PROCESADO','hechos_para_ruka','ESTATICA'),('hechos_para_yeri.csv','PROCESADO','hechos_para_yeri','ESTATICA'),('hechos_sobre_kpopers.csv','PROCESADO','hechos_sobre_kpopers','ESTATICA');
/*!40000 ALTER TABLE `fuente_estatica` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-02-26 19:19:03
