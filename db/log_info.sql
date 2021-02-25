/*
Navicat MySQL Data Transfer

Source Server         : vmware
Source Server Version : 50651
Source Host           : 192.168.23.84:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50651
File Encoding         : 65001

Date: 2021-02-22 14:33:55
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for log_info
-- ----------------------------
DROP TABLE IF EXISTS `log_info`;
CREATE TABLE `log_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `trace_id` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL,
  `request_ip` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL,
  `request_param` varchar(2048) COLLATE utf8mb4_bin DEFAULT NULL,
  `request_method` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL,
  `request_time` bigint(40) DEFAULT NULL,
  `response_param` varchar(2048) COLLATE utf8mb4_bin DEFAULT NULL,
  `response_time` bigint(40) DEFAULT NULL,
  `time_consuming` bigint(40) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
