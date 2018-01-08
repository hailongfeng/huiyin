/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50556
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50556
File Encoding         : 65001

Date: 2017-09-04 14:03:21
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `menu`
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `link` varchar(255) DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `icon_css` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES ('1', '河南省', '#', '0', 'icon-desktop');
INSERT INTO `menu` VALUES ('2', '郑州市', '#', '1', 'icon-desktop');
INSERT INTO `menu` VALUES ('3', '许昌市', '#', '1', 'icon-desktop');
INSERT INTO `menu` VALUES ('4', '长葛县', '#', '3', 'icon-desktop');
INSERT INTO `menu` VALUES ('5', '禹州县', '#', '3', 'icon-desktop');
INSERT INTO `menu` VALUES ('6', '北京', '/springmvchibernate_menu/menu/add', '0', 'icon-desktop');
INSERT INTO `menu` VALUES ('7', '洛阳市', '#', '1', 'icon-desktop');
INSERT INTO `menu` VALUES ('8', '平顶山市', '#', '1', null);
INSERT INTO `menu` VALUES ('9', '江苏省', '#', '0', null);
INSERT INTO `menu` VALUES ('10', '无锡市', '#', '9', null);

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', '张三', '20', '15988887653', '无锡惠山。');
INSERT INTO `user` VALUES ('2', '马云', '45', '18765398876', '杭州');

