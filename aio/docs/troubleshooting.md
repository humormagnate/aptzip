# 트러블슈팅

## 1. MySQL 인코딩 이슈

새로운 게시물이나 댓글을 등록할 경우 문제없이 한글과 이모티콘이 출력되는데
init 스크립트를 통해서 등록된 데이터들은 인코딩이 깨지는 문제가 있었습니다.
캐릭터 셋은 다음과 같습니다.

```bash
docker exec aptzip-mysql mysql -u root -ptestmaria -e "show session variables like 'char%';"

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

```bash
docker exec aptzip-db mysql -u root -ptestmaria -e "select * from aptzip.tb_category;"

+----+---------------+
| id | category_name |
+----+---------------+
|  1 | Discussion    |
|  2 | Question      |
|  3 | Poll          |
|  4 | Gallery       |
|  5 | Media         |
|  6 | Common        |
+----+---------------+
```

## 2. init 스크립트 실행 중 에러가 발생할 경우

- 호스트 머신에서 마운트된 init 스크립트 수정
- `aptzip-mysql` MySQL CLI에서 아래 명령어 실행

```bash
mysql -u root -p
Enter password:

mysql> source /docker-entrypoint-initdb.d/3-aptzip-data.sql;
```

- 셸에서 비밀번호를 입력하는 것은 위험하다.

```bash
mysql -u root -ptestmaria < /docker-entrypoint-initdb.d/3-aptzip-data.sql
# mysql: [Warning] Using a password on the command line interface can be insecure.
```

## 3. @RequestMapping

- `@RequestMapping`에 Entity 오브젝트를 직접 넘기지 말 것

```java
// Persistent entities should not be used as arguments of "@RequestMapping" methods (java:S4684)
@PostMapping("/write")
public String postWrite(BoardEntity board) throws Exception {
  return "redirect:/";
}
```
