-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: trolley.proxy.rlwy.net    Database: railway
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
-- Table structure for table `adjunto`
--

DROP TABLE IF EXISTS `adjunto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `adjunto` (
  `id_adjunto` bigint NOT NULL AUTO_INCREMENT,
  `tipo` enum('AUDIO','IMAGEN','TEXTO','VIDEO') DEFAULT NULL,
  `url` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id_adjunto`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `adjunto`
--

LOCK TABLES `adjunto` WRITE;
/*!40000 ALTER TABLE `adjunto` DISABLE KEYS */;
/*!40000 ALTER TABLE `adjunto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `algoritmo_de_consenso`
--

DROP TABLE IF EXISTS `algoritmo_de_consenso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `algoritmo_de_consenso` (
  `id_algoritmo` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id_algoritmo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `algoritmo_de_consenso`
--

LOCK TABLES `algoritmo_de_consenso` WRITE;
/*!40000 ALTER TABLE `algoritmo_de_consenso` DISABLE KEYS */;
/*!40000 ALTER TABLE `algoritmo_de_consenso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categoria`
--

DROP TABLE IF EXISTS `categoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categoria` (
  `id_categoria` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_categoria`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categoria`
--

LOCK TABLES `categoria` WRITE;
/*!40000 ALTER TABLE `categoria` DISABLE KEYS */;
INSERT INTO `categoria` VALUES (1,'Cultura'),(2,'Servicios'),(3,'Incendio forestal'),(4,'Turismo'),(5,'Eventos'),(6,'Naturaleza'),(7,'Tránsito'),(8,'Inseguridad'),(9,'Clima'),(10,'Disturbios'),(11,'Hurto'),(12,'Accidente'),(13,'Emergencia Medica'),(14,'Infraestructura'),(15,'Vialidad'),(16,'Protesta'),(17,'Higiene'),(18,'Sismo'),(19,'Comunidad'),(20,'Mascotas'),(21,'Ambiente'),(22,'Convivencia'),(23,'Comercio'),(24,'Vandalismo'),(28,'Automovilismo'),(29,'Aeropuerto');
/*!40000 ALTER TABLE `categoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `coleccion`
--

DROP TABLE IF EXISTS `coleccion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `coleccion` (
  `id_coleccion` bigint NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(1000) DEFAULT NULL,
  `tipo_de_algoritmo` enum('ABSOLUTA','DEFAULT','MAYORIA_SIMPLE','MULTIPLES_MENCIONES') DEFAULT NULL,
  `titulo` varchar(255) NOT NULL,
  PRIMARY KEY (`id_coleccion`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `coleccion`
--

LOCK TABLES `coleccion` WRITE;
/*!40000 ALTER TABLE `coleccion` DISABLE KEYS */;
INSERT INTO `coleccion` VALUES (1,'son los hechos apartir de una fecha en especifico que es la de Fecha Despues del 01/12/2025','MAYORIA_SIMPLE','Cualquier cosa que entre dos fechas'),(3,'Son goles que se hicieron en el 2022','ABSOLUTA','Goles de Argentina'),(4,'Esta coleccion tiene como finalidad probar fucnionalidades','MAYORIA_SIMPLE','Coleccion de prueba'),(5,'hechos de transito.','DEFAULT','Hechos de transito'),(6,'todo','DEFAULT','Todo'),(7,'incidentes','DEFAULT','Incidentes en la via pública ');
/*!40000 ALTER TABLE `coleccion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `condicion`
--

DROP TABLE IF EXISTS `condicion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `condicion` (
  `condicion` varchar(31) NOT NULL,
  `id_condicion` bigint NOT NULL AUTO_INCREMENT,
  `fecha_antes` date DEFAULT NULL,
  `fecha_despues` date DEFAULT NULL,
  `titulo` varchar(255) DEFAULT NULL,
  `categoria_id_categoria` bigint DEFAULT NULL,
  `etiqueta_id_etiqueta` bigint DEFAULT NULL,
  `fuente_id_fuente` bigint DEFAULT NULL,
  PRIMARY KEY (`id_condicion`),
  KEY `FKl1mf5mf1he2simdnx6j6e2vpq` (`categoria_id_categoria`),
  KEY `FKs7l9mswal3kysn20fwya9xywx` (`etiqueta_id_etiqueta`),
  KEY `FKc4nxshams8b3ywejxkn6p403e` (`fuente_id_fuente`),
  CONSTRAINT `FKc4nxshams8b3ywejxkn6p403e` FOREIGN KEY (`fuente_id_fuente`) REFERENCES `fuente` (`id_fuente`),
  CONSTRAINT `FKl1mf5mf1he2simdnx6j6e2vpq` FOREIGN KEY (`categoria_id_categoria`) REFERENCES `categoria` (`id_categoria`),
  CONSTRAINT `FKs7l9mswal3kysn20fwya9xywx` FOREIGN KEY (`etiqueta_id_etiqueta`) REFERENCES `etiqueta` (`id_etiqueta`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `condicion`
--

LOCK TABLES `condicion` WRITE;
/*!40000 ALTER TABLE `condicion` DISABLE KEYS */;
INSERT INTO `condicion` VALUES ('fechaDespues',1,NULL,'2025-12-01',NULL,NULL,NULL,NULL),('fechaAntes',3,'2026-02-11',NULL,NULL,NULL,NULL,NULL),('categoria',4,NULL,NULL,NULL,28,NULL,NULL),('categoria',5,NULL,NULL,NULL,7,NULL,NULL),('fechaAntes',6,'2026-02-20',NULL,NULL,NULL,NULL,NULL),('categoria',7,NULL,NULL,NULL,9,NULL,NULL);
/*!40000 ALTER TABLE `condicion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `condicion_x_coleccion`
--

DROP TABLE IF EXISTS `condicion_x_coleccion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `condicion_x_coleccion` (
  `id_coleccion` bigint NOT NULL,
  `id_condicion` bigint NOT NULL,
  KEY `FK4ue9udwoub3b94jaceyq3u187` (`id_condicion`),
  KEY `FKlkfcmc7a0bvro404ohvl4kqsp` (`id_coleccion`),
  CONSTRAINT `FK4ue9udwoub3b94jaceyq3u187` FOREIGN KEY (`id_condicion`) REFERENCES `condicion` (`id_condicion`),
  CONSTRAINT `FKlkfcmc7a0bvro404ohvl4kqsp` FOREIGN KEY (`id_coleccion`) REFERENCES `coleccion` (`id_coleccion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `condicion_x_coleccion`
--

LOCK TABLES `condicion_x_coleccion` WRITE;
/*!40000 ALTER TABLE `condicion_x_coleccion` DISABLE KEYS */;
INSERT INTO `condicion_x_coleccion` VALUES (3,3),(4,4),(5,5),(6,6),(1,1),(7,7);
/*!40000 ALTER TABLE `condicion_x_coleccion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contribuyente`
--

DROP TABLE IF EXISTS `contribuyente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contribuyente` (
  `id_contribuyente` bigint NOT NULL AUTO_INCREMENT,
  `anonimo` bit(1) DEFAULT NULL,
  `apellido` varchar(255) DEFAULT NULL,
  `edad` int NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_contribuyente`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contribuyente`
--

LOCK TABLES `contribuyente` WRITE;
/*!40000 ALTER TABLE `contribuyente` DISABLE KEYS */;
/*!40000 ALTER TABLE `contribuyente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `etiqueta`
--

DROP TABLE IF EXISTS `etiqueta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `etiqueta` (
  `id_etiqueta` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_etiqueta`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `etiqueta`
--

LOCK TABLES `etiqueta` WRITE;
/*!40000 ALTER TABLE `etiqueta` DISABLE KEYS */;
/*!40000 ALTER TABLE `etiqueta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fuente`
--

DROP TABLE IF EXISTS `fuente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fuente` (
  `id_fuente` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  `tipo_fuente` enum('DEMO','DINAMICA','ESTATICA','METAMAPA') NOT NULL,
  `url` varchar(255) NOT NULL,
  PRIMARY KEY (`id_fuente`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fuente`
--

LOCK TABLES `fuente` WRITE;
/*!40000 ALTER TABLE `fuente` DISABLE KEYS */;
INSERT INTO `fuente` VALUES (1,'datos_6','ESTATICA','datos_6.csv'),(2,'hecho_para_fabri','ESTATICA','hecho_para_fabri.csv'),(3,'hechos_de_transito','ESTATICA','hechos_de_transito.csv'),(4,'hechos_para_juan','ESTATICA','hechos_para_juan.csv'),(5,'hechos_para_manu','ESTATICA','hechos_para_manu.csv'),(6,'hechos_para_ruka','ESTATICA','hechos_para_ruka.csv'),(7,'hechos_para_yeri','ESTATICA','hechos_para_yeri.csv'),(8,'hechos_sobre_kpopers','ESTATICA','hechos_sobre_kpopers.csv'),(14,'Dinamica','DINAMICA','https://metamapa-grupo9.onrender.com');
/*!40000 ALTER TABLE `fuente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fuente_x_coleccion`
--

DROP TABLE IF EXISTS `fuente_x_coleccion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fuente_x_coleccion` (
  `id_coleccion` bigint NOT NULL,
  `id_fuente` bigint NOT NULL,
  KEY `FKdgqqacc0qvj3lf0njet88kina` (`id_fuente`),
  KEY `FKits5r19yx31eml76m33e5um8s` (`id_coleccion`),
  CONSTRAINT `FKdgqqacc0qvj3lf0njet88kina` FOREIGN KEY (`id_fuente`) REFERENCES `fuente` (`id_fuente`),
  CONSTRAINT `FKits5r19yx31eml76m33e5um8s` FOREIGN KEY (`id_coleccion`) REFERENCES `coleccion` (`id_coleccion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fuente_x_coleccion`
--

LOCK TABLES `fuente_x_coleccion` WRITE;
/*!40000 ALTER TABLE `fuente_x_coleccion` DISABLE KEYS */;
INSERT INTO `fuente_x_coleccion` VALUES (1,1),(1,4),(1,7),(3,1),(3,3),(4,1),(4,2),(4,3),(4,4),(4,5),(4,6),(4,7),(4,8),(4,14),(5,1),(5,2),(5,3),(5,4),(5,5),(5,6),(5,7),(5,8),(5,14),(6,1),(6,4),(7,4),(7,2);
/*!40000 ALTER TABLE `fuente_x_coleccion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hecho`
--

DROP TABLE IF EXISTS `hecho`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hecho` (
  `id_hecho` bigint NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(1000) DEFAULT NULL,
  `estado_hecho` tinyint DEFAULT NULL,
  `fecha` date NOT NULL,
  `fecha_carga` datetime(6) NOT NULL,
  `tipo_hecho` enum('MULTIMEDIA','TEXTO') DEFAULT NULL,
  `titulo` varchar(255) DEFAULT NULL,
  `categoria_id_categoria` bigint DEFAULT NULL,
  `etiqueta_id_etiqueta` bigint DEFAULT NULL,
  `fuente_id_fuente` bigint DEFAULT NULL,
  `ubicacion_id_ubicacion` bigint DEFAULT NULL,
  PRIMARY KEY (`id_hecho`),
  KEY `FKoif6g4d6k2mso6lxfea6kpvs3` (`categoria_id_categoria`),
  KEY `FK752mx6wvjpls808es0frfmfl7` (`etiqueta_id_etiqueta`),
  KEY `FKmu5donrf3l7bvoyfrbklldhpt` (`fuente_id_fuente`),
  KEY `FKajepcpjt8krkgi8jjapcw9d1p` (`ubicacion_id_ubicacion`),
  CONSTRAINT `FK752mx6wvjpls808es0frfmfl7` FOREIGN KEY (`etiqueta_id_etiqueta`) REFERENCES `etiqueta` (`id_etiqueta`),
  CONSTRAINT `FKajepcpjt8krkgi8jjapcw9d1p` FOREIGN KEY (`ubicacion_id_ubicacion`) REFERENCES `ubicacion` (`id_ubicacion`),
  CONSTRAINT `FKmu5donrf3l7bvoyfrbklldhpt` FOREIGN KEY (`fuente_id_fuente`) REFERENCES `fuente` (`id_fuente`),
  CONSTRAINT `FKoif6g4d6k2mso6lxfea6kpvs3` FOREIGN KEY (`categoria_id_categoria`) REFERENCES `categoria` (`id_categoria`)
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hecho`
--

LOCK TABLES `hecho` WRITE;
/*!40000 ALTER TABLE `hecho` DISABLE KEYS */;
INSERT INTO `hecho` VALUES (1,'Desfile de gauchos en San Antonio de Areco.',3,'2025-11-10','2026-02-15 20:27:36.916515','TEXTO','Fiesta de la Tradición',1,NULL,1,1),(2,'Rotura de acueducto deja sin servicio a barrios de Santa Rosa.',3,'2025-12-08','2026-02-15 20:27:36.916528','TEXTO','Corte de agua',2,NULL,1,2),(3,'Quema de pastizales cerca de ruta nacional en La Rioja.',3,'2025-12-07','2026-02-15 20:27:36.916537','TEXTO','Fuego forestal',3,NULL,1,3),(4,'Ocupación hotelera plena en El Calafate por fin de semana largo.',3,'2025-12-08','2026-02-15 20:27:36.916545','TEXTO','Turismo masivo',4,NULL,1,4),(5,'Miles de fieles caminan hacia la Virgen del Valle.',3,'2025-12-08','2026-02-15 20:27:36.916553','TEXTO','Procesión religiosa',5,NULL,1,5),(6,'Temporada récord de ballenas en Puerto Madryn.',3,'2025-12-05','2026-02-15 20:27:36.916561','TEXTO','Avistaje de ballenas',6,NULL,1,6),(7,'Accidente leve genera filas en el Puente Zárate-Brazo Largo.',3,'2025-12-09','2026-02-15 20:27:36.916568','TEXTO','Demora en el puente',7,NULL,1,7),(8,'Asalto a mano armada en kiosco céntrico de Berazategui.',3,'2025-12-06','2026-02-15 20:27:36.916576','TEXTO','Robo en comercio',8,NULL,1,8),(9,'Feria internacional de cocina en Lima Perú.',3,'2025-12-05','2026-02-15 20:27:36.916590','TEXTO','Congreso gastronómico',5,NULL,1,9),(10,'Vientos fuertes y lluvia intensa en la costa de Miami.',3,'2025-12-04','2026-02-15 20:27:36.916608','TEXTO','Huracán leve',9,NULL,1,10),(11,'Disturbios y peleas en la via publica',3,'2023-02-01','2026-02-15 20:27:37.116584','TEXTO','Ruidos Molestos',10,NULL,2,11),(12,'Robo de cables de tendido electrico',3,'2023-02-02','2026-02-15 20:27:37.116593','TEXTO','Robo Cables',11,NULL,2,12),(13,'Accidente de moto por calzada resbaladiza',3,'2023-02-03','2026-02-15 20:27:37.116611','TEXTO','Moto Derrapada',12,NULL,2,13),(14,'Asistencia a persona desmayada en calle',3,'2023-02-04','2026-02-15 20:27:37.116618','TEXTO','Persona Descompensada',13,NULL,2,14),(15,'Colisión múltiple en Panamericana km 50 mano a Capital.',3,'2025-12-10','2026-02-15 20:27:37.307323','TEXTO','Choque en cadena',7,NULL,3,15),(16,'Cruce peligroso sin señalización lumínica en Av. Libertador.',3,'2025-12-11','2026-02-15 20:27:37.307333','TEXTO','Semáforo averiado',7,NULL,3,16),(17,'Corte total de 9 de Julio por reclamo vecinal.',3,'2025-12-12','2026-02-15 20:27:37.307342','TEXTO','Manifestación céntrica',7,NULL,3,17),(18,'Reducción de carril por repavimentación en zona sur.',3,'2025-12-10','2026-02-15 20:27:37.307351','TEXTO','Obra en calzada',7,NULL,3,18),(19,'Vehículo de gran porte bloquea salida de autopista.',3,'2025-12-13','2026-02-15 20:27:37.307360','TEXTO','Camión varado',7,NULL,3,19),(20,'Pozo causa daños en vehículos y demoras en Palermo.',3,'2025-12-14','2026-02-15 20:27:37.307371','TEXTO','Bache profundo',7,NULL,3,20),(21,'Largas filas en peaje de Acceso Oeste por sistema caído.',3,'2025-12-12','2026-02-15 20:27:37.307380','TEXTO','Demora en peaje',7,NULL,3,21),(22,'Vehículo detenido en carril rápido genera congestión.',3,'2025-12-15','2026-02-15 20:27:37.307389','TEXTO','Auto averiado',7,NULL,3,22),(23,'Calles cortadas por evento deportivo barrial.',3,'2025-12-16','2026-02-15 20:27:37.307398','TEXTO','Desvío por maratón',7,NULL,3,23),(24,'Cartel de contramano caído confunde a conductores.',3,'2025-12-11','2026-02-15 20:27:37.307413','TEXTO','Señalización caída',7,NULL,3,24),(25,'Sustracción de mochila a turistas en zona de Playa Grande.',3,'2025-12-04','2026-02-15 20:27:37.687593','TEXTO','Robo en la playa',8,NULL,5,35),(26,'Colisión entre tres vehículos en acceso norte a San Miguel de Tucumán.',3,'2025-12-03','2026-02-15 20:27:37.687612','TEXTO','Choque múltiple',7,NULL,5,36),(27,'Lluvia intensa provoca inundaciones en centro de Resistencia.',3,'2025-12-02','2026-02-15 20:27:37.687622','TEXTO','Calles anegadas',9,NULL,5,37),(28,'Poste de luz apagado hace una semana en calle principal de Lanús.',3,'2025-12-01','2026-02-15 20:27:37.687630','TEXTO','Luminaria fuera de servicio',14,NULL,5,38),(29,'Árbol con raíces levantando la vereda y riesgo de caída en San Isidro.',3,'2025-12-05','2026-02-15 20:27:37.687639','TEXTO','Árbol peligroso',14,NULL,5,39),(30,'Cráter en la avenida interrumpe el paso de colectivos en Neuquén capital.',3,'2025-11-30','2026-02-15 20:27:37.687648','TEXTO','Bache profundo',15,NULL,5,40),(31,'Evento gastronómico en el Centro Cívico con gran concurrencia.',3,'2025-12-06','2026-02-15 20:27:37.687656','TEXTO','Fiesta del Chocolate',5,NULL,5,41),(32,'Corte total de 9 de Julio por reclamo salarial.',3,'2025-12-04','2026-02-15 20:27:37.687665','TEXTO','Manifestación en Obelisco',16,NULL,5,42),(33,'Acumulación de residuos en esquina baldía de Quilmes.',3,'2025-12-03','2026-02-15 20:27:37.687673','TEXTO','Basural a cielo abierto',17,NULL,5,43),(34,'Temblor percibido en edificios altos de Santiago de Chile.',3,'2025-12-05','2026-02-15 20:27:37.687681','TEXTO','Sismo leve',18,NULL,5,44),(35,'Sujetos en moto arrebataron celular en la parada del colectivo 114.',3,'2025-11-15','2026-02-15 20:27:37.866681','TEXTO','Robo de celular',8,NULL,6,45),(36,'Pozo profundo en el carril derecho de Av. Corrientes al 3000.',3,'2025-11-20','2026-02-15 20:27:37.866697','TEXTO','Bache peligroso',14,NULL,6,46),(37,'Semáforo intermitente en cruce peligroso hace 2 días.',3,'2025-11-22','2026-02-15 20:27:37.866710','TEXTO','Semáforo roto',7,NULL,6,47),(38,'Festival de música en Parque Centenario entrada libre.',3,'2025-11-25','2026-02-15 20:27:37.866721','TEXTO','Recital gratuito',5,NULL,6,48),(39,'Rama de gran porte bloqueando la vereda tras la tormenta.',3,'2025-11-28','2026-02-15 20:27:37.866730','TEXTO','Árbol caído',14,NULL,6,49),(40,'Choque entre dos autos genera congestión en Autopista.',3,'2025-11-29','2026-02-15 20:27:37.866739','TEXTO','Demora por accidente',7,NULL,6,50),(41,'Feria de vecinos vendiendo ropa y antigüedades en la plaza.',3,'2025-11-29','2026-02-15 20:27:37.866747','TEXTO','Feria vecinal',19,NULL,6,51),(42,'Desagües tapados provocan anegamiento en la esquina tras la lluvia.',3,'2025-11-27','2026-02-15 20:27:37.866756','TEXTO','Calle inundada',9,NULL,6,52),(43,'Cortaron la cadena de la bici atada al poste mientras compraba.',3,'2025-11-26','2026-02-15 20:27:37.866765','TEXTO','Robo de bicicleta',8,NULL,6,53),(44,'Se busca perro Golden Retriever con collar rojo.',3,'2025-11-28','2026-02-15 20:27:37.866773','TEXTO','Mascota perdida',20,NULL,6,54),(45,'Apagón masivo en el barrio de Flores por explosión de transformador.',3,'2025-12-01','2026-02-15 20:27:38.057617','TEXTO','Corte de luz',2,NULL,7,55),(46,'Manifestación obstruyendo media calzada en zona céntrica.',3,'2025-12-02','2026-02-15 20:27:38.057627','TEXTO','Protesta sindical',7,NULL,7,56),(47,'Sustracción de cables de telefonía en La Plata.',3,'2025-11-30','2026-02-15 20:27:38.057636','TEXTO','Robo de cables',8,NULL,7,57),(48,'Fuego descontrolado cerca de la ruta en Córdoba capital.',3,'2025-11-28','2026-02-15 20:27:38.057644','TEXTO','Incendio de pastizales',21,NULL,7,58),(49,'Caída de mampostería en edificio antiguo de Congreso.',3,'2025-12-03','2026-02-15 20:27:38.057652','TEXTO','Derrumbe de mampostería',14,NULL,7,59),(50,'Música alta hasta la madrugada en Rosario centro.',3,'2025-11-29','2026-02-15 20:27:38.057660','TEXTO','Ruidos molestos',22,NULL,7,60),(51,'Puestos bloqueando el paso peatonal en estación Ramos Mejía.',3,'2025-12-01','2026-02-15 20:27:38.057667','TEXTO','Venta ambulante',23,NULL,7,61),(52,'Pérdida de agua potable inunda la vereda en ciudad de Mendoza.',3,'2025-11-25','2026-02-15 20:27:38.057676','TEXTO','Caño roto',14,NULL,7,62),(53,'Pintadas políticas en frente de casa particular en Caballito.',3,'2025-11-26','2026-02-15 20:27:38.057683','TEXTO','Graffitis en frente',24,NULL,7,63),(54,'Festival de música en la rambla de Montevideo.',3,'2025-12-02','2026-02-15 20:27:38.057704','TEXTO','Evento en la costa',5,NULL,7,64),(55,'Temblor de 4.5 grados sentido en la capital sanjuanina.',3,'2025-12-07','2026-02-15 20:27:38.256938','TEXTO','Sismo moderado',18,NULL,8,65),(56,'Alerta por altura del río Iguazú en zona de cataratas.',3,'2025-12-06','2026-02-15 20:27:38.256948','TEXTO','Crecida del río',9,NULL,8,66),(57,'Nieve fuera de temporada bloquea camino al glaciar en Ushuaia.',3,'2025-12-05','2026-02-15 20:27:38.256958','TEXTO','Nevada intensa',9,NULL,8,67),(58,'Comparsas desfilando en el corsódromo de Gualeguaychú.',3,'2025-12-07','2026-02-15 20:27:38.256965','TEXTO','Fiesta del Carnaval',5,NULL,8,68),(59,'Caída de piedras interrumpe el paso en ruta a Cafayate.',3,'2025-12-04','2026-02-15 20:27:38.256972','TEXTO','Desmoronamiento',15,NULL,8,69),(60,'Accidente leve entre taxi y particular en el Obelisco.',3,'2025-12-08','2026-02-15 20:27:38.256982','TEXTO','Choque en el centro',7,NULL,8,70),(61,'Foco de incendio controlado en Potrero de los Funes.',3,'2025-12-02','2026-02-15 20:27:38.256989','TEXTO','Incendio de sierras',21,NULL,8,71),(62,'Temperaturas récord afectan el suministro eléctrico en Formosa.',3,'2025-12-06','2026-02-15 20:27:38.256996','TEXTO','Calor extremo',9,NULL,8,72),(63,'Encuentro de comercio en Asunción del Paraguay.',3,'2025-12-05','2026-02-15 20:27:38.257003','TEXTO','Feria internacional',23,NULL,8,73),(64,'Lluvias fuertes en playas de Río de Janeiro.',4,'2025-12-07','2026-02-15 20:27:38.257011','TEXTO','Tormenta tropical',9,NULL,8,74),(74,'wjkefjwbc',4,'2026-02-19','2026-02-21 10:42:48.213293','TEXTO','Hechi ejemplo',6,NULL,14,84),(75,'jskc',3,'2026-02-20','2026-02-21 10:42:48.213325','TEXTO','Hecho Ejemplo 2',28,NULL,14,85),(76,'vuelos cancelados',3,'2026-02-21','2026-02-24 00:09:22.945800','TEXTO','vuelo cancellado',29,NULL,14,86);
/*!40000 ALTER TABLE `hecho` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hecho_adjuntos`
--

DROP TABLE IF EXISTS `hecho_adjuntos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hecho_adjuntos` (
  `hecho_id_hecho` bigint NOT NULL,
  `adjuntos_id_adjunto` bigint NOT NULL,
  UNIQUE KEY `UKb9w5ukc2pwnngo20en46jnt9y` (`adjuntos_id_adjunto`),
  KEY `FKa01c6loaltph8t3qe9dqexdo` (`hecho_id_hecho`),
  CONSTRAINT `FKa01c6loaltph8t3qe9dqexdo` FOREIGN KEY (`hecho_id_hecho`) REFERENCES `hecho` (`id_hecho`),
  CONSTRAINT `FKkdek3qu4vu4htm6dgl98jmxjh` FOREIGN KEY (`adjuntos_id_adjunto`) REFERENCES `adjunto` (`id_adjunto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hecho_adjuntos`
--

LOCK TABLES `hecho_adjuntos` WRITE;
/*!40000 ALTER TABLE `hecho_adjuntos` DISABLE KEYS */;
/*!40000 ALTER TABLE `hecho_adjuntos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hecho_x_coleccion`
--

DROP TABLE IF EXISTS `hecho_x_coleccion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hecho_x_coleccion` (
  `id_hecho_x_coleccion` bigint NOT NULL AUTO_INCREMENT,
  `consensuado` bit(1) NOT NULL DEFAULT b'0',
  `id_coleccion` bigint DEFAULT NULL,
  `id_hecho` bigint DEFAULT NULL,
  PRIMARY KEY (`id_hecho_x_coleccion`),
  KEY `FKe0lyiobc5q6c27cbwakn4gu2k` (`id_coleccion`),
  KEY `FK6a580pwgvdx50gv3hbd029v2j` (`id_hecho`),
  CONSTRAINT `FK6a580pwgvdx50gv3hbd029v2j` FOREIGN KEY (`id_hecho`) REFERENCES `hecho` (`id_hecho`),
  CONSTRAINT `FKe0lyiobc5q6c27cbwakn4gu2k` FOREIGN KEY (`id_coleccion`) REFERENCES `coleccion` (`id_coleccion`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hecho_x_coleccion`
--

LOCK TABLES `hecho_x_coleccion` WRITE;
/*!40000 ALTER TABLE `hecho_x_coleccion` DISABLE KEYS */;
INSERT INTO `hecho_x_coleccion` VALUES (1,_binary '\0',1,2),(2,_binary '\0',1,2),(3,_binary '\0',1,3),(4,_binary '\0',1,3),(5,_binary '\0',1,4),(6,_binary '\0',1,4),(7,_binary '\0',1,5),(8,_binary '\0',1,5),(9,_binary '\0',1,6),(10,_binary '\0',1,6),(11,_binary '\0',1,7),(12,_binary '\0',1,7),(13,_binary '\0',1,8),(14,_binary '\0',1,8),(15,_binary '\0',1,9),(16,_binary '\0',1,9),(17,_binary '\0',1,10),(18,_binary '\0',1,10),(19,_binary '\0',1,46),(20,_binary '\0',1,46),(21,_binary '\0',1,49),(22,_binary '\0',1,49),(23,_binary '\0',1,54),(24,_binary '\0',1,54),(25,_binary '\0',3,1),(26,_binary '\0',3,2),(27,_binary '\0',3,3),(28,_binary '\0',3,4),(29,_binary '\0',3,5),(30,_binary '\0',3,6),(31,_binary '\0',3,7),(32,_binary '\0',3,8),(33,_binary '\0',3,9),(34,_binary '\0',3,10),(35,_binary '\0',3,15),(36,_binary '\0',3,16),(37,_binary '\0',3,17),(38,_binary '\0',3,18),(39,_binary '\0',3,19),(40,_binary '\0',3,20),(41,_binary '\0',3,21),(42,_binary '\0',3,22),(43,_binary '\0',3,23),(44,_binary '\0',3,24),(45,_binary '\0',4,75),(46,_binary '',5,7),(47,_binary '',5,15),(48,_binary '',5,16),(49,_binary '',5,17),(50,_binary '',5,18),(51,_binary '',5,19),(52,_binary '',5,20),(53,_binary '',5,21),(54,_binary '',5,22),(55,_binary '',5,23),(56,_binary '',5,24),(57,_binary '',5,26),(58,_binary '',5,37),(59,_binary '',5,40),(60,_binary '',5,46),(61,_binary '',5,60),(62,_binary '',6,1),(63,_binary '',6,2),(64,_binary '',6,3),(65,_binary '',6,4),(66,_binary '',6,5),(67,_binary '',6,6),(68,_binary '',6,7),(69,_binary '',6,8),(70,_binary '',6,9),(71,_binary '',6,10);
/*!40000 ALTER TABLE `hecho_x_coleccion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `provincia`
--

DROP TABLE IF EXISTS `provincia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `provincia` (
  `id_provincia` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  `pais` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_provincia`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `provincia`
--

LOCK TABLES `provincia` WRITE;
/*!40000 ALTER TABLE `provincia` DISABLE KEYS */;
INSERT INTO `provincia` VALUES (1,'Buenos Aires','Argentina'),(2,'La Pampa','Argentina'),(3,'La Rioja','Argentina'),(4,'Santa Cruz','Argentina'),(5,'Catamarca','Argentina'),(6,'Chubut','Argentina'),(7,'EXTERIOR','EXTERIOR'),(8,'Ciudad Autonoma de Buenos Aires','Argentina'),(9,'Tucuman','Argentina'),(10,'Chaco','Argentina'),(11,'Neuquen','Argentina'),(12,'Rio Negro','Argentina'),(13,'Cordoba','Argentina'),(14,'Santa Fe','Argentina'),(15,'Mendoza','Argentina'),(16,'San Juan','Argentina'),(17,'Tierra del Fuego, Antartida E Islas del Atlantico Sur','Argentina'),(18,'Entre Rios','Argentina'),(19,'Salta','Argentina'),(20,'San Luis','Argentina'),(21,'Formosa','Argentina');
/*!40000 ALTER TABLE `provincia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `solicitud`
--

DROP TABLE IF EXISTS `solicitud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `solicitud` (
  `id_solicitud` bigint NOT NULL AUTO_INCREMENT,
  `estado` enum('ACEPTADA','PENDIENTE','RECHAZADA') NOT NULL,
  `fecha` date NOT NULL,
  `id_solicitante` bigint NOT NULL,
  `motivo` varchar(200) DEFAULT NULL,
  `spam` bit(1) DEFAULT NULL,
  `hecho_id_hecho` bigint DEFAULT NULL,
  PRIMARY KEY (`id_solicitud`),
  KEY `FKi505ws8t0nqwt02yrpnpppqfe` (`hecho_id_hecho`),
  CONSTRAINT `FKi505ws8t0nqwt02yrpnpppqfe` FOREIGN KEY (`hecho_id_hecho`) REFERENCES `hecho` (`id_hecho`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `solicitud`
--

LOCK TABLES `solicitud` WRITE;
/*!40000 ALTER TABLE `solicitud` DISABLE KEYS */;
INSERT INTO `solicitud` VALUES (1,'PENDIENTE','2026-02-21',5,'nome gusta',_binary '\0',64),(2,'RECHAZADA','2026-02-21',5,'plata',_binary '',3),(3,'RECHAZADA','2026-02-21',5,'dinero',_binary '',3),(4,'RECHAZADA','2026-02-21',5,'r',_binary '',3),(5,'PENDIENTE','2026-02-21',5,'Me parece que el hecho no esta bien',_binary '\0',3),(6,'RECHAZADA','2026-02-21',5,'dinero',_binary '',3),(7,'PENDIENTE','2026-02-21',5,'Este hecho es cualquier cosa!!',_binary '\0',74),(8,'PENDIENTE','2026-02-26',2,'horrible',_binary '\0',2);
/*!40000 ALTER TABLE `solicitud` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ubicacion`
--

DROP TABLE IF EXISTS `ubicacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ubicacion` (
  `id_ubicacion` bigint NOT NULL AUTO_INCREMENT,
  `latitud` float NOT NULL,
  `longitud` float NOT NULL,
  `id_provincia` bigint DEFAULT NULL,
  PRIMARY KEY (`id_ubicacion`),
  KEY `FKk52vtin2x1ijf8h3rjlpxgyr2` (`id_provincia`),
  CONSTRAINT `FKk52vtin2x1ijf8h3rjlpxgyr2` FOREIGN KEY (`id_provincia`) REFERENCES `provincia` (`id_provincia`)
) ENGINE=InnoDB AUTO_INCREMENT=87 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ubicacion`
--

LOCK TABLES `ubicacion` WRITE;
/*!40000 ALTER TABLE `ubicacion` DISABLE KEYS */;
INSERT INTO `ubicacion` VALUES (1,-34.2443,-59.4746,1),(2,-36.6167,-64.2833,2),(3,-29.4131,-66.8558,3),(4,-50.3379,-72.2648,4),(5,-28.4696,-65.7852,5),(6,-42.7692,-65.0385,6),(7,-34.1,-59,1),(8,-34.7633,-58.2117,1),(9,-12.0464,-77.0428,7),(10,25.7617,-80.1918,7),(11,-34.58,-58.41,8),(12,-34.65,-58.45,8),(13,-34.63,-58.44,8),(14,-34.61,-58.37,8),(15,-34.45,-58.9,1),(16,-34.55,-58.45,8),(17,-34.6037,-58.3816,8),(18,-34.62,-58.4,8),(19,-34.63,-58.37,8),(20,-34.58,-58.42,8),(21,-34.64,-58.6,1),(22,-34.59,-58.39,8),(23,-34.57,-58.43,8),(24,-34.61,-58.41,8),(25,-34.2443,-59.4746,1),(26,-36.6167,-64.2833,2),(27,-29.4131,-66.8558,3),(28,-50.3379,-72.2648,4),(29,-28.4696,-65.7852,5),(30,-42.7692,-65.0385,6),(31,-34.1,-59,1),(32,-34.7633,-58.2117,1),(33,-12.0464,-77.0428,7),(34,25.7617,-80.1918,7),(35,-38.0283,-57.5315,1),(36,-26.8083,-65.2176,9),(37,-27.4606,-58.9839,10),(38,-34.7053,-58.3917,1),(39,-34.47,-58.52,1),(40,-38.9516,-68.0591,11),(41,-41.1335,-71.3103,12),(42,-34.6037,-58.3816,8),(43,-34.72,-58.26,1),(44,-33.4489,-70.6693,7),(45,-34.5984,-58.4201,8),(46,-34.6037,-58.4108,8),(47,-34.615,-58.385,8),(48,-34.6065,-58.4356,8),(49,-34.589,-58.4,8),(50,-34.628,-58.45,8),(51,-34.57,-58.44,8),(52,-34.62,-58.39,8),(53,-34.58,-58.42,8),(54,-34.61,-58.41,8),(55,-34.62,-58.44,8),(56,-34.6037,-58.3816,8),(57,-34.9214,-57.9545,1),(58,-31.4201,-64.1888,13),(59,-34.61,-58.39,8),(60,-32.9468,-60.6393,14),(61,-34.65,-58.55,1),(62,-32.8908,-68.8272,15),(63,-34.6,-58.45,8),(64,-34.9011,-56.1645,7),(65,-31.5375,-68.5364,16),(66,-25.6953,-54.4367,7),(67,-54.8019,-68.303,17),(68,-33.0094,-58.5173,18),(69,-26.0733,-65.976,19),(70,-34.6037,-58.3816,8),(71,-33.2239,-66.2336,20),(72,-26.1849,-58.1731,21),(73,-25.2637,-57.5759,7),(74,-22.9068,-43.1729,7),(84,-34.8679,-65.1733,20),(85,-36.3753,-52.1166,7),(86,-35.998,-58.4474,1);
/*!40000 ALTER TABLE `ubicacion` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-02-26 13:46:56
