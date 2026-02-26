-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: localhost    Database: fuentedinamica
-- ------------------------------------------------------
-- Server version	8.4.2

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
-- Table structure for table `archivo`
--

DROP TABLE IF EXISTS `archivo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `archivo` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tamanio` varchar(255) DEFAULT NULL,
  `tipo_media` enum('AUDIO','IMAGEN','TEXTO','VIDEO') NOT NULL,
  `url` varchar(255) NOT NULL,
  `hecho_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKs293lv8g0q6p68udde3jn2frt` (`hecho_id`),
  CONSTRAINT `FKs293lv8g0q6p68udde3jn2frt` FOREIGN KEY (`hecho_id`) REFERENCES `hecho` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `archivo`
--

LOCK TABLES `archivo` WRITE;
/*!40000 ALTER TABLE `archivo` DISABLE KEYS */;
INSERT INTO `archivo` VALUES (1,'42687','IMAGEN','https://res.cloudinary.com/dnumakjin/image/upload/v1772122218/szfxsdh4j0fulukye0hn.jpg',3);
/*!40000 ALTER TABLE `archivo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contribucion`
--

DROP TABLE IF EXISTS `contribucion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contribucion` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `anonimo` bit(1) NOT NULL,
  `exportada` bit(1) NOT NULL,
  `fecha_de_carga` date NOT NULL,
  `contribuyente_id` bigint NOT NULL,
  `hecho_id` bigint NOT NULL,
  `revision_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK7v50dlqnpw8479edhub0nshe6` (`hecho_id`),
  UNIQUE KEY `UKrwj1s2j52j631nc9ohod9v0dr` (`revision_id`),
  KEY `FK45wq7liua6kkwaqjkxfm3vb96` (`contribuyente_id`),
  CONSTRAINT `FK45wq7liua6kkwaqjkxfm3vb96` FOREIGN KEY (`contribuyente_id`) REFERENCES `contribuyente` (`id`),
  CONSTRAINT `FKaafandvicx890qgc0wbb1ujls` FOREIGN KEY (`hecho_id`) REFERENCES `hecho` (`id`),
  CONSTRAINT `FKfmf6ojw9lw0r8u9987dxyq54j` FOREIGN KEY (`revision_id`) REFERENCES `revision` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contribucion`
--

LOCK TABLES `contribucion` WRITE;
/*!40000 ALTER TABLE `contribucion` DISABLE KEYS */;
INSERT INTO `contribucion` VALUES (1,_binary '\0',_binary '\0','2026-02-26',1,1,1),(2,_binary '\0',_binary '\0','2026-02-26',1,2,2),(3,_binary '\0',_binary '\0','2026-02-26',2,3,3);
/*!40000 ALTER TABLE `contribucion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contribuyente`
--

DROP TABLE IF EXISTS `contribuyente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contribuyente` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `apellido` varchar(255) NOT NULL,
  `edad` int DEFAULT NULL,
  `keycloak_id` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKiieqfxh3gpth3g8dvfrf06uds` (`keycloak_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contribuyente`
--

LOCK TABLES `contribuyente` WRITE;
/*!40000 ALTER TABLE `contribuyente` DISABLE KEYS */;
INSERT INTO `contribuyente` VALUES (1,'Villarreal',NULL,'0ad2634f-9ba2-45ca-b62c-84ca820e0646','Jimena'),(2,'garcia',NULL,'4169e87d-39aa-4bc8-a67f-271e1f95ad3b','roberto');
/*!40000 ALTER TABLE `contribuyente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hecho`
--

DROP TABLE IF EXISTS `hecho`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hecho` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `titulo` varchar(255) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `descripcion` varchar(1000) DEFAULT NULL,
  `fecha` date NOT NULL,
  `latitud` float DEFAULT NULL,
  `longitud` float DEFAULT NULL,
  `origen` varchar(255) DEFAULT NULL,
  `tipo_de_hecho` enum('MULTIMEDIA','TEXTO') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hecho`
--

LOCK TABLES `hecho` WRITE;
/*!40000 ALTER TABLE `hecho` DISABLE KEYS */;
INSERT INTO `hecho` VALUES (1,'Casa tomada en Lomas de Zamora','Inseguridad','Ocurrio un ocupacion ilegal de una propiedad cerca al estacion de tren de lomas de zamora','2026-02-26',-34.7573,-58.4027,NULL,'TEXTO'),(2,'Japones perdido','Desaparecimiento','Se encuentra perdido un japones de nombre xxxxxx, y apellido xxxxx , el dia de 26 de febrero de 2026','2026-02-26',-34.5572,-58.4504,NULL,'TEXTO'),(3,'Atraco en Flores Avellaneda','Hurto','Se robaron un coche en pleno rivadavia y san pedrito','2026-02-11',-34.6197,-58.4521,NULL,'MULTIMEDIA');
/*!40000 ALTER TABLE `hecho` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `outbox`
--

DROP TABLE IF EXISTS `outbox`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `outbox` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `hecho_json` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `outbox`
--

LOCK TABLES `outbox` WRITE;
/*!40000 ALTER TABLE `outbox` DISABLE KEYS */;
INSERT INTO `outbox` VALUES (1,'{\"titulo\":\"Japones perdido\",\"descripcion\":\"Se encuentra perdido un japones de nombre xxxxxx, y apellido xxxxx , el dia de 26 de febrero de 2026\",\"fecha\":[2026,2,26],\"ubicacion\":{\"latitud\":-34.5572,\"longitud\":-58.4504},\"categoria\":\"Desaparecimiento\",\"adjuntos\":null,\"tipoDeHecho\":\"TEXTO\"}');
/*!40000 ALTER TABLE `outbox` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `revision`
--

DROP TABLE IF EXISTS `revision`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `revision` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `estado` enum('ACEPTADA','ACEPTADA_CON_SUGERENCIA','PENDIENTE','RECHAZADA') NOT NULL,
  `fecha` date DEFAULT NULL,
  `mensaje` varchar(500) DEFAULT NULL,
  `responsable_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKa7cf4yxvidnl9u6ta089q0tnf` (`responsable_id`),
  CONSTRAINT `FKa7cf4yxvidnl9u6ta089q0tnf` FOREIGN KEY (`responsable_id`) REFERENCES `contribuyente` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `revision`
--

LOCK TABLES `revision` WRITE;
/*!40000 ALTER TABLE `revision` DISABLE KEYS */;
INSERT INTO `revision` VALUES (1,'ACEPTADA_CON_SUGERENCIA','2026-02-26','debera cambiar la zona y especificarla claramente',1),(2,'ACEPTADA','2026-02-26','ok',1),(3,'PENDIENTE',NULL,'El hecho está pendiente de revisión',NULL);
/*!40000 ALTER TABLE `revision` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-02-26 13:15:20
