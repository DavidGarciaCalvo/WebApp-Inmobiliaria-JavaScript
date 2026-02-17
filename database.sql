CREATE DATABASE  IF NOT EXISTS `vitobadi16` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `vitobadi16`;
-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: vitobadi16
-- ------------------------------------------------------
-- Server version	8.0.43

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
-- Table structure for table `alquiler`
--

DROP TABLE IF EXISTS `alquiler`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alquiler` (
  `idAlquiler` int NOT NULL AUTO_INCREMENT,
  `codHabi` int NOT NULL,
  `emailInquilino` varchar(100) NOT NULL,
  `fechaInicioAlqui` date NOT NULL,
  `fechaFinAlqui` date NOT NULL,
  PRIMARY KEY (`idAlquiler`),
  KEY `codHabi` (`codHabi`),
  KEY `emailInquilino` (`emailInquilino`),
  CONSTRAINT `alquiler_ibfk_1` FOREIGN KEY (`codHabi`) REFERENCES `habitacion` (`codHabi`),
  CONSTRAINT `alquiler_ibfk_2` FOREIGN KEY (`emailInquilino`) REFERENCES `usuario` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alquiler`
--

LOCK TABLES `alquiler` WRITE;
/*!40000 ALTER TABLE `alquiler` DISABLE KEYS */;
INSERT INTO `alquiler` VALUES (1,1,'prop5@gmail.com','2022-01-01','2022-12-31'),(2,7,'prop6@gmail.com','2024-01-01','2025-12-31'),(3,6,'prop7@gmail.com','2023-01-01','2023-06-30'),(4,6,'prop7@gmail.com','2025-01-01','2025-06-30'),(5,1,'prop7@gmail.com','2026-01-06','2026-01-06'),(6,2,'prop8@gmail.com','2025-03-01','2025-08-01'),(8,2,'prop5@gmail.com','2026-01-06','2026-01-09'),(9,2,'prop5@gmail.com','2026-01-12','2026-01-18'),(11,3,'prop7@gmail.com','2026-01-06','2026-01-08');
/*!40000 ALTER TABLE `alquiler` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `habitacion`
--

DROP TABLE IF EXISTS `habitacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `habitacion` (
  `codHabi` int NOT NULL AUTO_INCREMENT,
  `ciudad` varchar(50) NOT NULL,
  `direccion` varchar(150) NOT NULL,
  `emailPropietario` varchar(100) NOT NULL,
  `imagenHabitacion` varchar(255) DEFAULT NULL,
  `latitudH` double(10,4) DEFAULT NULL,
  `longitudH` double(10,4) DEFAULT NULL,
  `precioMes` int NOT NULL,
  PRIMARY KEY (`codHabi`),
  KEY `emailPropietario` (`emailPropietario`),
  CONSTRAINT `habitacion_ibfk_1` FOREIGN KEY (`emailPropietario`) REFERENCES `usuario` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `habitacion`
--

LOCK TABLES `habitacion` WRITE;
/*!40000 ALTER TABLE `habitacion` DISABLE KEYS */;
INSERT INTO `habitacion` VALUES (1,'Vitoria','Calle Dato 1','prop1@gmail.com','img/hab1.jpg',42.8467,-2.6716,300),(2,'Vitoria','San Prudencio 3','prop1@gmail.com','img/hab2.jpg',42.8450,-2.6705,350),(3,'Vitoria','Av. Gasteiz 40','prop1@gmail.com','img/hab3.jpg',42.8520,-2.6750,320),(4,'Vitoria','Calle Francia 10','prop1@gmail.com','img/hab4.jpg',42.8485,-2.6680,280),(5,'Vitoria','Portal de Arriaga','prop1@gmail.com','img/hab5.jpg',42.8550,-2.6780,310),(6,'Vitoria','Lakua 5','prop4@gmail.com','img/hab6.jpg',42.8650,-2.6800,290),(7,'Bilbao','Gran Vía 55','prop2@gmail.com','img/hab7.jpg',43.2630,-2.9350,500),(8,'Donostia','La Concha 1','prop3@gmail.com','img/hab8.jpg',43.3183,-1.9812,600),(9,'Vitoria','Ferrocarril 2','prop1@gmail.com','img/hab9.jpg',42.8467,-2.6716,260);
/*!40000 ALTER TABLE `habitacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `puntuacion`
--

DROP TABLE IF EXISTS `puntuacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `puntuacion` (
  `codHabi` int NOT NULL,
  `emailInquilino` varchar(100) NOT NULL,
  `puntos` int NOT NULL,
  PRIMARY KEY (`codHabi`,`emailInquilino`),
  KEY `emailInquilino` (`emailInquilino`),
  CONSTRAINT `puntuacion_ibfk_1` FOREIGN KEY (`codHabi`) REFERENCES `habitacion` (`codHabi`),
  CONSTRAINT `puntuacion_ibfk_2` FOREIGN KEY (`emailInquilino`) REFERENCES `usuario` (`email`),
  CONSTRAINT `puntuacion_chk_1` CHECK (((`puntos` >= 0) and (`puntos` <= 5)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `puntuacion`
--

LOCK TABLES `puntuacion` WRITE;
/*!40000 ALTER TABLE `puntuacion` DISABLE KEYS */;
INSERT INTO `puntuacion` VALUES (1,'prop5@gmail.com',5),(6,'prop7@gmail.com',4);
/*!40000 ALTER TABLE `puntuacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `solicitud`
--

DROP TABLE IF EXISTS `solicitud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `solicitud` (
  `idSolicitud` int NOT NULL AUTO_INCREMENT,
  `codHabi` int NOT NULL,
  `emailInquilino` varchar(100) NOT NULL,
  `estado` varchar(20) NOT NULL,
  `fechaInicioPosibleAlquiler` date DEFAULT NULL,
  `fechaFinPosibleAlquiler` date DEFAULT NULL,
  PRIMARY KEY (`idSolicitud`),
  KEY `codHabi` (`codHabi`),
  KEY `emailInquilino` (`emailInquilino`),
  CONSTRAINT `solicitud_ibfk_1` FOREIGN KEY (`codHabi`) REFERENCES `habitacion` (`codHabi`),
  CONSTRAINT `solicitud_ibfk_2` FOREIGN KEY (`emailInquilino`) REFERENCES `usuario` (`email`),
  CONSTRAINT `solicitud_chk_1` CHECK ((`estado` in (_utf8mb4'pendiente',_utf8mb4'aceptada',_utf8mb4'rechazada')))
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `solicitud`
--

LOCK TABLES `solicitud` WRITE;
/*!40000 ALTER TABLE `solicitud` DISABLE KEYS */;
INSERT INTO `solicitud` VALUES (2,2,'prop8@gmail.com','aceptada','2025-03-01','2025-08-01'),(3,3,'prop6@gmail.com','rechazada','2024-01-01','2024-02-01'),(4,1,'prop7@gmail.com','aceptada','2026-01-06','2026-01-06'),(6,2,'prop5@gmail.com','aceptada','2026-01-06','2026-01-09'),(7,2,'prop5@gmail.com','aceptada','2026-01-12','2026-01-18'),(9,2,'prop7@gmail.com','pendiente','2026-01-06','2026-01-06'),(10,3,'prop7@gmail.com','rechazada','2026-01-06','2026-01-10'),(11,3,'prop7@gmail.com','aceptada','2026-01-06','2026-01-10'),(12,3,'prop7@gmail.com','pendiente','2026-01-31','2026-01-31'),(14,3,'prop5@gmail.com','rechazada','2026-01-06','2026-01-08'),(15,2,'prop5@gmail.com','pendiente','2026-01-06','2026-01-10');
/*!40000 ALTER TABLE `solicitud` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `email` varchar(100) NOT NULL,
  `contrasena` varchar(50) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `imagenUsuario` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES ('prop1@gmail.com','prop1','Usuario Prop1','img/user1.png'),('prop2@gmail.com','prop2','Usuario Prop2','img/user2.png'),('prop3@gmail.com','prop3','Usuario Prop3','img/user3.png'),('prop4@gmail.com','prop4','Usuario Prop4','img/user4.png'),('prop5@gmail.com','prop5','Usuario Inquilino 5','img/user5.png'),('prop6@gmail.com','prop6','Usuario Inquilino 6','img/user6.png'),('prop7@gmail.com','prop7','Usuario Inquilino 7','img/user7.png'),('prop8@gmail.com','prop8','Usuario Inquilino 8','img/user8.png');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-06 15:02:40
