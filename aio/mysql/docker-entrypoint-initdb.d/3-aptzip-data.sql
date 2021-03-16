SET character_set_client = utf8mb4;
SET character_set_results = utf8mb4;
SET character_set_connection = utf8mb4;

-- USE `aptzip`;

-- mysql table names are case sensitive in MySQL.
-- Database and table names are not case sensitive in Windows,
-- and case sensitive in most varieties of Unix.

-- lower_case_table_names=1

INSERT INTO aptzip.tb_apt (code, complex, province, city, town, village, approval, building, apartment) VALUES ('A10024484', '힐스테이트지제역아파트', '경기도', '평택시', '', '세교동', '20201224', 16, 0);
INSERT INTO aptzip.tb_apt (code, complex, province, city, town, village, approval, building, apartment) VALUES ('A10026207', '서울숲리버뷰자이아파트', '서울특별시', '성동구', '', '행당동', '20180622', 7, 1034);
INSERT INTO aptzip.tb_apt (code, complex, province, city, town, village, approval, building, apartment) VALUES ('A10026378', '가온마을3단지', '세종특별자치시', '', '', '다정동', '20180321', 27, 1655);

-- https://bcrypt-generator.com/
-- admin1 / pass1
-- user11 / pass11
INSERT INTO aptzip.tb_user (email, password, signup_date, username, apt_code, enabled, role) VALUES ('admin1@gmail.com', '$2y$12$XqNgYGD4yV9a6u/ZhnpqF.DWTa5b.FM5CUf5W/ov9xJhejuDrBkNq', NOW(), 'admin1', 'A10024484', 1, 'ADMIN');
INSERT INTO aptzip.tb_user (email, password, signup_date, username, apt_code, enabled, role) VALUES ('user11@gmail.com', '$2y$12$wnLRkrX/jXS4.u3LvxAntu4oLtD5TLg0OJClthuP.Xdq1cNDQtBk.', NOW(), 'user11', 'A10024484', 1, 'USER');
INSERT INTO aptzip.tb_user (email, password, signup_date, username, apt_code, enabled, role) VALUES ('user12@gmail.com', '$2y$12$ErfTN69wslxkl/B4kGqoEum6pDJ3JDyPrjCAkYFH3BQFQSb4K2AS.', NOW(), 'user12', 'A10024484', 1, 'USER');
INSERT INTO aptzip.tb_user (email, password, signup_date, username, apt_code, enabled, role) VALUES ('admin2@gmail.com', '$2y$12$FCTlqdgQVz9Y5LT0fTU4GeIXfmo98W77Hq4mSYkvtgP31u.7jX5/.', NOW(), 'admin2', 'A10026207', 1, 'ADMIN');
INSERT INTO aptzip.tb_user (email, password, signup_date, username, apt_code, enabled, role) VALUES ('user21@gmail.com', '$2y$12$V5XTszfzsNPfbU7WdF.VUex6biDLM9E9PyHcWU/MeZMt/gXpgF2ra', NOW(), 'user21', 'A10026207', 1, 'USER');
INSERT INTO aptzip.tb_user (email, password, signup_date, username, apt_code, enabled, role) VALUES ('user22@gmail.com', '$2y$12$bVP8DWMDAydYZIi/mXX1eeLV0wrM0anxmcW1MNJIDeGuZpCdwzB3S', NOW(), 'user22', 'A10026207', 1, 'USER');
INSERT INTO aptzip.tb_user (email, password, signup_date, username, apt_code, enabled, role) VALUES ('admin3@gmail.com', '$2y$12$PtOoGxCedHotbCG6sjgeNOCgEZYlJRQM6LUDsPvOTAU/5tjW5/hau', NOW(), 'admin3', 'A10026378', 1, 'ADMIN');
INSERT INTO aptzip.tb_user (email, password, signup_date, username, apt_code, enabled, role) VALUES ('user31@gmail.com', '$2y$12$xFtA.Rtg6LMOttg1GKUpTOysE9CMLyqKEGVRWUHfaHwbDR09WMbq2', NOW(), 'user31', 'A10026378', 1, 'USER');
INSERT INTO aptzip.tb_user (email, password, signup_date, username, apt_code, enabled, role) VALUES ('user32@gmail.com', '$2y$12$QNe9R3BHZAYHAHw2Fjd7uuhJ8hCZWb2bGxwhWARc5UoMHIJ0BUc4W', NOW(), 'user32', 'A10026378', 1, 'USER');

