INSERT INTO TB_ROLE (role) VALUES ('ADMIN');
INSERT INTO TB_ROLE (role) VALUES ('USER');

INSERT INTO TB_APT (apt_name, apt_province, apt_city, apt_town) VALUES ('주공', '서울특별시', '강남구', '대치동');
INSERT INTO TB_APT (apt_name, apt_province, apt_city, apt_town) VALUES ('래미안', '서울특별시', '동작구', '사당동');
INSERT INTO TB_APT (apt_name, apt_province, apt_city, apt_town) VALUES ('푸르지오', '경기도', '화성시', '동탄동');
INSERT INTO TB_APT (apt_name, apt_province, apt_city, apt_town) VALUES ('중흥S클래스', '경기도', '군포시', '산본동');
INSERT INTO TB_APT (apt_name, apt_province, apt_city, apt_town) VALUES ('사랑으로부영', '부산광역시', '수영구', '수영동');

INSERT INTO TB_CATEGORY (category_name) VALUES ('Discussion');
INSERT INTO TB_CATEGORY (category_name) VALUES ('Question');
INSERT INTO TB_CATEGORY (category_name) VALUES ('Poll');
INSERT INTO TB_CATEGORY (category_name) VALUES ('Gallery');
INSERT INTO TB_CATEGORY (category_name) VALUES ('Media');
INSERT INTO TB_CATEGORY (category_name) VALUES ('Common');

INSERT INTO TB_USER (email, password, signup_date, username, role, apt_id) VALUES ('qqq@qqq.qqq', '$2a$10$FUGG6CFHmsptQ0KXebvv2.J9DM7lFpJSUWJtVUKyeq0bkQehdcUMq', NOW(), 'qqqqq', 'ADMIN', 1);
INSERT INTO TB_USER (email, password, signup_date, username, role, apt_id) VALUES ('www@www.www', '$2a$10$FUGG6CFHmsptQ0KXebvv2.J9DM7lFpJSUWJtVUKyeq0bkQehdcUMq', NOW(), 'wwwww', 'USER', 1);
INSERT INTO TB_USER (email, password, signup_date, username, role, apt_id) VALUES ('eee@eee.eee', '$2a$10$FUGG6CFHmsptQ0KXebvv2.J9DM7lFpJSUWJtVUKyeq0bkQehdcUMq', NOW(), 'eeeee', 'USER', 1);
INSERT INTO TB_USER (email, password, signup_date, username, role, apt_id) VALUES ('aaa@aaa.aaa', '$2a$10$oAuT9QNVZe2e5nIZlkqAUOoYgsJDtX5WeYPRmQ5m.eSien2cca/he', NOW(), 'aaaaa', 'ADMIN', 2);
INSERT INTO TB_USER (email, password, signup_date, username, role, apt_id) VALUES ('sss@sss.sss', '$2a$10$oAuT9QNVZe2e5nIZlkqAUOoYgsJDtX5WeYPRmQ5m.eSien2cca/he', NOW(), 'sssss', 'USER', 2);
INSERT INTO TB_USER (email, password, signup_date, username, role, apt_id) VALUES ('ddd@ddd.ddd', '$2a$10$oAuT9QNVZe2e5nIZlkqAUOoYgsJDtX5WeYPRmQ5m.eSien2cca/he', NOW(), 'ddddd', 'USER', 2);
INSERT INTO TB_USER (email, password, signup_date, username, role, apt_id) VALUES ('zzz@zzz.zzz', '$2a$10$uPnIU4ALeQ/RmOxDrUAvVejQ0m.XgReaSUzoZIlqJD/rD2DZHKAu6', NOW(), 'zzzzz', 'ADMIN', 3);
INSERT INTO TB_USER (email, password, signup_date, username, role, apt_id) VALUES ('xxx@xxx.xxx', '$2a$10$uPnIU4ALeQ/RmOxDrUAvVejQ0m.XgReaSUzoZIlqJD/rD2DZHKAu6', NOW(), 'xxxxx', 'USER', 3);
INSERT INTO TB_USER (email, password, signup_date, username, role, apt_id) VALUES ('ccc@ccc.ccc', '$2a$10$uPnIU4ALeQ/RmOxDrUAvVejQ0m.XgReaSUzoZIlqJD/rD2DZHKAu6', NOW(), 'ccccc', 'USER', 3);

