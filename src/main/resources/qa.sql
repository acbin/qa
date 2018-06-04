
#
# Structure for table "comment"
#

DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` text NOT NULL,
  `user_id` int(11) NOT NULL,
  `entity_id` int(11) NOT NULL,
  `entity_type` int(11) NOT NULL,
  `created_date` datetime NOT NULL,
  `status` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `entity_index` (`entity_id`,`entity_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


#
# Structure for table "feed"
#

DROP TABLE IF EXISTS `feed`;
CREATE TABLE `feed` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_date` datetime DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `data` tinytext,
  `type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_index` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


#
# Structure for table "login_ticket"
#

DROP TABLE IF EXISTS `login_ticket`;
CREATE TABLE `login_ticket` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `ticket` varchar(45) NOT NULL,
  `expired` datetime NOT NULL,
  `status` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ticket_unique` (`ticket`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


#
# Structure for table "message"
#

DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `from_id` int(11) DEFAULT NULL,
  `to_id` int(11) DEFAULT NULL,
  `content` text,
  `created_date` datetime DEFAULT NULL,
  `has_read` int(11) DEFAULT NULL,
  `conversation_id` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `conversation_index` (`conversation_id`),
  KEY `created_date` (`created_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


#
# Structure for table "question"
#

DROP TABLE IF EXISTS `question`;
CREATE TABLE `question` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `content` text,
  `user_id` int(11) NOT NULL,
  `created_date` datetime NOT NULL,
  `comment_count` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `date_index` (`created_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


#
# Structure for table "user"
#

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL DEFAULT '',
  `password` varchar(128) NOT NULL DEFAULT '',
  `salt` varchar(32) NOT NULL DEFAULT '',
  `head_url` varchar(256) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
