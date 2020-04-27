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

 Date: 02/03/2020 14:43:27
 
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_activs
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_activs`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '热门活动表id',
  `activ_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '活动名',
  `activs_count` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '该活动下的视频数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_admin_auths
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_admin_auths`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '管理员授权登录表id',
  `account` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '管理员帐号',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '管理员密码',
  `last_login_date` bigint(255) UNSIGNED NULL DEFAULT NULL COMMENT '最近登录时间',
  `admin_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '管理员信息id',
  `pwd_last_set` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码是否需要修改（1：不需要；0：需要）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `admin_id`(`admin_id`) USING BTREE,
  CONSTRAINT `admin_id` FOREIGN KEY (`admin_id`) REFERENCES `tb_admin_info` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_admin_info
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_admin_info`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '管理员信息表id',
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '管理员昵称，和帐号相同',
  `sys_auth_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '系统权限id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `sys_auth_id`(`sys_auth_id`) USING BTREE,
  CONSTRAINT `sys_auth_id` FOREIGN KEY (`sys_auth_id`) REFERENCES `tb_sys_auths` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_bgms
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_bgms`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '背景音乐信息表id',
  `albumid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '封面图mid',
  `mid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'bgm接口mid',
  `bgm_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '音乐名',
  `singer` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '演唱者',
  `bgm_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '音频本地链接',
  `bgm_cover` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '封面图',
  `bgm_seconds` bigint(255) UNSIGNED NULL DEFAULT 0 COMMENT '音频长度（毫秒）',
  `cited` int(255) NULL DEFAULT NULL COMMENT '音乐被使用次数',
  `status` int(11) NULL DEFAULT NULL COMMENT '音乐用户是否可检索（0：不可；1：可）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_comments
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_comments`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '短视频评论表id',
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '评论内容',
  `created_at` bigint(255) UNSIGNED NULL DEFAULT NULL COMMENT '评论时间',
  `created_by` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '评论用户id',
  `video_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '被评论短视频id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `created_by_comment`(`created_by`) USING BTREE,
  INDEX `video_id_comment`(`video_id`) USING BTREE,
  CONSTRAINT `created_by_comment` FOREIGN KEY (`created_by`) REFERENCES `tb_user_info` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `video_id_comment` FOREIGN KEY (`video_id`) REFERENCES `tb_videos` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_danmaku
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_danmaku`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '短视频弹幕表',
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '弹幕内容',
  `created_at` bigint(255) UNSIGNED NULL DEFAULT NULL COMMENT '弹幕创建时间',
  `replayed_at` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '弹幕回放时间（秒）',
  `created_by` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '评论用户id',
  `video_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '被评论短视频id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `created_by_danmaku`(`created_by`) USING BTREE,
  INDEX `video_id_danmaku`(`video_id`) USING BTREE,
  CONSTRAINT `created_by_danmaku` FOREIGN KEY (`created_by`) REFERENCES `tb_user_info` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `video_id_danmaku` FOREIGN KEY (`video_id`) REFERENCES `tb_videos` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_message_type
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_message_type`  (
  `id` int(11) UNSIGNED NOT NULL COMMENT '消息类型表id',
  `type_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_messages
-- ----------------------------
CREATE TABLE IF NOT EXISTS`tb_messages`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户消息表id',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消息标题',
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消息内容',
  `created_at` bigint(255) NULL DEFAULT NULL COMMENT '消息创建时间',
  `created_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消息创建者',
  `extra_link` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消息附带链接',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消息状态（是否被阅读，0：已阅读 1：未阅读）',
  `user_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '关联用户',
  `type_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '消息类型',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `type_id`(`type_id`) USING BTREE,
  INDEX `user_id_message`(`user_id`) USING BTREE,
  CONSTRAINT `type_id` FOREIGN KEY (`type_id`) REFERENCES `tb_message_type` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `user_id_message` FOREIGN KEY (`user_id`) REFERENCES `tb_user_info` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_stat_info
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_stat_info`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '统计数据总表id',
  `value` double NULL DEFAULT NULL COMMENT '数值',
  `update_date` bigint(255) UNSIGNED NULL DEFAULT NULL COMMENT '更新时间',
  `type_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '统计数据类型id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `type_id_stat`(`type_id`) USING BTREE,
  CONSTRAINT `type_id_stat` FOREIGN KEY (`type_id`) REFERENCES `tb_stat_type` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_stat_type
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_stat_type`  (
  `id` int(11) UNSIGNED NOT NULL COMMENT '统计数据类型表id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_sys_auths
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `tb_sys_auths`  (
  `id` int(11) UNSIGNED NOT NULL COMMENT '系统权限表id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '系统权限名（1：超级管理员 2：系统管理员）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_user_auths
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_user_auths`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户授权登录表id',
  `openid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `credential` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微信token（密码）',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '登录状态，1：已登录 0：未登录',
  `last_login_date` bigint(20) NULL DEFAULT NULL COMMENT '上次离开时间',
  `user_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '用户基本信息id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id_auth`(`user_id`) USING BTREE,
  CONSTRAINT `user_id_auth` FOREIGN KEY (`user_id`) REFERENCES `tb_user_info` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_user_blacklist
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_user_blacklist`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户黑名单id',
  `user_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '某用户id',
  `black_user_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '被列入黑名单的用户id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id_black`(`user_id`) USING BTREE,
  INDEX `black_user_id_black`(`black_user_id`) USING BTREE,
  CONSTRAINT `black_user_id_black` FOREIGN KEY (`black_user_id`) REFERENCES `tb_user_info` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_id_black` FOREIGN KEY (`user_id`) REFERENCES `tb_user_info` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_user_collected_videos
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_user_collected_videos`  (
  `id` bigint(255) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户收藏短视频表id',
  `user_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '点赞用户id',
  `video_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '被收藏的短视频id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id_collected`(`user_id`) USING BTREE,
  INDEX `video_id_collected`(`video_id`) USING BTREE,
  CONSTRAINT `user_id_collected` FOREIGN KEY (`user_id`) REFERENCES `tb_user_info` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `video_id_collected` FOREIGN KEY (`video_id`) REFERENCES `tb_videos` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_user_fans
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_user_fans`  (
  `id` bigint(255) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户粉丝表id',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '粉丝状态（0：只是粉丝 1：互相关注）',
  `followed_date` bigint(255) UNSIGNED NULL DEFAULT NULL COMMENT '关注时间',
  `user_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '被关注用户id',
  `fans_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '粉丝用户id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id_follow`(`user_id`) USING BTREE,
  INDEX `followed_user_follow`(`fans_id`) USING BTREE,
  CONSTRAINT `fans_id_fans` FOREIGN KEY (`fans_id`) REFERENCES `tb_user_info` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_id_fans` FOREIGN KEY (`user_id`) REFERENCES `tb_user_info` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_user_followers
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_user_followers`  (
  `id` bigint(255) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户关注表id',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关注状态（0：单方面关注 1：互相关注）',
  `followed_date` bigint(255) UNSIGNED NULL DEFAULT NULL COMMENT '关注时间',
  `user_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '主动关注用户id',
  `followed_user` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '被关注用户id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id_follow`(`user_id`) USING BTREE,
  INDEX `followed_user_follow`(`followed_user`) USING BTREE,
  CONSTRAINT `followed_user_follow` FOREIGN KEY (`followed_user`) REFERENCES `tb_user_info` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_id_follow` FOREIGN KEY (`user_id`) REFERENCES `tb_user_info` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_user_info
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_user_info`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户基本信息表id',
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `gender` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户性别，0：未知、1：男、2：女',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户头像',
  `country` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '国家',
  `province` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '省',
  `city` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '城市',
  `registered_date` bigint(255) NULL DEFAULT NULL COMMENT '注册时间',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '用户状态，0：正常 1：封禁',
  `openid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,			 
  PRIMARY KEY (`id`) USING BTREE,
    INDEX `registe_time`(`registered_date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_user_reports
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_user_reports`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户举报表id',
  `report_content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '举报内容',
  `created_at` bigint(255) UNSIGNED NULL DEFAULT NULL COMMENT '举报时间',
  `created_by` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '举报用户',
  `video_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '被举报短视频',
  `result` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '处理结果（0：未处理 1：已处理）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `created_by_report`(`created_by`) USING BTREE,
  INDEX `video_id_report`(`video_id`) USING BTREE,
  CONSTRAINT `created_by_report` FOREIGN KEY (`created_by`) REFERENCES `tb_user_info` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `video_id_report` FOREIGN KEY (`video_id`) REFERENCES `tb_videos` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_user_stat
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_user_stat`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户数据表id',
  `followers_count` int(11) UNSIGNED NULL DEFAULT 0 COMMENT '关注数',
  `fans_count` int(11) UNSIGNED NULL DEFAULT 0 COMMENT '粉丝数',
  `works_count` int(11) UNSIGNED NULL DEFAULT 0 COMMENT '作品数',
  `received_liked_count` int(11) NULL DEFAULT 0 COMMENT '点赞数',
  `user_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '用户基本信息id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id_stat`(`user_id`) USING BTREE,
  CONSTRAINT `user_id_stat` FOREIGN KEY (`user_id`) REFERENCES `tb_user_info` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_video_stat
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_video_stat`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '短视频数据表id',
  `liked_count` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '点赞数',
  `comment_count` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '评论数',
  `favored_count` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '收藏数',
  `shared_count` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '转发数',
  `video_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '短视频信息id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `video_id_stat`(`video_id`) USING BTREE,
  CONSTRAINT `video_id_stat` FOREIGN KEY (`video_id`) REFERENCES `tb_videos` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_video_status
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_video_status`  (
  `id` int(11) UNSIGNED NOT NULL COMMENT '短视频状态表id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态名（1：正常 2：被举报 3：封禁 4：待审核）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_videos
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tb_videos`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '短视频信息表id',
  `video_title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `cover_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '封面图路径',
  `video_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述文字',
  `created_at` bigint(255) NULL DEFAULT NULL COMMENT '上传时间',
  `video_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '视频链接',
  `video_seconds` bigint(255) UNSIGNED NULL DEFAULT 0 COMMENT '短视频长度',
  `video_width` double NULL DEFAULT NULL COMMENT '短视频宽度',
  `video_height` double NULL DEFAULT NULL COMMENT '短视频高度',
  `video_size` double NULL DEFAULT NULL COMMENT '短视频大小（KB）',
  `created_by` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '上传用户id',
  `status_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '短视频状态id',
  `activ_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '热门活动id',
  `bgm_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '背景音乐id',
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

SET FOREIGN_KEY_CHECKS = 1;