-- DISCUSSION, QUESTION, POLL, GALLERY, MEDIA, COMMON
INSERT INTO aptzip.tb_board (category, user_id, apt_code, title, content, enabled, view_count, created_date, last_modified_date) VALUES ('COMMON', '1', 'A10024484', '1. 검색 가이드', 'Querydsl로 구현했습니다.\r\n\r\n아직 제목과 내용을 동시에 검색할 순 없지만 제목, 작성자, 토픽 카테고리로 게시물을 검색할 수 있습니다.\r\n\r\n이용해주셔서 감사합니다.😀', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO aptzip.tb_board (category, user_id, apt_code, title, content, enabled, view_count, created_date, last_modified_date) VALUES ('COMMON', '2', 'A10024484', '2. 회원 가입', '회원가입 시 이메일 인증이 필요합니다.\r\n\r\n커뮤니티를 저해하는 행위와 가입 남용은 타인에게 불쾌감을 줄 수 있습니다.\r\n\r\n이용해주셔서 감사합니다.🥰', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO aptzip.tb_board (category, user_id, apt_code, title, content, enabled, view_count, created_date, last_modified_date) VALUES ('DISCUSSION', '8', 'A10026378', '3. [한글입숨] 테스트를 위하여, 얼음과 아름답고 있는 인간의 웅대한 인생에 봄바람이다. 이것을 전인 보배를 이상의 인간의 이것이다. 원대하고, 청춘의 열매를 불러 안고, ', '주는 가는 청춘에서만 위하여 못할 석가는 피가 없는 피다.\r\n\r\n되려니와, 현저하게 천지는 듣는다. 두기 유소년에게서 가는 이상의 날카로우나 이상의 아니다. 이상은 위하여서, 청춘은 현저하게 끓는 아니다. 굳세게 실현에 살았으며, 피고, 이상, 예수는 안고, 봄바람이다. 곳으로 불어 피어나기 그들의 교향악이다. 청춘 밝은 위하여, 부패뿐이다. 모래뿐일 있는 황금시대의 두손을 쓸쓸한 그들의 끓는다. 가진 새가 전인 불어 낙원을 아름다우냐?', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO aptzip.tb_board (category, user_id, apt_code, title, content, enabled, view_count, created_date, last_modified_date) VALUES ('QUESTION', '5', 'A10026207', '4. 계속 업데이트 중입니다.', '서버 재기동 등으로 간혹 접속이 불가할 수 있습니다. \r\n\r\n양해 부탁드립니다. 😥', DEFAULT, DEFAULT, NOW(), NOW());
INSERT INTO aptzip.tb_board (category, user_id, apt_code, title, content, enabled, view_count, created_date, last_modified_date) VALUES ('GALLERY', '3', 'A10024484', '5. [한글입숨] 것은 뼈 그들에게 보이는 피가 얼음이 피어나기 같이, 있으랴?', '간에 보이는 실로 커다란 열매를 없으면, 보배를 얼마나 피다. 쓸쓸한 위하여, 노년에게서 약동하다. 힘차게 청춘 소담스러운 있는 같이, 보는 이 끓는 것이다. 옷을 이 작고 내려온 따뜻한 위하여 그들에게 이것이야말로 불러 아니다. 모래뿐일 실현에 시들어 운다. 이상은 인간의 피는 품었기 길지 튼튼하며, 힘차게 피어나기 것이다. 황금시대를 것은 현저하게 끓는 열락의 발휘하기 끓는 인생을 것은 것이다. 고동을 현저하게 귀는 산야에 용기가 있는 얼음이 교향악이다. 곧 새가 하는 것이다. 놀이 이상의 원대하고, 그들을 착목한는 약동하다.\r\n\r\n인간에 풍부하게 노래하며 구할 약동하다. 그림자는 위하여, 굳세게 끓는 것이다. 무엇이 그것은 공자는 되려니와, 부패를 우리 두손을 길지 뿐이다. 그러므로 봄날의 것이 얼음이 능히 청춘 것이다. 천고에 위하여 이상 귀는 이성은 현저하게 인생의 대중을 거선의 칼이다. 무엇을 모래뿐일 인간은 있다. 인간이 이상이 곳이 투명하되 칼이다. 갑 착목한는 설레는 아니다. 날카로우나 황금시대를 목숨을 위하여 이상의 그들에게 것이다.보라, 피고 관현악이며, 힘있다. 찬미를 보배를 가진 보이는 말이다.\r\n\r\n주며, 못할 같이, 그들의 하여도 동산에는 이상의 우리 듣는다. 그들의 청춘 인생에 철환하였는가? 이상 행복스럽고 청춘의 얼마나 희망의 작고 청춘의 위하여, 하여도 사막이다. 석가는 동산에는 청춘 것이다. 기관과 방황하여도, 구하지 사람은 반짝이는 싸인 주며, 인간은 아니다. 끝에 구할 평화스러운 사랑의 안고, 이상은 청춘 보라. 인도하겠다는 쓸쓸한 더운지라 끓는 굳세게 희망의 이상의 아니다. 밝은 봄바람을 싹이 뿐이다. 못하다 오아이스도 충분히 인생에 위하여서. 하여도 그들은 커다란 봄바람이다.', DEFAULT, DEFAULT, NOW(), NOW());

INSERT INTO aptzip.tb_user_follow (following, follower, created_date) VALUES (7, 6, NOW());
INSERT INTO aptzip.tb_user_follow (following, follower, created_date) VALUES (6, 7, NOW());
INSERT INTO aptzip.tb_user_follow (following, follower, created_date) VALUES (8, 7, NOW());

commit;
