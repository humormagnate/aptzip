DROP TABLE IF EXISTS persistent_logins CASCADE;
DROP TABLE IF EXISTS tb_comment;
DROP TABLE IF EXISTS tb_favorite;
-- DROP TABLE IF EXISTS tb_notice;
DROP TABLE IF EXISTS tb_user_follow;
DROP TABLE IF EXISTS tb_board	CASCADE;
DROP TABLE IF EXISTS tb_user CASCADE;
DROP TABLE IF EXISTS tb_role;
DROP TABLE IF EXISTS tb_apt;
DROP TABLE IF EXISTS tb_category;

CREATE TABLE tb_role (
	role	VARCHAR(128) NOT NULL,
	PRIMARY KEY (role)
);

CREATE TABLE tb_apt (
	id 			      INTEGER      NOT NULL	AUTO_INCREMENT,
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
	apt_id				INTEGER,
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
  apt_id				INTEGER				NOT NULL,
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
  comment_content	TEXT					NOT NULL,
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

CREATE TABLE tb_favorite (
	board_id			BIGINT			NOT NULL,
	user_id				BIGINT			NOT NULL,
	create_date		TIMESTAMP		DEFAULT NOW() NOT NULL,
	PRIMARY KEY (board_id, user_id),
  FOREIGN KEY (board_id)	REFERENCES tb_board (id),
  FOREIGN KEY (user_id)		REFERENCES tb_user (id)
);