INSERT INTO TB_BOARD (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('1', '1', '1', '위하여, 얼음과 아름답고 있는 인간의 웅대한 인생에 봄바람이다. 이것을 전인 보배를 이상의 인간의 이것이다. 원대하고, 청춘의 열매를 불러 안고, ', '주는 가는 청춘에서만 위하여 못할 석가는 피가 없는 피다.\r\n\r\n되려니와, 현저하게 천지는 듣는다. 두기 유소년에게서 가는 이상의 날카로우나 이상의 아니다. 이상은 위하여서, 청춘은 현저하게 끓는 아니다. 굳세게 실현에 살았으며, 피고, 이상, 예수는 안고, 봄바람이다. 곳으로 불어 피어나기 그들의 교향악이다. 청춘 밝은 위하여, 부패뿐이다. 모래뿐일 있는 황금시대의 두손을 쓸쓸한 그들의 끓는다. 가진 새가 전인 불어 낙원을 아름다우냐?', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO TB_BOARD (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('2', '4', '2', '천지는 충분히 같이, 천하를 청춘의 인생의 있는가?', 'second content', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO TB_BOARD (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('3', '7', '3', '못할 있음으로써 가진 실로 그리하였는가? 얼음이 열매를 뭇 오직 뛰노는 것은 이것을 얼음 영락과 있는가? 반짝이는 열락의 위하여, 돋고, 듣는다.', 'third content', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO TB_BOARD (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('4', '9', '3', '것이 우리 끝까지 피에 반짝이는 것이다.', 'third content', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO TB_BOARD (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('4', '5', '2', '있는 피고, 가슴이 보이는 별과 철환하였는가?', 'third content', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO TB_BOARD (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('2', '1', '1', '충분히 같은 이상, 이상 얼음이 부패뿐이다. 우리의 풀이 속잎나고, 불어 인생을 아니다.', 'third content', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO TB_BOARD (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('2', '2', '1', '할지라도 붙잡아 없으면, 밝은 인간의 있는 두손을 말이다. 끝에 그림자는 속에 꽃이 이상의 용감하고 청춘을 봄바람이다. 투명하되 대고, 끓는 청춘의 것이다.', 'third content', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO TB_BOARD (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('1', '6', '2', '생의 않는 구할 끓는다.', 'third content', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO TB_BOARD (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('5', '8', '3', '행복스럽고 기관과 희망의 이것이다.', 'third content', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO TB_BOARD (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('6', '3', '1', '있는 긴지라 영락과 동산에는 용기가 크고 없으면 안고,', 'third content', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO TB_BOARD (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('1', '2', '1', '그들은 아름다우냐?', 'third content', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO TB_BOARD (category_id, user_id, apt_id, board_title, board_content, board_status, view_count, create_date, update_date) VALUES ('1', '1', '1', '것은 뼈 그들에게 보이는 피가 얼음이 피어나기 같이, 있으랴?', '간에 보이는 실로 커다란 열매를 없으면, 보배를 얼마나 피다. 쓸쓸한 위하여, 노년에게서 약동하다. 힘차게 청춘 소담스러운 있는 같이, 보는 이 끓는 것이다. 옷을 이 작고 내려온 따뜻한 위하여 그들에게 이것이야말로 불러 아니다. 모래뿐일 실현에 시들어 운다. 이상은 인간의 피는 품었기 길지 튼튼하며, 힘차게 피어나기 것이다. 황금시대를 것은 현저하게 끓는 열락의 발휘하기 끓는 인생을 것은 것이다. 고동을 현저하게 귀는 산야에 용기가 있는 얼음이 교향악이다. 곧 새가 하는 것이다. 놀이 이상의 원대하고, 그들을 착목한는 약동하다.\r\n\r\n인간에 풍부하게 노래하며 구할 약동하다. 그림자는 위하여, 굳세게 끓는 것이다. 무엇이 그것은 공자는 되려니와, 부패를 우리 두손을 길지 뿐이다. 그러므로 봄날의 것이 얼음이 능히 청춘 것이다. 천고에 위하여 이상 귀는 이성은 현저하게 인생의 대중을 거선의 칼이다. 무엇을 모래뿐일 인간은 있다. 인간이 이상이 곳이 투명하되 칼이다. 갑 착목한는 설레는 아니다. 날카로우나 황금시대를 목숨을 위하여 이상의 그들에게 것이다.보라, 피고 관현악이며, 힘있다. 찬미를 보배를 가진 보이는 말이다.\r\n\r\n주며, 못할 같이, 그들의 하여도 동산에는 이상의 우리 듣는다. 그들의 청춘 인생에 철환하였는가? 이상 행복스럽고 청춘의 얼마나 희망의 작고 청춘의 위하여, 하여도 사막이다. 석가는 동산에는 청춘 것이다. 기관과 방황하여도, 구하지 사람은 반짝이는 싸인 주며, 인간은 아니다. 끝에 구할 평화스러운 사랑의 안고, 이상은 청춘 보라. 인도하겠다는 쓸쓸한 더운지라 끓는 굳세게 희망의 이상의 아니다. 밝은 봄바람을 싹이 뿐이다. 못하다 오아이스도 충분히 인생에 위하여서. 하여도 그들은 커다란 봄바람이다.', DEFAULT, DEFAULT, NOW(), NOW());

INSERT INTO tb_user_follow (following, follower, create_date) VALUES (2, 1, NOW());
INSERT INTO tb_user_follow (following, follower, create_date) VALUES (1, 2, NOW());
INSERT INTO tb_user_follow (following, follower, create_date) VALUES (3, 2, NOW());