INSERT INTO TB_ROLE (role) VALUES ('ADMIN');
INSERT INTO TB_ROLE (role) VALUES ('USER');

INSERT INTO TB_APT (apt_name, apt_province, apt_city, apt_town) VALUES ('주공', '서울특별시', '강남구', '대치동');
INSERT INTO TB_APT (apt_name, apt_province, apt_city, apt_town) VALUES ('래미안', '서울특별시', '동작구', '사당동');
INSERT INTO TB_APT (apt_name, apt_province, apt_city, apt_town) VALUES ('푸르지오', '경기도', '화성시', '동탄동');
INSERT INTO TB_APT (apt_name, apt_province, apt_city, apt_town) VALUES ('중흥S클래스', '경기도', '군포시', '산본동');
INSERT INTO TB_APT (apt_name, apt_province, apt_city, apt_town) VALUES ('사랑으로부영', '부산광역시', '수영구', '수영동');

INSERT INTO TB_USER (email, password, signup_date, username, role) VALUES ('qwe@qwe.qwe', '$2a$10$FUGG6CFHmsptQ0KXebvv2.J9DM7lFpJSUWJtVUKyeq0bkQehdcUMq', NOW(), 'qwe', 'USER');
INSERT INTO TB_USER (email, password, signup_date, username, role) VALUES ('zxc@zxc.zxc', '$2a$10$FUGG6CFHmsptQ0KXebvv2.J9DM7lFpJSUWJtVUKyeq0bkQehdcUMq', NOW(), 'qwe', 'USER');
INSERT INTO TB_USER (email, password, signup_date, username, role) VALUES ('asd@asd.asd', '$2a$10$oAuT9QNVZe2e5nIZlkqAUOoYgsJDtX5WeYPRmQ5m.eSien2cca/he', NOW(), 'asd', 'ADMIN');

--INSERT INTO TB_BOARD (category, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('01', 'first title', 'first content', DEFAULT, DEFAULT, NOW(), NOW());
--INSERT INTO TB_BOARD (category, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('02', 'second title', 'second content', DEFAULT, DEFAULT, NOW(), NOW());
--INSERT INTO TB_BOARD (category, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('03', 'third title', 'third content', DEFAULT, DEFAULT, NOW(), NOW());

INSERT INTO TB_BOARD (category, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('01', '1', '1', 'first title', 'first content', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO TB_BOARD (category, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('02', '2', '2', 'second title', 'second content', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO TB_BOARD (category, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('03', '3', '3', 'third title', 'third content', DEFAULT, DEFAULT, NOW(), NOW());