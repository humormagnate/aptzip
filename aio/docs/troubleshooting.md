# 트러블슈팅

## 1. MySQL 인코딩 이슈

새로운 게시물이나 댓글을 등록할 경우 문제없이 한글과 이모티콘이 출력되는데
init 스크립트를 통해서 등록된 데이터들은 인코딩이 깨지는 문제가 있었습니다.
캐릭터 셋은 다음과 같습니다.

```bash
docker exec mysql mysql -u root -ptestmaria -e "show session variables like 'char%';"

# +--------------------------+--------------------------------+
# | Variable_name            | Value                          |
# +--------------------------+--------------------------------+
# | character_set_client     | latin1                         |
# | character_set_connection | latin1                         |
# | character_set_database   | utf8mb4                        |
# | character_set_filesystem | binary                         |
# | character_set_results    | latin1                         |
# | character_set_server     | utf8mb4                        |
# | character_set_system     | utf8                           |
# | character_sets_dir       | /usr/share/mysql-8.0/charsets/ |
# +--------------------------+--------------------------------+
```

이는 init 스크립트에 [캐릭터 셋](https://dev.mysql.com/doc/refman/8.0/en/charset-connection.html) 지정해서 해결할 수 있습니다.

```sql
CREATE DATABASE `aptzip`
	CHARACTER SET utf8mb4
	COLLATE utf8mb4_unicode_ci;

SET character_set_client = utf8mb4;
SET character_set_results = utf8mb4;
SET character_set_connection = utf8mb4;

USE `aptzip`;
```
