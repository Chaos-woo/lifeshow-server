/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80002
 Source Host           : localhost:3306
 Source Schema         : life_show

 Target Server Type    : MySQL
 Target Server Version : 80002
 File Encoding         : 65001

 Date: 02/03/2020 19:21:55
 
 * 初始化顺序：
 * 初始化系统权限
 * 初始化超级管理员
 * 初始化短视频状态类型
 * 初始化用户消息类型
 * 初始化统计数据类型
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_sys_auths
-- ----------------------------
/* DROP TABLE IF EXISTS `tb_sys_auths`;
CREATE TABLE `tb_sys_auths`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '系统权限表id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '系统权限名（1：超级管理员 2：系统管理员）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic; */

-- ----------------------------
-- Records of tb_sys_auths
-- ----------------------------
INSERT IGNORE INTO `tb_sys_auths` VALUES (1, '超级管理员');
INSERT IGNORE INTO `tb_sys_auths` VALUES (2, '系统管理员');

-- ----------------------------
-- Table structure for tb_admin_info
-- ----------------------------
/* DROP TABLE IF EXISTS `tb_admin_info`;
CREATE TABLE `tb_admin_info`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '管理员信息表id',
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '管理员昵称，和帐号相同',
  `sys_auth_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '系统权限id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `sys_auth_id`(`sys_auth_id`) USING BTREE,
  CONSTRAINT `sys_auth_id` FOREIGN KEY (`sys_auth_id`) REFERENCES `tb_sys_auths` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic; */

-- ----------------------------
-- Records of tb_admin_info
-- ----------------------------
INSERT IGNORE INTO `tb_admin_info` VALUES (1, 'admin', 1);

-- ----------------------------
-- Table structure for tb_admin_auths
-- ----------------------------
/* DROP TABLE IF EXISTS `tb_admin_auths`;
CREATE TABLE `tb_admin_auths`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '管理员授权登录表id',
  `account` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '管理员帐号',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '管理员密码',
  `last_login_date` bigint(255) UNSIGNED NULL DEFAULT NULL COMMENT '最近登录时间',
  `admin_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '管理员信息id',
  `pwd_last_set` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码是否需要修改（1：不需要；0：需要）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `admin_id`(`admin_id`) USING BTREE,
  CONSTRAINT `admin_id` FOREIGN KEY (`admin_id`) REFERENCES `tb_admin_info` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic; */

-- ----------------------------
-- Records of tb_admin_auths
-- ----------------------------
INSERT IGNORE INTO `tb_admin_auths` VALUES (1, 'admin', '91400406ce4677e88c29989ad52393892038e95b3610bc08', NULL, 1, '0');

-- ----------------------------
-- Table structure for tb_operate_log_type
-- ----------------------------
/* DROP TABLE IF EXISTS `tb_operate_log_type`;
CREATE TABLE `tb_operate_log_type`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '管理员操作日志类型表id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic; */

-- ----------------------------
-- Table structure for tb_activs
-- ----------------------------
/* DROP TABLE IF EXISTS `tb_activs`;
CREATE TABLE `tb_activs`  (
  `id` int unsigned NOT NULL COMMENT '热门活动表id',
  `activ_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '活动名',
  `activs_count` int unsigned NULL COMMENT '该活动下的视频数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;
*/

-- ----------------------------
-- Records of tb_activs
-- ----------------------------
INSERT IGNORE INTO `tb_activs` VALUES (1, ' 生活挑战', 1);

-- ----------------------------
-- Table structure for tb_bgms
-- ----------------------------
/* DROP TABLE IF EXISTS `tb_bgms`;
CREATE TABLE `tb_bgms`  (
  `id` int unsigned NOT NULL COMMENT '背景音乐信息表id',
  `albumid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '封面图mid',
  `mid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'bgm接口mid',
  `bgm_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '音乐名',
  `singer` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '演唱者',
  `bgm_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '音频本地链接',
  `bgm_cover` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '封面图',
  `bgm_seconds` bigint unsigned NULL COMMENT '音频长度（毫秒）',
  `cited` int(0) NULL DEFAULT NULL COMMENT '音乐被使用次数',
  `status` int(0) NULL DEFAULT NULL COMMENT '音乐用户是否可检索（0：不可；1：可）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;
*/

