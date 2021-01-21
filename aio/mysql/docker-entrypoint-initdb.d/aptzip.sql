CREATE DATABASE `aptzip`
	CHARACTER SET utf8mb4
	COLLATE utf8mb4_unicode_ci;

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
	-- UNIQUE (email)
	-- CONSTRAINT unique_email UNIQUE (email)
);
-- ALTER TABLE tb_user ADD UNIQUE (email);
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
	id			        BIGINT				NOT NULL	AUTO_INCREMENT,
  comment_content	TEXT					NOT NULL ,
  ip_address			VARCHAR(128),
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

create table UserConnection (
	userId 					varchar(255) 	not null,
  providerId 			varchar(255) 	not null,
  providerUserId	varchar(255),
  `rank` 						integer				not null, -- rank == keyword
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

-- mysql table names are case sensitive in MySQL.
-- Database and table names are not case sensitive in Windows,
-- and case sensitive in most varieties of Unix.
-- INSERT INTO TB_ROLE (role) VALUES ('ADMIN');
INSERT INTO tb_role (role) VALUES ('ADMIN');
INSERT INTO tb_role (role) VALUES ('USER');
-- lower_case_table_names=1
INSERT INTO tb_apt (apt_name, apt_province, apt_city, apt_town) VALUES ('주공', '서울특별시', '강남구', '대치동');
INSERT INTO tb_apt (apt_name, apt_province, apt_city, apt_town) VALUES ('래미안', '서울특별시', '동작구', '사당동');
INSERT INTO tb_apt (apt_name, apt_province, apt_city, apt_town) VALUES ('푸르지오', '경기도', '화성시', '동탄동');
INSERT INTO tb_apt (apt_name, apt_province, apt_city, apt_town) VALUES ('중흥S클래스', '경기도', '군포시', '산본동');
INSERT INTO tb_apt (apt_name, apt_province, apt_city, apt_town) VALUES ('사랑으로부영', '부산광역시', '수영구', '수영동');

INSERT INTO tb_category (category_name) VALUES ('Discussion');
INSERT INTO tb_category (category_name) VALUES ('Question');
INSERT INTO tb_category (category_name) VALUES ('Poll');
INSERT INTO tb_category (category_name) VALUES ('Gallery');
INSERT INTO tb_category (category_name) VALUES ('Media');
INSERT INTO tb_category (category_name) VALUES ('Common');

-- https://bcrypt-generator.com/
-- admin1 / pass1
-- user11 / pass11
INSERT INTO tb_user (email, password, signup_date, username, role, apt_id, is_enabled) VALUES ('admin1@gmail.com', '$2y$12$XqNgYGD4yV9a6u/ZhnpqF.DWTa5b.FM5CUf5W/ov9xJhejuDrBkNq', NOW(), 'admin1', 'ADMIN', 1, 1);
INSERT INTO tb_user (email, password, signup_date, username, role, apt_id, is_enabled) VALUES ('user11@gmail.com', '$2y$12$wnLRkrX/jXS4.u3LvxAntu4oLtD5TLg0OJClthuP.Xdq1cNDQtBk.', NOW(), 'user11', 'USER', 1, 1);
INSERT INTO tb_user (email, password, signup_date, username, role, apt_id, is_enabled) VALUES ('user12@gmail.com', '$2y$12$ErfTN69wslxkl/B4kGqoEum6pDJ3JDyPrjCAkYFH3BQFQSb4K2AS.', NOW(), 'user12', 'USER', 1, 1);
INSERT INTO tb_user (email, password, signup_date, username, role, apt_id, is_enabled) VALUES ('admin2@gmail.com', '$2y$12$FCTlqdgQVz9Y5LT0fTU4GeIXfmo98W77Hq4mSYkvtgP31u.7jX5/.', NOW(), 'admin2', 'ADMIN', 2, 1);
INSERT INTO tb_user (email, password, signup_date, username, role, apt_id, is_enabled) VALUES ('user21@gmail.com', '$2y$12$V5XTszfzsNPfbU7WdF.VUex6biDLM9E9PyHcWU/MeZMt/gXpgF2ra', NOW(), 'user21', 'USER', 2, 1);
INSERT INTO tb_user (email, password, signup_date, username, role, apt_id, is_enabled) VALUES ('user22@gmail.com', '$2y$12$bVP8DWMDAydYZIi/mXX1eeLV0wrM0anxmcW1MNJIDeGuZpCdwzB3S', NOW(), 'user22', 'USER', 2, 1);
INSERT INTO tb_user (email, password, signup_date, username, role, apt_id, is_enabled) VALUES ('admin3@gmail.com', '$2y$12$PtOoGxCedHotbCG6sjgeNOCgEZYlJRQM6LUDsPvOTAU/5tjW5/hau', NOW(), 'admin3', 'ADMIN', 3, 1);
INSERT INTO tb_user (email, password, signup_date, username, role, apt_id, is_enabled) VALUES ('user31@gmail.com', '$2y$12$xFtA.Rtg6LMOttg1GKUpTOysE9CMLyqKEGVRWUHfaHwbDR09WMbq2', NOW(), 'user31', 'USER', 3, 1);
INSERT INTO tb_user (email, password, signup_date, username, role, apt_id, is_enabled) VALUES ('user32@gmail.com', '$2y$12$QNe9R3BHZAYHAHw2Fjd7uuhJ8hCZWb2bGxwhWARc5UoMHIJ0BUc4W', NOW(), 'user32', 'USER', 3, 1);

INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('6', '1', '1', '1. 검색 가이드', 'Querydsl로 구현했습니다.\r\n\r\n아직 제목과 내용을 동시에 검색할 순 없지만 제목, 작성자, 토픽 카테고리로 게시글을 검색할 수 있습니다.\r\n\r\n이용해주셔서 감사합니다.😀', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('6', '2', '1', '2. 회원 가입', '회원가입 시 이메일 인증이 필요합니다.\r\n\r\n커뮤니티를 저해하는 행위와 가입 남용은 타인에게 불쾌감을 줄 수 있습니다.\r\n\r\n이용해주셔서 감사합니다.🥰', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('1', '8', '3', '3. [한글입숨] 테스트를 위하여, 얼음과 아름답고 있는 인간의 웅대한 인생에 봄바람이다. 이것을 전인 보배를 이상의 인간의 이것이다. 원대하고, 청춘의 열매를 불러 안고, ', '주는 가는 청춘에서만 위하여 못할 석가는 피가 없는 피다.\r\n\r\n되려니와, 현저하게 천지는 듣는다. 두기 유소년에게서 가는 이상의 날카로우나 이상의 아니다. 이상은 위하여서, 청춘은 현저하게 끓는 아니다. 굳세게 실현에 살았으며, 피고, 이상, 예수는 안고, 봄바람이다. 곳으로 불어 피어나기 그들의 교향악이다. 청춘 밝은 위하여, 부패뿐이다. 모래뿐일 있는 황금시대의 두손을 쓸쓸한 그들의 끓는다. 가진 새가 전인 불어 낙원을 아름다우냐?', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('2', '5', '2', '4. 계속 업데이트 중입니다.', '서버 재기동 등으로 간혹 접속이 불가할 수 있습니다. \r\n\r\n양해 부탁드립니다. 😥', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('4', '3', '1', '5. [한글입숨] 것은 뼈 그들에게 보이는 피가 얼음이 피어나기 같이, 있으랴?', '간에 보이는 실로 커다란 열매를 없으면, 보배를 얼마나 피다. 쓸쓸한 위하여, 노년에게서 약동하다. 힘차게 청춘 소담스러운 있는 같이, 보는 이 끓는 것이다. 옷을 이 작고 내려온 따뜻한 위하여 그들에게 이것이야말로 불러 아니다. 모래뿐일 실현에 시들어 운다. 이상은 인간의 피는 품었기 길지 튼튼하며, 힘차게 피어나기 것이다. 황금시대를 것은 현저하게 끓는 열락의 발휘하기 끓는 인생을 것은 것이다. 고동을 현저하게 귀는 산야에 용기가 있는 얼음이 교향악이다. 곧 새가 하는 것이다. 놀이 이상의 원대하고, 그들을 착목한는 약동하다.\r\n\r\n인간에 풍부하게 노래하며 구할 약동하다. 그림자는 위하여, 굳세게 끓는 것이다. 무엇이 그것은 공자는 되려니와, 부패를 우리 두손을 길지 뿐이다. 그러므로 봄날의 것이 얼음이 능히 청춘 것이다. 천고에 위하여 이상 귀는 이성은 현저하게 인생의 대중을 거선의 칼이다. 무엇을 모래뿐일 인간은 있다. 인간이 이상이 곳이 투명하되 칼이다. 갑 착목한는 설레는 아니다. 날카로우나 황금시대를 목숨을 위하여 이상의 그들에게 것이다.보라, 피고 관현악이며, 힘있다. 찬미를 보배를 가진 보이는 말이다.\r\n\r\n주며, 못할 같이, 그들의 하여도 동산에는 이상의 우리 듣는다. 그들의 청춘 인생에 철환하였는가? 이상 행복스럽고 청춘의 얼마나 희망의 작고 청춘의 위하여, 하여도 사막이다. 석가는 동산에는 청춘 것이다. 기관과 방황하여도, 구하지 사람은 반짝이는 싸인 주며, 인간은 아니다. 끝에 구할 평화스러운 사랑의 안고, 이상은 청춘 보라. 인도하겠다는 쓸쓸한 더운지라 끓는 굳세게 희망의 이상의 아니다. 밝은 봄바람을 싹이 뿐이다. 못하다 오아이스도 충분히 인생에 위하여서. 하여도 그들은 커다란 봄바람이다.', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('6', '1', '1', '1. 검색 가이드', 'Querydsl로 구현했습니다.\r\n\r\n아직 제목과 내용을 동시에 검색할 순 없지만 제목, 작성자, 토픽 카테고리로 게시글을 검색할 수 있습니다.\r\n\r\n이용해주셔서 감사합니다.😀', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('6', '2', '1', '2. 회원 가입', '회원가입 시 이메일 인증이 필요합니다.\r\n\r\n커뮤니티를 저해하는 행위와 가입 남용은 타인에게 불쾌감을 줄 수 있습니다.\r\n\r\n이용해주셔서 감사합니다.🥰', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('1', '8', '3', '3. [한글입숨] 테스트를 위하여, 얼음과 아름답고 있는 인간의 웅대한 인생에 봄바람이다. 이것을 전인 보배를 이상의 인간의 이것이다. 원대하고, 청춘의 열매를 불러 안고, ', '주는 가는 청춘에서만 위하여 못할 석가는 피가 없는 피다.\r\n\r\n되려니와, 현저하게 천지는 듣는다. 두기 유소년에게서 가는 이상의 날카로우나 이상의 아니다. 이상은 위하여서, 청춘은 현저하게 끓는 아니다. 굳세게 실현에 살았으며, 피고, 이상, 예수는 안고, 봄바람이다. 곳으로 불어 피어나기 그들의 교향악이다. 청춘 밝은 위하여, 부패뿐이다. 모래뿐일 있는 황금시대의 두손을 쓸쓸한 그들의 끓는다. 가진 새가 전인 불어 낙원을 아름다우냐?', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('2', '5', '2', '4. 계속 업데이트 중입니다.', '서버 재기동 등으로 간혹 접속이 불가할 수 있습니다. \r\n\r\n양해 부탁드립니다. 😥', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('4', '3', '1', '5. [한글입숨] 것은 뼈 그들에게 보이는 피가 얼음이 피어나기 같이, 있으랴?', '간에 보이는 실로 커다란 열매를 없으면, 보배를 얼마나 피다. 쓸쓸한 위하여, 노년에게서 약동하다. 힘차게 청춘 소담스러운 있는 같이, 보는 이 끓는 것이다. 옷을 이 작고 내려온 따뜻한 위하여 그들에게 이것이야말로 불러 아니다. 모래뿐일 실현에 시들어 운다. 이상은 인간의 피는 품었기 길지 튼튼하며, 힘차게 피어나기 것이다. 황금시대를 것은 현저하게 끓는 열락의 발휘하기 끓는 인생을 것은 것이다. 고동을 현저하게 귀는 산야에 용기가 있는 얼음이 교향악이다. 곧 새가 하는 것이다. 놀이 이상의 원대하고, 그들을 착목한는 약동하다.\r\n\r\n인간에 풍부하게 노래하며 구할 약동하다. 그림자는 위하여, 굳세게 끓는 것이다. 무엇이 그것은 공자는 되려니와, 부패를 우리 두손을 길지 뿐이다. 그러므로 봄날의 것이 얼음이 능히 청춘 것이다. 천고에 위하여 이상 귀는 이성은 현저하게 인생의 대중을 거선의 칼이다. 무엇을 모래뿐일 인간은 있다. 인간이 이상이 곳이 투명하되 칼이다. 갑 착목한는 설레는 아니다. 날카로우나 황금시대를 목숨을 위하여 이상의 그들에게 것이다.보라, 피고 관현악이며, 힘있다. 찬미를 보배를 가진 보이는 말이다.\r\n\r\n주며, 못할 같이, 그들의 하여도 동산에는 이상의 우리 듣는다. 그들의 청춘 인생에 철환하였는가? 이상 행복스럽고 청춘의 얼마나 희망의 작고 청춘의 위하여, 하여도 사막이다. 석가는 동산에는 청춘 것이다. 기관과 방황하여도, 구하지 사람은 반짝이는 싸인 주며, 인간은 아니다. 끝에 구할 평화스러운 사랑의 안고, 이상은 청춘 보라. 인도하겠다는 쓸쓸한 더운지라 끓는 굳세게 희망의 이상의 아니다. 밝은 봄바람을 싹이 뿐이다. 못하다 오아이스도 충분히 인생에 위하여서. 하여도 그들은 커다란 봄바람이다.', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('6', '1', '1', '1. 검색 가이드', 'Querydsl로 구현했습니다.\r\n\r\n아직 제목과 내용을 동시에 검색할 순 없지만 제목, 작성자, 토픽 카테고리로 게시글을 검색할 수 있습니다.\r\n\r\n이용해주셔서 감사합니다.😀', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('6', '2', '1', '2. 회원 가입', '회원가입 시 이메일 인증이 필요합니다.\r\n\r\n커뮤니티를 저해하는 행위와 가입 남용은 타인에게 불쾌감을 줄 수 있습니다.\r\n\r\n이용해주셔서 감사합니다.🥰', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('1', '8', '3', '3. [한글입숨] 테스트를 위하여, 얼음과 아름답고 있는 인간의 웅대한 인생에 봄바람이다. 이것을 전인 보배를 이상의 인간의 이것이다. 원대하고, 청춘의 열매를 불러 안고, ', '주는 가는 청춘에서만 위하여 못할 석가는 피가 없는 피다.\r\n\r\n되려니와, 현저하게 천지는 듣는다. 두기 유소년에게서 가는 이상의 날카로우나 이상의 아니다. 이상은 위하여서, 청춘은 현저하게 끓는 아니다. 굳세게 실현에 살았으며, 피고, 이상, 예수는 안고, 봄바람이다. 곳으로 불어 피어나기 그들의 교향악이다. 청춘 밝은 위하여, 부패뿐이다. 모래뿐일 있는 황금시대의 두손을 쓸쓸한 그들의 끓는다. 가진 새가 전인 불어 낙원을 아름다우냐?', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('2', '5', '2', '4. 계속 업데이트 중입니다.', '서버 재기동 등으로 간혹 접속이 불가할 수 있습니다. \r\n\r\n양해 부탁드립니다. 😥', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('4', '3', '1', '5. [한글입숨] 것은 뼈 그들에게 보이는 피가 얼음이 피어나기 같이, 있으랴?', '간에 보이는 실로 커다란 열매를 없으면, 보배를 얼마나 피다. 쓸쓸한 위하여, 노년에게서 약동하다. 힘차게 청춘 소담스러운 있는 같이, 보는 이 끓는 것이다. 옷을 이 작고 내려온 따뜻한 위하여 그들에게 이것이야말로 불러 아니다. 모래뿐일 실현에 시들어 운다. 이상은 인간의 피는 품었기 길지 튼튼하며, 힘차게 피어나기 것이다. 황금시대를 것은 현저하게 끓는 열락의 발휘하기 끓는 인생을 것은 것이다. 고동을 현저하게 귀는 산야에 용기가 있는 얼음이 교향악이다. 곧 새가 하는 것이다. 놀이 이상의 원대하고, 그들을 착목한는 약동하다.\r\n\r\n인간에 풍부하게 노래하며 구할 약동하다. 그림자는 위하여, 굳세게 끓는 것이다. 무엇이 그것은 공자는 되려니와, 부패를 우리 두손을 길지 뿐이다. 그러므로 봄날의 것이 얼음이 능히 청춘 것이다. 천고에 위하여 이상 귀는 이성은 현저하게 인생의 대중을 거선의 칼이다. 무엇을 모래뿐일 인간은 있다. 인간이 이상이 곳이 투명하되 칼이다. 갑 착목한는 설레는 아니다. 날카로우나 황금시대를 목숨을 위하여 이상의 그들에게 것이다.보라, 피고 관현악이며, 힘있다. 찬미를 보배를 가진 보이는 말이다.\r\n\r\n주며, 못할 같이, 그들의 하여도 동산에는 이상의 우리 듣는다. 그들의 청춘 인생에 철환하였는가? 이상 행복스럽고 청춘의 얼마나 희망의 작고 청춘의 위하여, 하여도 사막이다. 석가는 동산에는 청춘 것이다. 기관과 방황하여도, 구하지 사람은 반짝이는 싸인 주며, 인간은 아니다. 끝에 구할 평화스러운 사랑의 안고, 이상은 청춘 보라. 인도하겠다는 쓸쓸한 더운지라 끓는 굳세게 희망의 이상의 아니다. 밝은 봄바람을 싹이 뿐이다. 못하다 오아이스도 충분히 인생에 위하여서. 하여도 그들은 커다란 봄바람이다.', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('6', '1', '1', '1. 검색 가이드', 'Querydsl로 구현했습니다.\r\n\r\n아직 제목과 내용을 동시에 검색할 순 없지만 제목, 작성자, 토픽 카테고리로 게시글을 검색할 수 있습니다.\r\n\r\n이용해주셔서 감사합니다.😀', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('6', '2', '1', '2. 회원 가입', '회원가입 시 이메일 인증이 필요합니다.\r\n\r\n커뮤니티를 저해하는 행위와 가입 남용은 타인에게 불쾌감을 줄 수 있습니다.\r\n\r\n이용해주셔서 감사합니다.🥰', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('1', '8', '3', '3. [한글입숨] 테스트를 위하여, 얼음과 아름답고 있는 인간의 웅대한 인생에 봄바람이다. 이것을 전인 보배를 이상의 인간의 이것이다. 원대하고, 청춘의 열매를 불러 안고, ', '주는 가는 청춘에서만 위하여 못할 석가는 피가 없는 피다.\r\n\r\n되려니와, 현저하게 천지는 듣는다. 두기 유소년에게서 가는 이상의 날카로우나 이상의 아니다. 이상은 위하여서, 청춘은 현저하게 끓는 아니다. 굳세게 실현에 살았으며, 피고, 이상, 예수는 안고, 봄바람이다. 곳으로 불어 피어나기 그들의 교향악이다. 청춘 밝은 위하여, 부패뿐이다. 모래뿐일 있는 황금시대의 두손을 쓸쓸한 그들의 끓는다. 가진 새가 전인 불어 낙원을 아름다우냐?', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('2', '5', '2', '4. 계속 업데이트 중입니다.', '서버 재기동 등으로 간혹 접속이 불가할 수 있습니다. \r\n\r\n양해 부탁드립니다. 😥', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('4', '3', '1', '5. [한글입숨] 것은 뼈 그들에게 보이는 피가 얼음이 피어나기 같이, 있으랴?', '간에 보이는 실로 커다란 열매를 없으면, 보배를 얼마나 피다. 쓸쓸한 위하여, 노년에게서 약동하다. 힘차게 청춘 소담스러운 있는 같이, 보는 이 끓는 것이다. 옷을 이 작고 내려온 따뜻한 위하여 그들에게 이것이야말로 불러 아니다. 모래뿐일 실현에 시들어 운다. 이상은 인간의 피는 품었기 길지 튼튼하며, 힘차게 피어나기 것이다. 황금시대를 것은 현저하게 끓는 열락의 발휘하기 끓는 인생을 것은 것이다. 고동을 현저하게 귀는 산야에 용기가 있는 얼음이 교향악이다. 곧 새가 하는 것이다. 놀이 이상의 원대하고, 그들을 착목한는 약동하다.\r\n\r\n인간에 풍부하게 노래하며 구할 약동하다. 그림자는 위하여, 굳세게 끓는 것이다. 무엇이 그것은 공자는 되려니와, 부패를 우리 두손을 길지 뿐이다. 그러므로 봄날의 것이 얼음이 능히 청춘 것이다. 천고에 위하여 이상 귀는 이성은 현저하게 인생의 대중을 거선의 칼이다. 무엇을 모래뿐일 인간은 있다. 인간이 이상이 곳이 투명하되 칼이다. 갑 착목한는 설레는 아니다. 날카로우나 황금시대를 목숨을 위하여 이상의 그들에게 것이다.보라, 피고 관현악이며, 힘있다. 찬미를 보배를 가진 보이는 말이다.\r\n\r\n주며, 못할 같이, 그들의 하여도 동산에는 이상의 우리 듣는다. 그들의 청춘 인생에 철환하였는가? 이상 행복스럽고 청춘의 얼마나 희망의 작고 청춘의 위하여, 하여도 사막이다. 석가는 동산에는 청춘 것이다. 기관과 방황하여도, 구하지 사람은 반짝이는 싸인 주며, 인간은 아니다. 끝에 구할 평화스러운 사랑의 안고, 이상은 청춘 보라. 인도하겠다는 쓸쓸한 더운지라 끓는 굳세게 희망의 이상의 아니다. 밝은 봄바람을 싹이 뿐이다. 못하다 오아이스도 충분히 인생에 위하여서. 하여도 그들은 커다란 봄바람이다.', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('6', '1', '1', '1. 검색 가이드', 'Querydsl로 구현했습니다.\r\n\r\n아직 제목과 내용을 동시에 검색할 순 없지만 제목, 작성자, 토픽 카테고리로 게시글을 검색할 수 있습니다.\r\n\r\n이용해주셔서 감사합니다.😀', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('6', '2', '1', '2. 회원 가입', '회원가입 시 이메일 인증이 필요합니다.\r\n\r\n커뮤니티를 저해하는 행위와 가입 남용은 타인에게 불쾌감을 줄 수 있습니다.\r\n\r\n이용해주셔서 감사합니다.🥰', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('1', '8', '3', '3. [한글입숨] 테스트를 위하여, 얼음과 아름답고 있는 인간의 웅대한 인생에 봄바람이다. 이것을 전인 보배를 이상의 인간의 이것이다. 원대하고, 청춘의 열매를 불러 안고, ', '주는 가는 청춘에서만 위하여 못할 석가는 피가 없는 피다.\r\n\r\n되려니와, 현저하게 천지는 듣는다. 두기 유소년에게서 가는 이상의 날카로우나 이상의 아니다. 이상은 위하여서, 청춘은 현저하게 끓는 아니다. 굳세게 실현에 살았으며, 피고, 이상, 예수는 안고, 봄바람이다. 곳으로 불어 피어나기 그들의 교향악이다. 청춘 밝은 위하여, 부패뿐이다. 모래뿐일 있는 황금시대의 두손을 쓸쓸한 그들의 끓는다. 가진 새가 전인 불어 낙원을 아름다우냐?', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('2', '5', '2', '4. 계속 업데이트 중입니다.', '서버 재기동 등으로 간혹 접속이 불가할 수 있습니다. \r\n\r\n양해 부탁드립니다. 😥', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('4', '3', '1', '5. [한글입숨] 것은 뼈 그들에게 보이는 피가 얼음이 피어나기 같이, 있으랴?', '간에 보이는 실로 커다란 열매를 없으면, 보배를 얼마나 피다. 쓸쓸한 위하여, 노년에게서 약동하다. 힘차게 청춘 소담스러운 있는 같이, 보는 이 끓는 것이다. 옷을 이 작고 내려온 따뜻한 위하여 그들에게 이것이야말로 불러 아니다. 모래뿐일 실현에 시들어 운다. 이상은 인간의 피는 품었기 길지 튼튼하며, 힘차게 피어나기 것이다. 황금시대를 것은 현저하게 끓는 열락의 발휘하기 끓는 인생을 것은 것이다. 고동을 현저하게 귀는 산야에 용기가 있는 얼음이 교향악이다. 곧 새가 하는 것이다. 놀이 이상의 원대하고, 그들을 착목한는 약동하다.\r\n\r\n인간에 풍부하게 노래하며 구할 약동하다. 그림자는 위하여, 굳세게 끓는 것이다. 무엇이 그것은 공자는 되려니와, 부패를 우리 두손을 길지 뿐이다. 그러므로 봄날의 것이 얼음이 능히 청춘 것이다. 천고에 위하여 이상 귀는 이성은 현저하게 인생의 대중을 거선의 칼이다. 무엇을 모래뿐일 인간은 있다. 인간이 이상이 곳이 투명하되 칼이다. 갑 착목한는 설레는 아니다. 날카로우나 황금시대를 목숨을 위하여 이상의 그들에게 것이다.보라, 피고 관현악이며, 힘있다. 찬미를 보배를 가진 보이는 말이다.\r\n\r\n주며, 못할 같이, 그들의 하여도 동산에는 이상의 우리 듣는다. 그들의 청춘 인생에 철환하였는가? 이상 행복스럽고 청춘의 얼마나 희망의 작고 청춘의 위하여, 하여도 사막이다. 석가는 동산에는 청춘 것이다. 기관과 방황하여도, 구하지 사람은 반짝이는 싸인 주며, 인간은 아니다. 끝에 구할 평화스러운 사랑의 안고, 이상은 청춘 보라. 인도하겠다는 쓸쓸한 더운지라 끓는 굳세게 희망의 이상의 아니다. 밝은 봄바람을 싹이 뿐이다. 못하다 오아이스도 충분히 인생에 위하여서. 하여도 그들은 커다란 봄바람이다.', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('6', '1', '1', '1. 검색 가이드', 'Querydsl로 구현했습니다.\r\n\r\n아직 제목과 내용을 동시에 검색할 순 없지만 제목, 작성자, 토픽 카테고리로 게시글을 검색할 수 있습니다.\r\n\r\n이용해주셔서 감사합니다.😀', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('6', '2', '1', '2. 회원 가입', '회원가입 시 이메일 인증이 필요합니다.\r\n\r\n커뮤니티를 저해하는 행위와 가입 남용은 타인에게 불쾌감을 줄 수 있습니다.\r\n\r\n이용해주셔서 감사합니다.🥰', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('1', '8', '3', '3. [한글입숨] 테스트를 위하여, 얼음과 아름답고 있는 인간의 웅대한 인생에 봄바람이다. 이것을 전인 보배를 이상의 인간의 이것이다. 원대하고, 청춘의 열매를 불러 안고, ', '주는 가는 청춘에서만 위하여 못할 석가는 피가 없는 피다.\r\n\r\n되려니와, 현저하게 천지는 듣는다. 두기 유소년에게서 가는 이상의 날카로우나 이상의 아니다. 이상은 위하여서, 청춘은 현저하게 끓는 아니다. 굳세게 실현에 살았으며, 피고, 이상, 예수는 안고, 봄바람이다. 곳으로 불어 피어나기 그들의 교향악이다. 청춘 밝은 위하여, 부패뿐이다. 모래뿐일 있는 황금시대의 두손을 쓸쓸한 그들의 끓는다. 가진 새가 전인 불어 낙원을 아름다우냐?', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('2', '5', '2', '4. 계속 업데이트 중입니다.', '서버 재기동 등으로 간혹 접속이 불가할 수 있습니다. \r\n\r\n양해 부탁드립니다. 😥', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('4', '3', '1', '5. [한글입숨] 것은 뼈 그들에게 보이는 피가 얼음이 피어나기 같이, 있으랴?', '간에 보이는 실로 커다란 열매를 없으면, 보배를 얼마나 피다. 쓸쓸한 위하여, 노년에게서 약동하다. 힘차게 청춘 소담스러운 있는 같이, 보는 이 끓는 것이다. 옷을 이 작고 내려온 따뜻한 위하여 그들에게 이것이야말로 불러 아니다. 모래뿐일 실현에 시들어 운다. 이상은 인간의 피는 품었기 길지 튼튼하며, 힘차게 피어나기 것이다. 황금시대를 것은 현저하게 끓는 열락의 발휘하기 끓는 인생을 것은 것이다. 고동을 현저하게 귀는 산야에 용기가 있는 얼음이 교향악이다. 곧 새가 하는 것이다. 놀이 이상의 원대하고, 그들을 착목한는 약동하다.\r\n\r\n인간에 풍부하게 노래하며 구할 약동하다. 그림자는 위하여, 굳세게 끓는 것이다. 무엇이 그것은 공자는 되려니와, 부패를 우리 두손을 길지 뿐이다. 그러므로 봄날의 것이 얼음이 능히 청춘 것이다. 천고에 위하여 이상 귀는 이성은 현저하게 인생의 대중을 거선의 칼이다. 무엇을 모래뿐일 인간은 있다. 인간이 이상이 곳이 투명하되 칼이다. 갑 착목한는 설레는 아니다. 날카로우나 황금시대를 목숨을 위하여 이상의 그들에게 것이다.보라, 피고 관현악이며, 힘있다. 찬미를 보배를 가진 보이는 말이다.\r\n\r\n주며, 못할 같이, 그들의 하여도 동산에는 이상의 우리 듣는다. 그들의 청춘 인생에 철환하였는가? 이상 행복스럽고 청춘의 얼마나 희망의 작고 청춘의 위하여, 하여도 사막이다. 석가는 동산에는 청춘 것이다. 기관과 방황하여도, 구하지 사람은 반짝이는 싸인 주며, 인간은 아니다. 끝에 구할 평화스러운 사랑의 안고, 이상은 청춘 보라. 인도하겠다는 쓸쓸한 더운지라 끓는 굳세게 희망의 이상의 아니다. 밝은 봄바람을 싹이 뿐이다. 못하다 오아이스도 충분히 인생에 위하여서. 하여도 그들은 커다란 봄바람이다.', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('6', '1', '1', '1. 검색 가이드', 'Querydsl로 구현했습니다.\r\n\r\n아직 제목과 내용을 동시에 검색할 순 없지만 제목, 작성자, 토픽 카테고리로 게시글을 검색할 수 있습니다.\r\n\r\n이용해주셔서 감사합니다.😀', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('6', '2', '1', '2. 회원 가입', '회원가입 시 이메일 인증이 필요합니다.\r\n\r\n커뮤니티를 저해하는 행위와 가입 남용은 타인에게 불쾌감을 줄 수 있습니다.\r\n\r\n이용해주셔서 감사합니다.🥰', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('1', '8', '3', '3. [한글입숨] 테스트를 위하여, 얼음과 아름답고 있는 인간의 웅대한 인생에 봄바람이다. 이것을 전인 보배를 이상의 인간의 이것이다. 원대하고, 청춘의 열매를 불러 안고, ', '주는 가는 청춘에서만 위하여 못할 석가는 피가 없는 피다.\r\n\r\n되려니와, 현저하게 천지는 듣는다. 두기 유소년에게서 가는 이상의 날카로우나 이상의 아니다. 이상은 위하여서, 청춘은 현저하게 끓는 아니다. 굳세게 실현에 살았으며, 피고, 이상, 예수는 안고, 봄바람이다. 곳으로 불어 피어나기 그들의 교향악이다. 청춘 밝은 위하여, 부패뿐이다. 모래뿐일 있는 황금시대의 두손을 쓸쓸한 그들의 끓는다. 가진 새가 전인 불어 낙원을 아름다우냐?', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('2', '5', '2', '4. 계속 업데이트 중입니다.', '서버 재기동 등으로 간혹 접속이 불가할 수 있습니다. \r\n\r\n양해 부탁드립니다. 😥', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('4', '3', '1', '5. [한글입숨] 것은 뼈 그들에게 보이는 피가 얼음이 피어나기 같이, 있으랴?', '간에 보이는 실로 커다란 열매를 없으면, 보배를 얼마나 피다. 쓸쓸한 위하여, 노년에게서 약동하다. 힘차게 청춘 소담스러운 있는 같이, 보는 이 끓는 것이다. 옷을 이 작고 내려온 따뜻한 위하여 그들에게 이것이야말로 불러 아니다. 모래뿐일 실현에 시들어 운다. 이상은 인간의 피는 품었기 길지 튼튼하며, 힘차게 피어나기 것이다. 황금시대를 것은 현저하게 끓는 열락의 발휘하기 끓는 인생을 것은 것이다. 고동을 현저하게 귀는 산야에 용기가 있는 얼음이 교향악이다. 곧 새가 하는 것이다. 놀이 이상의 원대하고, 그들을 착목한는 약동하다.\r\n\r\n인간에 풍부하게 노래하며 구할 약동하다. 그림자는 위하여, 굳세게 끓는 것이다. 무엇이 그것은 공자는 되려니와, 부패를 우리 두손을 길지 뿐이다. 그러므로 봄날의 것이 얼음이 능히 청춘 것이다. 천고에 위하여 이상 귀는 이성은 현저하게 인생의 대중을 거선의 칼이다. 무엇을 모래뿐일 인간은 있다. 인간이 이상이 곳이 투명하되 칼이다. 갑 착목한는 설레는 아니다. 날카로우나 황금시대를 목숨을 위하여 이상의 그들에게 것이다.보라, 피고 관현악이며, 힘있다. 찬미를 보배를 가진 보이는 말이다.\r\n\r\n주며, 못할 같이, 그들의 하여도 동산에는 이상의 우리 듣는다. 그들의 청춘 인생에 철환하였는가? 이상 행복스럽고 청춘의 얼마나 희망의 작고 청춘의 위하여, 하여도 사막이다. 석가는 동산에는 청춘 것이다. 기관과 방황하여도, 구하지 사람은 반짝이는 싸인 주며, 인간은 아니다. 끝에 구할 평화스러운 사랑의 안고, 이상은 청춘 보라. 인도하겠다는 쓸쓸한 더운지라 끓는 굳세게 희망의 이상의 아니다. 밝은 봄바람을 싹이 뿐이다. 못하다 오아이스도 충분히 인생에 위하여서. 하여도 그들은 커다란 봄바람이다.', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('6', '1', '1', '1. 검색 가이드', 'Querydsl로 구현했습니다.\r\n\r\n아직 제목과 내용을 동시에 검색할 순 없지만 제목, 작성자, 토픽 카테고리로 게시글을 검색할 수 있습니다.\r\n\r\n이용해주셔서 감사합니다.😀', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('6', '2', '1', '2. 회원 가입', '회원가입 시 이메일 인증이 필요합니다.\r\n\r\n커뮤니티를 저해하는 행위와 가입 남용은 타인에게 불쾌감을 줄 수 있습니다.\r\n\r\n이용해주셔서 감사합니다.🥰', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('1', '8', '3', '3. [한글입숨] 테스트를 위하여, 얼음과 아름답고 있는 인간의 웅대한 인생에 봄바람이다. 이것을 전인 보배를 이상의 인간의 이것이다. 원대하고, 청춘의 열매를 불러 안고, ', '주는 가는 청춘에서만 위하여 못할 석가는 피가 없는 피다.\r\n\r\n되려니와, 현저하게 천지는 듣는다. 두기 유소년에게서 가는 이상의 날카로우나 이상의 아니다. 이상은 위하여서, 청춘은 현저하게 끓는 아니다. 굳세게 실현에 살았으며, 피고, 이상, 예수는 안고, 봄바람이다. 곳으로 불어 피어나기 그들의 교향악이다. 청춘 밝은 위하여, 부패뿐이다. 모래뿐일 있는 황금시대의 두손을 쓸쓸한 그들의 끓는다. 가진 새가 전인 불어 낙원을 아름다우냐?', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('2', '5', '2', '4. 계속 업데이트 중입니다.', '서버 재기동 등으로 간혹 접속이 불가할 수 있습니다. \r\n\r\n양해 부탁드립니다. 😥', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO tb_board (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('4', '3', '1', '5. [한글입숨] 것은 뼈 그들에게 보이는 피가 얼음이 피어나기 같이, 있으랴?', '간에 보이는 실로 커다란 열매를 없으면, 보배를 얼마나 피다. 쓸쓸한 위하여, 노년에게서 약동하다. 힘차게 청춘 소담스러운 있는 같이, 보는 이 끓는 것이다. 옷을 이 작고 내려온 따뜻한 위하여 그들에게 이것이야말로 불러 아니다. 모래뿐일 실현에 시들어 운다. 이상은 인간의 피는 품었기 길지 튼튼하며, 힘차게 피어나기 것이다. 황금시대를 것은 현저하게 끓는 열락의 발휘하기 끓는 인생을 것은 것이다. 고동을 현저하게 귀는 산야에 용기가 있는 얼음이 교향악이다. 곧 새가 하는 것이다. 놀이 이상의 원대하고, 그들을 착목한는 약동하다.\r\n\r\n인간에 풍부하게 노래하며 구할 약동하다. 그림자는 위하여, 굳세게 끓는 것이다. 무엇이 그것은 공자는 되려니와, 부패를 우리 두손을 길지 뿐이다. 그러므로 봄날의 것이 얼음이 능히 청춘 것이다. 천고에 위하여 이상 귀는 이성은 현저하게 인생의 대중을 거선의 칼이다. 무엇을 모래뿐일 인간은 있다. 인간이 이상이 곳이 투명하되 칼이다. 갑 착목한는 설레는 아니다. 날카로우나 황금시대를 목숨을 위하여 이상의 그들에게 것이다.보라, 피고 관현악이며, 힘있다. 찬미를 보배를 가진 보이는 말이다.\r\n\r\n주며, 못할 같이, 그들의 하여도 동산에는 이상의 우리 듣는다. 그들의 청춘 인생에 철환하였는가? 이상 행복스럽고 청춘의 얼마나 희망의 작고 청춘의 위하여, 하여도 사막이다. 석가는 동산에는 청춘 것이다. 기관과 방황하여도, 구하지 사람은 반짝이는 싸인 주며, 인간은 아니다. 끝에 구할 평화스러운 사랑의 안고, 이상은 청춘 보라. 인도하겠다는 쓸쓸한 더운지라 끓는 굳세게 희망의 이상의 아니다. 밝은 봄바람을 싹이 뿐이다. 못하다 오아이스도 충분히 인생에 위하여서. 하여도 그들은 커다란 봄바람이다.', DEFAULT, DEFAULT, NOW(), NOW());

INSERT INTO tb_user_follow (following, follower, create_date) VALUES (2, 1, NOW());
INSERT INTO tb_user_follow (following, follower, create_date) VALUES (1, 2, NOW());
INSERT INTO tb_user_follow (following, follower, create_date) VALUES (3, 2, NOW());

commit;
