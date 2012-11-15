# ************************************************************
# Sequel Pro SQL dump
# Version 3408
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: 127.0.0.1 (MySQL 5.5.28)
# Database: dashboard
# Generation Time: 2012-11-08 17:35:53 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table chart_data
# ------------------------------------------------------------

LOCK TABLES `chart_data` WRITE;
/*!40000 ALTER TABLE `chart_data` DISABLE KEYS */;

INSERT INTO `chart_data` (`id`, `chart_id`, `x`, `y`)
VALUES
	(1,10,0,3),
	(2,10,1,4),
	(3,10,2,2),
	(4,10,3,4),
	(5,10,4,3),
	(6,20,0,2),
	(7,20,1,3),
	(8,20,2,4),
	(9,20,3,3),
	(10,20,4,2),
	(11,20,5,3);

/*!40000 ALTER TABLE `chart_data` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table ef_chart_series
# ------------------------------------------------------------

LOCK TABLES `ef_chart_series` WRITE;
/*!40000 ALTER TABLE `ef_chart_series` DISABLE KEYS */;

INSERT INTO `ef_chart_series` (`id`, `name`, `sql_text`, `widget_id`)
VALUES
	(1,'First Series','select x , y from chart_data where chart_id = 10',2),
	(2,'Second Series','select x , y from chart_data where chart_id = 20',2);

/*!40000 ALTER TABLE `ef_chart_series` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table ef_dashboard_column_layout
# ------------------------------------------------------------

LOCK TABLES `ef_dashboard_column_layout` WRITE;
/*!40000 ALTER TABLE `ef_dashboard_column_layout` DISABLE KEYS */;

INSERT INTO `ef_dashboard_column_layout` (`id`, `position`, `type`, `dashboard_id`)
VALUES
	(1,1,1,0),
	(2,2,0,0),
	(3,3,0,0);

/*!40000 ALTER TABLE `ef_dashboard_column_layout` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table ef_dashboard_definition
# ------------------------------------------------------------

LOCK TABLES `ef_dashboard_definition` WRITE;
/*!40000 ALTER TABLE `ef_dashboard_definition` DISABLE KEYS */;

INSERT INTO `ef_dashboard_definition` (`id`, `columns`, `userId`)
VALUES
	(0,2,'fmq');

/*!40000 ALTER TABLE `ef_dashboard_definition` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table ef_widget_instance
# ------------------------------------------------------------

LOCK TABLES `ef_widget_instance` WRITE;
/*!40000 ALTER TABLE `ef_widget_instance` DISABLE KEYS */;

INSERT INTO `ef_widget_instance` (`id`, `pos`, `widget_id`, `dashboard_layout_id`)
VALUES
	(10,0,2,1),
	(20,0,0,2),
	(30,0,3,3),
	(40,1,1,2);

/*!40000 ALTER TABLE `ef_widget_instance` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table ef_widgets
# ------------------------------------------------------------

LOCK TABLES `ef_widgets` WRITE;
/*!40000 ALTER TABLE `ef_widgets` DISABLE KEYS */;

INSERT INTO `ef_widgets` (`id`, `body_style_class`, `description`, `header_style_class`, `length`, `name`, `sql_text`, `status`, `style_class`, `title`, `type`, `width`, `height`, `widget_type`, `additional_data`)
VALUES
	(0,NULL,NULL,NULL,300,'Pie','select label, value from temp_data where value < 1000',1,NULL,'a pie chart',1,500,200,'PIE',NULL),
	(1,NULL,NULL,NULL,200,'Pie','select label, value from temp_data where value > 1000',1,NULL,'Another pie chart',1,500,200,'PIE',NULL),
	(2,NULL,NULL,NULL,100,'Chart','select x , y from chart_data',1,NULL,'Chart',NULL,1000,200,'CHART',NULL),
	(3,NULL,NULL,NULL,0,'Table','select col1, col2, col3, col4 from table_data',1,NULL,'Table',NULL,500,200,'TABLE','Columna 0,Columna 1,Columna 2,Columna 3');

/*!40000 ALTER TABLE `ef_widgets` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table hibernate_sequence
# ------------------------------------------------------------

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;

INSERT INTO `hibernate_sequence` (`next_val`)
VALUES
	(1);

/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table table_data
# ------------------------------------------------------------

LOCK TABLES `table_data` WRITE;
/*!40000 ALTER TABLE `table_data` DISABLE KEYS */;

INSERT INTO `table_data` (`id`, `col1`, `col2`, `col3`, `col4`)
VALUES
	(1,'Columna1 ','Otra','cosa','aca'),
	(2,'Otra','mas','pero','no'),
	(3,'la','ultima','esta','si');

/*!40000 ALTER TABLE `table_data` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table temp_data
# ------------------------------------------------------------

LOCK TABLES `temp_data` WRITE;
/*!40000 ALTER TABLE `temp_data` DISABLE KEYS */;

INSERT INTO `temp_data` (`label`, `value`)
VALUES
	('Uno',100),
	('Dos',130),
	('Tres',110),
	('Foo',1100),
	('Bar',2100),
	('Zoo',1100),
	('Lander',2430);

/*!40000 ALTER TABLE `temp_data` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
