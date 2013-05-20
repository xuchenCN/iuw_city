/*
SQLyog Community Edition- MySQL GUI v8.14 
MySQL - 5.5.12 
*********************************************************************
*/
/*!40101 SET NAMES utf8 */;

CREATE TABLE `user_info` (
   `id` double DEFAULT NULL,
   `name` varchar(768) DEFAULT NULL,
   `age` double DEFAULT NULL,
   `sex` tinyint(2) DEFAULT NULL,
   `create_time` datetime DEFAULT NULL,
   `last_login_date` datetime DEFAULT NULL,
   `status` tinyint(2) DEFAULT NULL,
   `head_img` varchar(255) DEFAULT NULL
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8

CREATE TABLE `feed` (
   `id` int(11) NOT NULL,
   `user_id` int(11) DEFAULT NULL,
   `content` mediumtext,
   `status` tinyint(2) DEFAULT NULL,
   `create_time` datetime DEFAULT NULL,
   `relation` varchar(100) DEFAULT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8
 
 CREATE TABLE `feed_reply` (
   `id` int(11) NOT NULL,
   `user_id` int(11) DEFAULT NULL,
   `feed_id` int(11) DEFAULT NULL,
   `status` tinyint(2) DEFAULT NULL,
   `content` mediumtext,
   `create_time` datetime DEFAULT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8