-- ----------------------------
-- Records of tb_bgms
-- ----------------------------
INSERT IGNORE INTO `tb_bgms` VALUES (1, '1549054', 'xx', 'no-sound', '', '', '', 0, 0, 0);

-- ----------------------------
-- Table structure for tb_video_status
-- ----------------------------
/* DROP TABLE IF EXISTS `tb_video_status`;
CREATE TABLE `tb_video_status`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '短视频状态表id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态名（1：正常 2：被举报 3：封禁 4：待审核）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic; */

-- ----------------------------
-- Records of tb_video_status
-- ----------------------------
INSERT IGNORE INTO `tb_video_status` VALUES (1, '正常');
INSERT IGNORE INTO `tb_video_status` VALUES (2, '被举报');
INSERT IGNORE INTO `tb_video_status` VALUES (3, '封禁');
INSERT IGNORE INTO `tb_video_status` VALUES (4, '待审核');

-- ----------------------------
-- Table structure for tb_message_type
-- ----------------------------
/* DROP TABLE IF EXISTS `tb_message_type`;
CREATE TABLE `tb_message_type`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '消息类型表id',
  `type_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic; */

-- ----------------------------
-- Records of tb_message_type
-- ----------------------------
INSERT IGNORE INTO `tb_message_type` VALUES (1, '短视频封禁');
INSERT IGNORE INTO `tb_message_type` VALUES (2, '短视频删除');
INSERT IGNORE INTO `tb_message_type` VALUES (3, '短视频待审核');
INSERT IGNORE INTO `tb_message_type` VALUES (4, '用户封禁');
INSERT IGNORE INTO `tb_message_type` VALUES (5, '用户删除');

-- ----------------------------
-- Table structure for tb_stat_type
-- ----------------------------
/* DROP TABLE IF EXISTS `tb_stat_type`;
CREATE TABLE `tb_stat_type`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '统计数据类型表id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic; */

-- ----------------------------
-- Records of tb_stat_type
-- ----------------------------
INSERT IGNORE INTO `tb_stat_type` VALUES (1, '七日注册用户变化');
INSERT IGNORE INTO `tb_stat_type` VALUES (2, '七日短视频增长变化');
INSERT IGNORE INTO `tb_stat_type` VALUES (3, '小程序访问量');

-- ----------------------------
-- Table structure for tb_user_info
-- ----------------------------
/* DROP TABLE IF EXISTS `tb_user_info`;
CREATE TABLE `tb_user_info`  (
  `id` int unsigned NOT NULL COMMENT '用户基本信息表id',
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `gender` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户性别，0：未知、1：男、2：女',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户头像',
  `country` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '国家',
  `province` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '省',
  `city` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '城市',
  `registered_date` bigint(0) NULL DEFAULT NULL COMMENT '注册时间',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '用户状态，0：正常 1：封禁',
  `openid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `registe_time`(`registered_date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;
*/

-- ----------------------------
-- Records of tb_user_info
-- ----------------------------
INSERT IGNORE INTO `tb_user_info` VALUES (1, 'nickname', '1', '', ' ', ' ', ' ', 0, '1', 'xx');

-- ----------------------------
-- Table structure for tb_user_auths
-- ----------------------------
/* DROP TABLE IF EXISTS `tb_user_auths`;
CREATE TABLE `tb_user_auths`  (
  `id` int unsigned NOT NULL COMMENT '用户授权登录表id',
  `openid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `credential` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微信token（密码）',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '登录状态，1：已登录 0：未登录',
  `last_login_date` bigint(0) NULL DEFAULT NULL COMMENT '上次离开时间',
  `user_id` int unsigned NULL COMMENT '用户基本信息id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id_auth`(`user_id`) USING BTREE,
  CONSTRAINT `user_id_auth` FOREIGN KEY (`user_id`) REFERENCES `tb_user_info` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;
*/

-- ----------------------------
-- Records of tb_user_auths
-- ----------------------------
INSERT IGNORE INTO `tb_user_auths` VALUES (1, 'xx', 'xx', '0', 0, 1);

