# ************************************************************
# Sequel Pro SQL dump
# Version 3408
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: 127.0.0.1 (MySQL 5.5.28)
# Database: dashboard
# Generation Time: 2012-11-08 17:39:28 +0000
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

DROP TABLE IF EXISTS `chart_data`;

CREATE TABLE `chart_data` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `chart_id` int(11) DEFAULT NULL,
  `x` bigint(20) DEFAULT NULL,
  `y` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;



# Dump of table ef_chart_series
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ef_chart_series`;



# Dump of table ef_dashboard_column_layout
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ef_dashboard_column_layout`;



# Dump of table ef_dashboard_definition
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ef_dashboard_definition`;



# Dump of table ef_widget_instance
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ef_widget_instance`;



# Dump of table ef_widgets
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ef_widgets`;



# Dump of table hibernate_sequence
# ------------------------------------------------------------

DROP TABLE IF EXISTS `hibernate_sequence`;



# Dump of table table_data
# ------------------------------------------------------------

DROP TABLE IF EXISTS `table_data`;

CREATE TABLE `table_data` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `col1` varchar(255) DEFAULT NULL,
  `col2` varchar(255) DEFAULT NULL,
  `col3` varchar(255) DEFAULT NULL,
  `col4` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;



# Dump of table temp_data
# ------------------------------------------------------------

DROP TABLE IF EXISTS `temp_data`;

CREATE TABLE `temp_data` (
  `label` varchar(200) DEFAULT NULL,
  `value` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
