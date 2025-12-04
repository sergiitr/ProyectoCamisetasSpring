CREATE DATABASE IF NOT EXISTS `camisetas`;
USE `camisetas`;

DROP TABLE IF EXISTS `linea_pedido`;
DROP TABLE IF EXISTS `pedido`;
DROP TABLE IF EXISTS `usuario`;

DROP TABLE IF EXISTS `camiseta`;
DROP TABLE IF EXISTS `categoria`; 

CREATE TABLE IF NOT EXISTS `categoria` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `descripcion` VARCHAR(255) NULL,
  `nombre` VARCHAR(255) NULL,
  `padre_id` BIGINT NULL,
  FOREIGN KEY (`padre_id`) REFERENCES `categoria`(`id`)
); 

CREATE TABLE `camiseta` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `talla` ENUM('xxs','xs','s','m','l','xl','xxl') NOT NULL,
  `sexo` ENUM('chica','chico','unisex','nino','nina','unisex_infantil') NOT NULL,
  `color` VARCHAR(50) NOT NULL,
  `marca` VARCHAR(50) NOT NULL,
  `stock` INT UNSIGNED NOT NULL DEFAULT 0,
  `precio` DECIMAL(8,2) NOT NULL,
  `activo` BOOLEAN,  
  `nombre` VARCHAR(255) NULL, 
  `descripcion` VARCHAR(255) NULL,   
  `categoria_id` BIGINT, 
  PRIMARY KEY (`id`),
  
  CONSTRAINT `fk_camiseta_categoria` FOREIGN KEY (`categoria_id`) REFERENCES `categoria`(`id`)
);


CREATE TABLE `usuario` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL UNIQUE,
  `password` VARCHAR(255) NOT NULL,
  `email` VARCHAR(100) NOT NULL UNIQUE,
  `telefono` VARCHAR(20),
  `direccion` VARCHAR(255),
  `activo` BOOLEAN,
  `tipo` ENUM('OPERADOR','CLIENTE'),  
  PRIMARY KEY (`id`)
); 

CREATE TABLE `pedido` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `fecha` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `estado` ENUM('carrito','pagado','procesando','procesado','enviado','recibido') NOT NULL DEFAULT 'carrito',
  `cliente` INT UNSIGNED NOT NULL,
  `total` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`cliente`) REFERENCES `usuario`(`id`)
); 


CREATE TABLE `linea_pedido` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `pedido` INT UNSIGNED NOT NULL,
  `producto` INT UNSIGNED NOT NULL,
  `precio_venta` DECIMAL(8,2) NOT NULL,
  `cantidad` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`pedido`) REFERENCES `pedido`(`id`) ,
  FOREIGN KEY (`producto`) REFERENCES `camiseta`(`id`)
); 


INSERT INTO `usuario` (`id`, `username`, `password`, `email`, `telefono`, `direccion`, `activo`, `tipo`) VALUES
(1, 'pepe', '$2a$10$czRAAeghw4UntjfS4etcgOfCwXypg/PRC.MmfadS8qN1PRWlEJiI2', 'juansinmiedo@sincorreo.com', '555123456',  'Paseo de la Estación 44, 23008, JAEN', 1,  'OPERADOR'),
(13,  'pepito', '$2b$10$.zbmFav.W7ZYXvuNerc4J.Saygg9JRE2mEudBrB1Cy71DwbBkJxcG', 'pepe@sinninguncorreo.com', '123-456-789',  'Paseo de la Estación 44, 23008, JAEN', 1,  'CLIENTE');


INSERT INTO `categoria` (`id`, `nombre`, `descripcion`, `padre_id`) VALUES
(1, 'Deporte', 'Todas las camisetas de estilo deportivo para entrenamiento y rendimiento.', NULL),
(2, 'Casual', 'Camisetas diseñadas para el uso diario y la moda casual.', NULL),
(3, 'Música', 'Camisetas con diseños de grupos de música, conciertos y álbumes.', NULL),
(4, 'Fútbol', 'Camisetas de equipos de fútbol de ligas internacionales.', 1),
(5, 'Fitness', 'Camisetas técnicas y transpirables para correr y gimnasio.', 1),
(6, 'Básicas', 'Camisetas de algodón con cuello redondo.', 2),
(7, 'Cuello V', 'Camisetas con cuello en V, ideales para un look relajado.', 2),
(8, 'Rock/Metal', 'Merchandising oficial de bandas de Rock y Metal.', 3),
(9, 'Hip Hop', 'Camisetas con artistas y diseños de Hip Hop y R&B.', 3);
-- Aseguramos que el auto-incremento continúe después del último valor
ALTER TABLE `categoria` AUTO_INCREMENT = 10;


INSERT INTO `camiseta` (`id`, `talla`, `sexo`, `color`, `marca`, `stock`, `precio`, `activo`, `categoria_id`, `nombre`, `descripcion`) VALUES
(1, 'xs', 'chico',  '#e01b24',  'Adidas', 3,  20.00,  1, 4, 'Camiseta Roja', NULL),
(2, 'xs', 'chico',  '#a0aba4',  'Puma', 10, 15.00,  0, 4, 'Camiseta Gris', NULL),
(3, 's',  'unisex', '#000000',  'Puma', 3,  1.20, 1, 1, 'Camiseta Negra', NULL),
(4, 'xxs',  'nina', '#ed333b',  'Puma', 12, 12.00,  1, 1, 'Camiseta Niña Roja', NULL),
(5, 'xxl',  'nino', '#f9f06b',  'Adidas', 12, 34.00,  1, 5, 'Camiseta Niño Amarilla', NULL),
(14,  'xs', 'unisex', '#e66100',  'Adidas', 3,  6.00, 1, 2, 'Camiseta Naranja', NULL);