-- ----------------------------
-- Table structure for tb_user_stat
-- ----------------------------
/*DROP TABLE IF EXISTS `tb_user_stat`;
CREATE TABLE `tb_user_stat`  (
  `id` int unsigned NOT NULL COMMENT '用户数据表id',
  `followers_count` int unsigned NULL COMMENT '关注数',
  `fans_count` int unsigned NULL COMMENT '粉丝数',
  `works_count` int unsigned NULL COMMENT '作品数',
  `received_liked_count` int(0) NULL DEFAULT 0 COMMENT '点赞数',
  `user_id` int unsigned NULL COMMENT '用户基本信息id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id_stat`(`user_id`) USING BTREE,
  CONSTRAINT `user_id_stat` FOREIGN KEY (`user_id`) REFERENCES `tb_user_info` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;
*/

-- ----------------------------
-- Records of tb_user_stat
-- ----------------------------
INSERT IGNORE INTO `tb_user_stat` VALUES (1, 0, 0, 0, 0, 1);

-- ----------------------------
-- Table structure for tb_videos
-- ----------------------------
/*DROP TABLE IF EXISTS `tb_videos`;
CREATE TABLE `tb_videos`  (
  `id` int unsigned NOT NULL COMMENT '短视频信息表id',
  `video_title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `cover_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '封面图路径',
  `video_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述文字',
  `created_at` bigint(0) NULL DEFAULT NULL COMMENT '上传时间',
  `video_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '视频链接',
  `video_seconds` bigint unsigned NULL COMMENT '短视频长度',
  `video_width` double NULL DEFAULT NULL COMMENT '短视频宽度',
  `video_height` double NULL DEFAULT NULL COMMENT '短视频高度',
  `video_size` double NULL DEFAULT NULL COMMENT '短视频大小（KB）',
  `created_by` int unsigned NULL COMMENT '上传用户id',
  `status_id` int unsigned NULL COMMENT '短视频状态id',
  `activ_id` int unsigned NULL COMMENT '热门活动id',
  `bgm_id` int unsigned NULL COMMENT '背景音乐id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `created_by_user`(`created_by`) USING BTREE,
  INDEX `status_id`(`status_id`) USING BTREE,
  INDEX `activ_id`(`activ_id`) USING BTREE,
  INDEX `bgm_id`(`bgm_id`) USING BTREE,
  INDEX `publish_time`(`created_at`) USING BTREE,
  CONSTRAINT `activ_id` FOREIGN KEY (`activ_id`) REFERENCES `tb_activs` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `bgm_id` FOREIGN KEY (`bgm_id`) REFERENCES `tb_bgms` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `created_by_user` FOREIGN KEY (`created_by`) REFERENCES `tb_user_info` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `status_id` FOREIGN KEY (`status_id`) REFERENCES `tb_video_status` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;
*/

-- ----------------------------
-- Records of tb_videos
-- ----------------------------
INSERT IGNORE INTO `tb_videos` VALUES (1, '模拟发布视频的第一条数据', '/root/lifeshow/video/cover/1.jpg', '模拟发布视频的第一条数据', 1587031961247, '/root/lifeshow/video/video/1.mp4', 0, 0, 0, 0, 1, 1, 1, 1);

-- ----------------------------
-- Table structure for tb_video_stat
-- ----------------------------
/*DROP TABLE IF EXISTS `tb_video_stat`;
CREATE TABLE `tb_video_stat`  (
  `id` int unsigned NOT NULL COMMENT '短视频数据表id',
  `liked_count` int unsigned NULL COMMENT '点赞数',
  `comment_count` int unsigned NULL COMMENT '评论数',
  `favored_count` int unsigned NULL COMMENT '收藏数',
  `shared_count` int unsigned NULL COMMENT '转发数',
  `video_id` int unsigned NULL COMMENT '短视频信息id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `video_id_stat`(`video_id`) USING BTREE,
  CONSTRAINT `video_id_stat` FOREIGN KEY (`video_id`) REFERENCES `tb_videos` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;
*/

-- ----------------------------
-- Records of tb_video_stat
-- ----------------------------
INSERT IGNORE INTO `tb_video_stat` VALUES (1, 0, 0, 0, 0, 1);

SET FOREIGN_KEY_CHECKS = 1;
