# 앞집 (apt-zip)

아파트 중심 무료 커뮤니티 사이트

## Getting Started

### TEST 계정
>ID : wwwww<br>
>PW : qwe<br>

>ID : qqqqq<br>
>PW : qwe<br>

### Focus on
>Spring Boot 2.2.4 (Spring 5.2.3)<br>
>[STUDY](/study/study.md)<br>

```
Context Root (localhost:XXXX/)
```

## Built With

- Maven<br>
- VS Code<br>
- FE : Bootstrap, Thymeleaf, Handlebars<br>
- SCM : Git Bash, GitHub<br>

## API

| Path  | Method | Description |
| ------------- | ------------- | ------------- |
|```USER```|||
| /login | GET | 로그인 페이지 이동 (Spring Security) |
| /login | POST | 로그인 (Spring Security) |
| /user/signup | POST | 회원 가입 |
| /user/{id} | GET | 회원 정보 조회 |
| /user/{id} | DELETE | 회원 탈퇴 |
| /user/{id}/pw | PATCH | 비밀번호 변경 |
| /user/{id}/follow | POST | 회원 팔로우/취소 (추후 수정 필요) |
|```BOARD```|||
| /board/write | GET | 게시글 작성 페이지 이동 |
| /board/write | POST | 게시글 작성 |
| /board/{id} | GET | 게시글 조회 |
| /board/{id} | DELETE | 게시글 삭제 |
| /board/{id}/edit | GET | 게시글 수정 페이지 이동 |
| /board/{id} | PUT | 게시글 수정 |
|```LIKE```|||
| /like/{id} | POST | 게시글 좋아요/취소 (추후 수정 필요) |
|```COMMENT```|||
| /comment/{id} | POST | 댓글 작성 |
| /comment/{id} | GET | 댓글 조회 |
| /comment/{id} | DELETE | 댓글 삭제 |
| /comment/{id} | PUT | 댓글 수정 |
|```APT```|||
| /apt/{id} | GET | 현재 로그인 중인 회원의 아파트 게시글 조회 |



## Authors

* **임창수** - [humormagnate](https://github.com/humormagnate)

See also the list of [contributors](https://github.com/humormagnate/aptzip/graphs/contributors) who participated in this project.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](https://www.apache.org/licenses/LICENSE-2.0) file for details