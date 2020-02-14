INSERT INTO TB_ROLE (role) VALUES ('ADMIN');
INSERT INTO TB_ROLE (role) VALUES ('USER');

INSERT INTO TB_APT (apt_name, apt_province, apt_city, apt_town) VALUES ('주공', '서울특별시', '강남구', '대치동');
INSERT INTO TB_APT (apt_name, apt_province, apt_city, apt_town) VALUES ('래미안', '서울특별시', '동작구', '사당동');
INSERT INTO TB_APT (apt_name, apt_province, apt_city, apt_town) VALUES ('푸르지오', '경기도', '화성시', '동탄동');
INSERT INTO TB_APT (apt_name, apt_province, apt_city, apt_town) VALUES ('중흥S클래스', '경기도', '군포시', '산본동');
INSERT INTO TB_APT (apt_name, apt_province, apt_city, apt_town) VALUES ('사랑으로부영', '부산광역시', '수영구', '수영동');

INSERT INTO TB_USER (email, password, signup_date, username, role, apt_id) VALUES ('qwe@qwe.qwe', '$2a$10$FUGG6CFHmsptQ0KXebvv2.J9DM7lFpJSUWJtVUKyeq0bkQehdcUMq', NOW(), 'qwe', 'USER', 1);
INSERT INTO TB_USER (email, password, signup_date, username, role, apt_id) VALUES ('zxc@zxc.zxc', '$2a$10$FUGG6CFHmsptQ0KXebvv2.J9DM7lFpJSUWJtVUKyeq0bkQehdcUMq', NOW(), 'zxc', 'USER', 2);
INSERT INTO TB_USER (email, password, signup_date, username, role, apt_id) VALUES ('asd@asd.asd', '$2a$10$oAuT9QNVZe2e5nIZlkqAUOoYgsJDtX5WeYPRmQ5m.eSien2cca/he', NOW(), 'asd', 'ADMIN', 3);

--INSERT INTO TB_BOARD (category, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('01', 'first title', 'first content', DEFAULT, DEFAULT, NOW(), NOW());
--INSERT INTO TB_BOARD (category, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('02', 'second title', 'second content', DEFAULT, DEFAULT, NOW(), NOW());
--INSERT INTO TB_BOARD (category, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('03', 'third title', 'third content', DEFAULT, DEFAULT, NOW(), NOW());

INSERT INTO TB_BOARD (category, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('Discussion', '1', '1', '위하여, 얼음과 아름답고 있는 인간의 웅대한 인생에 봄바람이다. 이것을 전인 보배를 이상의 인간의 이것이다. 원대하고, 청춘의 열매를 불러 안고, ', '주는 가는 청춘에서만 위하여 못할 석가는 피가 없는 피다.\r\n\r\n되려니와, 현저하게 천지는 듣는다. 두기 유소년에게서 가는 이상의 날카로우나 이상의 아니다. 이상은 위하여서, 청춘은 현저하게 끓는 아니다. 굳세게 실현에 살았으며, 피고, 이상, 예수는 안고, 봄바람이다. 곳으로 불어 피어나기 그들의 교향악이다. 청춘 밝은 위하여, 부패뿐이다. 모래뿐일 있는 황금시대의 두손을 쓸쓸한 그들의 끓는다. 가진 새가 전인 불어 낙원을 아름다우냐?', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO TB_BOARD (category, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('Question', '2', '2', '천지는 충분히 같이, 천하를 청춘의 인생의 있는가?', 'second content', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO TB_BOARD (category, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('Poll', '3', '3', '못할 있음으로써 가진 실로 그리하였는가? 얼음이 열매를 뭇 오직 뛰노는 것은 이것을 얼음 영락과 있는가? 반짝이는 열락의 위하여, 돋고, 듣는다.', 'third content', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO TB_BOARD (category, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('Common', '3', '3', '것이 우리 끝까지 피에 반짝이는 것이다.', 'third content', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO TB_BOARD (category, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('Media', '2', '2', '있는 피고, 가슴이 보이는 별과 철환하였는가?', 'third content', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO TB_BOARD (category, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('Common', '1', '1', '충분히 같은 이상, 이상 얼음이 부패뿐이다. 우리의 풀이 속잎나고, 불어 인생을 아니다.', 'third content', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO TB_BOARD (category, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('Discussion', '1', '1', '할지라도 붙잡아 없으면, 밝은 인간의 있는 두손을 말이다. 끝에 그림자는 속에 꽃이 이상의 용감하고 청춘을 봄바람이다. 투명하되 대고, 끓는 청춘의 것이다.', 'third content', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO TB_BOARD (category, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('Question', '2', '2', '생의 않는 구할 끓는다.', 'third content', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO TB_BOARD (category, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('Poll', '3', '3', '행복스럽고 기관과 희망의 이것이다.', 'third content', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO TB_BOARD (category, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('Gallery', '1', '1', '있는 긴지라 영락과 동산에는 용기가 크고 없으면 안고,', 'third content', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO TB_BOARD (category, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('Media', '1', '1', '그들은 아름다우냐?', 'third content', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO TB_BOARD (category, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('Common', '1', '1', '것은 뼈 그들에게 보이는 피가 얼음이 피어나기 같이, 있으랴?', 'third content', DEFAULT, DEFAULT, NOW(), NOW());