CREATE DATABASE `aptzip` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
SET character_set_client = utf8mb4;
SET character_set_results = utf8mb4;
SET character_set_connection = utf8mb4;
USE `aptzip`;
-- drop table aptzip.persistent_logins cascade;
-- select * from aptzip.tb_board cascade;
-- delete from aptzip.tb_category where category_id = 7;
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS persistent_logins;
DROP TABLE IF EXISTS UserConnection;
DROP TABLE IF EXISTS tb_confirmation_token;
DROP TABLE IF EXISTS to_like;
DROP TABLE IF EXISTS tb_comment;
DROP TABLE IF EXISTS tb_favorite;
DROP TABLE IF EXISTS tb_user_follow;
DROP TABLE IF EXISTS tb_board;
DROP TABLE IF EXISTS tb_user;
DROP TABLE IF EXISTS tb_role;
DROP TABLE IF EXISTS tb_apt;
DROP TABLE IF EXISTS tb_category;
SET FOREIGN_KEY_CHECKS = 1;
CREATE TABLE tb_persistent_logins (
	username VARCHAR(100) not null,
	series VARCHAR(64) primary key,
	token VARCHAR(64) not null,
	last_used timestamp not null
);
CREATE TABLE tb_role (
	role VARCHAR(128) NOT NULL,
	PRIMARY KEY (role)
);
CREATE TABLE tb_apt (
	code VARCHAR(128) NOT NULL,
	complex VARCHAR(128) NOT NULL,
	province VARCHAR(128) NULL,
	city VARCHAR(128) NULL,
	town VARCHAR(128) NULL,
	village VARCHAR(128) NULL,
	approval VARCHAR(128) NOT NULL,
	building INTEGER DEFAULT '1' NOT NULL,
	apartment INTEGER DEFAULT '1' NOT NULL,
	PRIMARY KEY (code)
);
CREATE TABLE tb_category (
	id INTEGER NOT NULL AUTO_INCREMENT,
	category_name VARCHAR(128) NOT NULL,
	PRIMARY KEY (id)
);
CREATE TABLE tb_user (
	id BIGINT NOT NULL AUTO_INCREMENT,
	email VARCHAR(128) NOT NULL UNIQUE,
	password VARCHAR(256) NOT NULL,
	username VARCHAR(128) NOT NULL UNIQUE,
	introduction TEXT,
	signup_date TIMESTAMP DEFAULT NOW(),
	reported INTEGER DEFAULT '0' NOT NULL,
	apt_code VARCHAR(128),
	is_enabled BIT(1) NOT NULL,
	role VARCHAR(128) DEFAULT 'USER',
	PRIMARY KEY (id),
	FOREIGN KEY (apt_code) REFERENCES tb_apt (code) -- UNIQUE (email)
	-- CONSTRAINT unique_email UNIQUE (email)
);
-- ALTER TABLE tb_user ADD UNIQUE (email);
ALTER TABLE tb_user
ADD CONSTRAINT FOREIGN KEY (role) REFERENCES tb_role (role);
CREATE TABLE tb_user_follow (
	id BIGINT NOT NULL AUTO_INCREMENT,
	following BIGINT NOT NULL,
	follower BIGINT NOT NULL,
	create_date TIMESTAMP NOT NULL,
	-- PRIMARY KEY (from_user, to_user),
	PRIMARY KEY (id),
	FOREIGN KEY (following) REFERENCES tb_user (id),
	FOREIGN KEY (follower) REFERENCES tb_user (id)
);
CREATE TABLE tb_board (
	id BIGINT NOT NULL AUTO_INCREMENT,
	board_title VARCHAR(256) NOT NULL,
	board_content TEXT NOT NULL,
	attachment VARCHAR(100),
	category_id INTEGER,
	is_enabled BIT(1) DEFAULT 1 			NOT NULL,
	create_date TIMESTAMP DEFAULT NOW() NOT NULL,
	update_date TIMESTAMP DEFAULT NOW() NOT NULL,
	view_count INTEGER DEFAULT '0' NOT NULL,
	user_id BIGINT NOT NULL,
	apt_code VARCHAR(128) NOT NULL,
	PRIMARY KEY (id),
	-- FOREIGN KEY (user_id)	REFERENCES tb_user (id),
	FOREIGN KEY (apt_code) REFERENCES tb_apt (code),
	FOREIGN KEY (category_id) REFERENCES tb_category (id)
);
-- ALTER TABLE tb_board ADD CONSTRAINT FOREIGN KEY (apt_code) REFERENCES tb_apt (code);
CREATE TABLE tb_comment (
	id BIGINT NOT NULL AUTO_INCREMENT,
	comment_content TEXT NOT NULL,
	ip_address VARCHAR(128),
	create_date TIMESTAMP DEFAULT NOW() NOT NULL,
	update_date TIMESTAMP DEFAULT NOW() NOT NULL,
	comment_status VARCHAR(1) DEFAULT 'Y' NOT NULL,
	board_id BIGINT NOT NULL,
	user_id BIGINT NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (board_id) REFERENCES tb_board (id),
	FOREIGN KEY (user_id) REFERENCES tb_user (id)
);
-- ALTER TABLE tb_comment ADD CONSTRAINT FOREIGN KEY (user_id) REFERENCES tb_user (user_id);
CREATE TABLE persistent_logins (
	username VARCHAR(64) NOT NULL,
	series VARCHAR(64) PRIMARY KEY,
	token VARCHAR(64) NOT NULL,
	last_used timestamp NOT NULL
);
-- CREATE TABLE tb_favorite (
-- 	board_id			BIGINT			NOT NULL,
-- 	user_id				BIGINT			NOT NULL,
-- 	create_date		TIMESTAMP		DEFAULT NOW() NOT NULL,
-- 	PRIMARY KEY (board_id, user_id),
--   FOREIGN KEY (board_id)	REFERENCES tb_board (id),
--   FOREIGN KEY (user_id)		REFERENCES tb_user (id)
-- );
-- create table UserConnection (
-- 	userId varchar(255) not null,
-- 	providerId varchar(255) not null,
-- 	providerUserId varchar(255),
-- 	`rank` integer not null,
-- 	-- rank == keyword
-- 	displayName varchar(255),
-- 	profileUrl varchar(512),
-- 	imageUrl varchar(512),
-- 	accessToken varchar(255) not null,
-- 	secret varchar(255),
-- 	refreshToken varchar(255),
-- 	expireTime bigint,
-- 	primary key (userId, providerId, providerUserId)
-- );
-- create unique index UserConnectionRank on UserConnection(userId, providerId, `rank`);
commit;