-- ----------------------------
-- Table structure for `user_t`
-- ----------------------------
DROP TABLE IF EXISTS `user_t`;
CREATE TABLE `user_t` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of user_t
-- ----------------------------
INSERT INTO `user_t` VALUES ('1', 'bbbb', '26', '22222');
INSERT INTO `user_t` VALUES ('2', 'aaa', '10', '111');
INSERT INTO `user_t` VALUES ('3', 'name0', '10', 'p0');
INSERT INTO `user_t` VALUES ('4', 'name1', '10', 'p1');
INSERT INTO `user_t` VALUES ('5', 'name2', '10', 'p2');
INSERT INTO `user_t` VALUES ('6', 'name3', '10', 'p3');
INSERT INTO `user_t` VALUES ('7', 'name4', '10', 'p4');
INSERT INTO `user_t` VALUES ('8', 'name5', '10', 'p5');
INSERT INTO `user_t` VALUES ('9', 'name6', '10', 'p6');
INSERT INTO `user_t` VALUES ('10', 'name7', '10', 'p7');
INSERT INTO `user_t` VALUES ('11', 'name8', '10', 'p8');
INSERT INTO `user_t` VALUES ('12', 'name9', '10', 'p9');
INSERT INTO `user_t` VALUES ('13', 'name10', '10', 'p10');
INSERT INTO `user_t` VALUES ('14', 'name11', '10', 'p11');
INSERT INTO `user_t` VALUES ('15', 'name12', '10', 'p12');
INSERT INTO `user_t` VALUES ('16', 'name13', '10', 'p13');
INSERT INTO `user_t` VALUES ('17', 'name14', '10', 'p14');
INSERT INTO `user_t` VALUES ('18', 'name15', '10', 'p15');
INSERT INTO `user_t` VALUES ('19', 'name16', '10', 'p16');
INSERT INTO `user_t` VALUES ('20', 'name17', '10', 'p17');
INSERT INTO `user_t` VALUES ('21', 'name18', '10', 'p18');
INSERT INTO `user_t` VALUES ('22', 'name19', '10', 'p19');
INSERT INTO `user_t` VALUES ('23', 'name20', '10', 'p20');
INSERT INTO `user_t` VALUES ('24', 'name21', '10', 'p21');
INSERT INTO `user_t` VALUES ('25', 'name22', '10', 'p22');
INSERT INTO `user_t` VALUES ('26', 'name23', '10', 'p23');
INSERT INTO `user_t` VALUES ('27', 'name24', '10', 'p24');
INSERT INTO `user_t` VALUES ('28', 'name25', '10', 'p25');
INSERT INTO `user_t` VALUES ('29', 'name26', '10', 'p26');
INSERT INTO `user_t` VALUES ('30', 'name27', '10', 'p27');
INSERT INTO `user_t` VALUES ('31', 'name28', '10', 'p28');
INSERT INTO `user_t` VALUES ('32', 'name29', '10', 'p29');
INSERT INTO `user_t` VALUES ('33', 'name30', '10', 'p30');
INSERT INTO `user_t` VALUES ('34', 'name31', '10', 'p31');
INSERT INTO `user_t` VALUES ('35', 'name32', '10', 'p32');
INSERT INTO `user_t` VALUES ('36', 'name33', '10', 'p33');
INSERT INTO `user_t` VALUES ('37', 'name34', '10', 'p34');
INSERT INTO `user_t` VALUES ('38', 'name35', '10', 'p35');
INSERT INTO `user_t` VALUES ('39', 'name36', '10', 'p36');
INSERT INTO `user_t` VALUES ('40', 'name37', '10', 'p37');
INSERT INTO `user_t` VALUES ('41', 'name38', '10', 'p38');
INSERT INTO `user_t` VALUES ('42', 'name39', '10', 'p39');
INSERT INTO `user_t` VALUES ('43', 'name40', '10', 'p40');
INSERT INTO `user_t` VALUES ('44', 'name41', '10', 'p41');
INSERT INTO `user_t` VALUES ('45', 'name42', '10', 'p42');
INSERT INTO `user_t` VALUES ('46', 'name43', '10', 'p43');
INSERT INTO `user_t` VALUES ('47', 'name44', '10', 'p44');
INSERT INTO `user_t` VALUES ('48', 'name45', '10', 'p45');
INSERT INTO `user_t` VALUES ('49', 'name46', '10', 'p46');
INSERT INTO `user_t` VALUES ('50', 'name47', '10', 'p47');
INSERT INTO `user_t` VALUES ('51', 'name48', '10', 'p48');
INSERT INTO `user_t` VALUES ('52', 'name49', '10', 'p49');
INSERT INTO `user_t` VALUES ('53', 'name50', '10', 'p50');
INSERT INTO `user_t` VALUES ('54', 'name51', '10', 'p51');
INSERT INTO `user_t` VALUES ('55', 'name52', '10', 'p52');
INSERT INTO `user_t` VALUES ('56', 'name53', '10', 'p53');
INSERT INTO `user_t` VALUES ('57', 'name54', '10', 'p54');
INSERT INTO `user_t` VALUES ('58', 'name55', '10', 'p55');
INSERT INTO `user_t` VALUES ('59', 'name56', '10', 'p56');
INSERT INTO `user_t` VALUES ('60', 'name57', '10', 'p57');
INSERT INTO `user_t` VALUES ('61', 'name58', '10', 'p58');
INSERT INTO `user_t` VALUES ('62', 'name59', '10', 'p59');
INSERT INTO `user_t` VALUES ('63', 'name60', '10', 'p60');
INSERT INTO `user_t` VALUES ('64', 'name61', '10', 'p61');
INSERT INTO `user_t` VALUES ('65', 'name62', '10', 'p62');
INSERT INTO `user_t` VALUES ('66', 'name63', '10', 'p63');
INSERT INTO `user_t` VALUES ('67', 'name64', '10', 'p64');
INSERT INTO `user_t` VALUES ('68', 'name65', '10', 'p65');
INSERT INTO `user_t` VALUES ('69', 'name66', '10', 'p66');
INSERT INTO `user_t` VALUES ('70', 'name67', '10', 'p67');
INSERT INTO `user_t` VALUES ('71', 'name68', '10', 'p68');
INSERT INTO `user_t` VALUES ('72', 'name69', '10', 'p69');
INSERT INTO `user_t` VALUES ('73', 'name70', '10', 'p70');
INSERT INTO `user_t` VALUES ('74', 'name71', '10', 'p71');
INSERT INTO `user_t` VALUES ('75', 'name72', '10', 'p72');
INSERT INTO `user_t` VALUES ('76', 'name73', '10', 'p73');
INSERT INTO `user_t` VALUES ('77', 'name74', '10', 'p74');
INSERT INTO `user_t` VALUES ('78', 'name75', '10', 'p75');
INSERT INTO `user_t` VALUES ('79', 'name76', '10', 'p76');
INSERT INTO `user_t` VALUES ('80', 'name77', '10', 'p77');
INSERT INTO `user_t` VALUES ('81', 'name78', '10', 'p78');
INSERT INTO `user_t` VALUES ('82', 'name79', '10', 'p79');
INSERT INTO `user_t` VALUES ('83', 'name80', '10', 'p80');
INSERT INTO `user_t` VALUES ('84', 'name81', '10', 'p81');
INSERT INTO `user_t` VALUES ('85', 'name82', '10', 'p82');
INSERT INTO `user_t` VALUES ('86', 'name83', '10', 'p83');
INSERT INTO `user_t` VALUES ('87', 'name84', '10', 'p84');
INSERT INTO `user_t` VALUES ('88', 'name85', '10', 'p85');
INSERT INTO `user_t` VALUES ('89', 'name86', '10', 'p86');
INSERT INTO `user_t` VALUES ('90', 'name87', '10', 'p87');
INSERT INTO `user_t` VALUES ('91', 'name88', '10', 'p88');
INSERT INTO `user_t` VALUES ('92', 'name89', '10', 'p89');
INSERT INTO `user_t` VALUES ('93', 'name90', '10', 'p90');
INSERT INTO `user_t` VALUES ('94', 'name91', '10', 'p91');
INSERT INTO `user_t` VALUES ('95', 'name92', '10', 'p92');
INSERT INTO `user_t` VALUES ('96', 'name93', '10', 'p93');
INSERT INTO `user_t` VALUES ('97', 'name94', '10', 'p94');
INSERT INTO `user_t` VALUES ('98', 'name95', '10', 'p95');
INSERT INTO `user_t` VALUES ('99', 'name96', '10', 'p96');
INSERT INTO `user_t` VALUES ('100', 'name97', '10', 'p97');
INSERT INTO `user_t` VALUES ('101', 'name98', '10', 'p98');
INSERT INTO `user_t` VALUES ('102', 'name99', '10', 'p99');
