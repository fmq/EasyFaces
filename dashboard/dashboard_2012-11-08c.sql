# ************************************************************
# Sequel Pro SQL dump
# Version 3408
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: 127.0.0.1 (MySQL 5.5.28)
# Database: dashboard
# Generation Time: 2012-11-08 17:45:22 +0000
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

CREATE TABLE `ef_chart_series` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `sql_text` varchar(255) DEFAULT NULL,
  `widget_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7EF1D8D65156A27D` (`widget_id`),
  CONSTRAINT `FK7EF1D8D65156A27D` FOREIGN KEY (`widget_id`) REFERENCES `ef_widgets` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table ef_dashboard_column_layout
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ef_dashboard_column_layout`;

CREATE TABLE `ef_dashboard_column_layout` (
  `id` bigint(20) NOT NULL,
  `position` int(11) NOT NULL,
  `type` int(11) DEFAULT NULL,
  `dashboard_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKB1CF002A8C0CFF4A` (`dashboard_id`),
  CONSTRAINT `FKB1CF002A8C0CFF4A` FOREIGN KEY (`dashboard_id`) REFERENCES `ef_dashboard_definition` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table ef_dashboard_definition
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ef_dashboard_definition`;

CREATE TABLE `ef_dashboard_definition` (
  `id` bigint(20) NOT NULL,
  `columns` int(11) NOT NULL,
  `userId` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table ef_widget_instance
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ef_widget_instance`;

CREATE TABLE `ef_widget_instance` (
  `id` bigint(20) NOT NULL,
  `pos` int(11) NOT NULL,
  `widget_id` bigint(20) DEFAULT NULL,
  `dashboard_layout_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKA8F9B892EC2F7796` (`dashboard_layout_id`),
  KEY `FKA8F9B8925156A27D` (`widget_id`),
  CONSTRAINT `FKA8F9B8925156A27D` FOREIGN KEY (`widget_id`) REFERENCES `ef_widgets` (`id`),
  CONSTRAINT `FKA8F9B892EC2F7796` FOREIGN KEY (`dashboard_layout_id`) REFERENCES `ef_dashboard_column_layout` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table ef_widgets
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ef_widgets`;

CREATE TABLE `ef_widgets` (
  `id` bigint(20) NOT NULL,
  `body_style_class` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `header_style_class` varchar(255) DEFAULT NULL,
  `length` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `sql_text` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `style_class` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `width` int(11) NOT NULL,
  `height` int(11) NOT NULL,
  `widget_type` varchar(255) DEFAULT NULL,
  `additional_data` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table hibernate_sequence
# ------------------------------------------------------------

DROP TABLE IF EXISTS `hibernate_sequence`;

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



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
