SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS persistent_logins;
DROP TABLE IF EXISTS UserConnection;
DROP TABLE IF EXISTS tb_confirmation_token;
DROP TABLE IF EXISTS to_like;
DROP TABLE IF EXISTS tb_comment;
DROP TABLE IF EXISTS tb_favorite;
-- DROP TABLE IF EXISTS tb_notice;
DROP TABLE IF EXISTS tb_user_follow;
DROP TABLE IF EXISTS tb_board;
DROP TABLE IF EXISTS tb_user;
DROP TABLE IF EXISTS tb_role;
DROP TABLE IF EXISTS tb_apt;
DROP TABLE IF EXISTS tb_category;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE tb_role (
	role	VARCHAR(128) NOT NULL,
	PRIMARY KEY (role)
);

CREATE TABLE tb_apt (
	id 			      BIGINT      NOT NULL	AUTO_INCREMENT,
	apt_name      VARCHAR(128) NOT NULL,
	apt_province  VARCHAR(128) NOT NULL,
	apt_city      VARCHAR(128) NOT NULL,
	apt_town      VARCHAR(128) NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE tb_category (
	id		INTEGER				NOT NULL	AUTO_INCREMENT,
	category_name	VARCHAR(128)	NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE tb_user (
	id				BIGINT				NOT NULL	AUTO_INCREMENT,
	email					VARCHAR(128)	NOT NULL UNIQUE,
	password			VARCHAR(256)	NOT NULL,
	username			VARCHAR(128)	NOT NULL UNIQUE,
	introduction	TEXT,
	signup_date		TIMESTAMP			DEFAULT	NOW(),
	reported			INTEGER				DEFAULT '0' 	NOT NULL,
	role					VARCHAR(128)	DEFAULT 'USER',
	apt_id				BIGINT,
	is_enabled		BIT(1)				NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (apt_id)	REFERENCES tb_apt (id)
	--UNIQUE (email)
	--CONSTRAINT unique_email UNIQUE (email)
);
ALTER TABLE tb_user ADD CONSTRAINT FOREIGN KEY (role) REFERENCES tb_role (role);

CREATE TABLE tb_user_follow (
	id			BIGINT		NOT NULL	AUTO_INCREMENT,
	following			BIGINT		NOT NULL,
	follower			BIGINT		NOT NULL,
	create_date		TIMESTAMP	NOT NULL,
	-- PRIMARY KEY (from_user, to_user),
	PRIMARY KEY (id),
	FOREIGN KEY (following) REFERENCES tb_user (id),
	FOREIGN KEY (follower) REFERENCES tb_user (id)
);

CREATE TABLE tb_board (
	id			BIGINT				NOT NULL	AUTO_INCREMENT,
  board_title		VARCHAR(256)	NOT NULL,
  board_content	TEXT					NOT NULL,
	attachment		VARCHAR(100),
  category_id		INTEGER,
  board_status	VARCHAR(1)	DEFAULT 'Y',
  create_date		TIMESTAMP		DEFAULT NOW()	NOT NULL,
  update_date		TIMESTAMP		DEFAULT NOW()	NOT NULL,
  view_count		INTEGER			DEFAULT '0'	NOT NULL,
  user_id				BIGINT				NOT NULL,
  apt_id				BIGINT				NOT NULL,
	PRIMARY KEY (id),
  -- FOREIGN KEY (user_id)	REFERENCES tb_user (id),
	FOREIGN KEY (apt_id) 	REFERENCES tb_apt (id),
	FOREIGN KEY (category_id)	REFERENCES tb_category (id)
);
-- ALTER TABLE tb_board ADD CONSTRAINT FOREIGN KEY (apt_id) REFERENCES tb_apt (apt_id);

-- CREATE TABLE tb_notice (
-- 	id				INTEGER				NOT NULL	AUTO_INCREMENT,
--   notice_title		VARCHAR(256)	NOT NULL,
--   notice_content	TEXT					NOT NULL,
--   notice_status		VARCHAR(1)	DEFAULT 'Y'		NOT NULL,
--   create_date			TIMESTAMP		DEFAULT NOW()	NOT NULL,
--   update_date			TIMESTAMP		DEFAULT NOW()	NOT NULL,
--   apt_id					INTEGER				NOT NULL,
-- 	PRIMARY KEY (id),
--   FOREIGN KEY (apt_id) REFERENCES tb_apt (id)
-- );

CREATE TABLE tb_comment (
	id			BIGINT				NOT NULL	AUTO_INCREMENT,
  comment_content	TEXT					NOT NULL ,
  ip_address			VARCHAR(128),	--NOT NULL,
  create_date			TIMESTAMP		DEFAULT NOW()	NOT NULL,
  update_date			TIMESTAMP		DEFAULT NOW()	NOT NULL,
  comment_status	VARCHAR(1)	DEFAULT 'Y'		NOT NULL,
  board_id				BIGINT				NOT NULL,
  user_id					BIGINT				NOT NULL,
	PRIMARY KEY (id),
  FOREIGN KEY (board_id)	REFERENCES tb_board (id),
  FOREIGN KEY (user_id)		REFERENCES tb_user (id)
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

-- https://docs.spring.io/spring-social/docs/2.0.0.M4/reference/htmlsingle/#section_jdbcConnectionFactory
-- https://github.com/mihajul/spring-social/commit/48d49d806571385fd0b68777376b77fadfcb23c6
-- 
create table UserConnection (
	userId 					varchar(255) 	not null,
  providerId 			varchar(255) 	not null,
  providerUserId	varchar(255),
  `rank` 						integer				not null, --> rank == keyword
  displayName 		varchar(255),
  profileUrl 			varchar(512),
  imageUrl 				varchar(512),
  accessToken 		varchar(255) 	not null,
  secret 					varchar(255),
  refreshToken 		varchar(255),
  expireTime 			bigint,
  primary key (userId, providerId, providerUserId)
);
create unique index UserConnectionRank on UserConnection(userId, providerId, `rank`);