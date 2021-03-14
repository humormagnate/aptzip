# 앞집 API

- [Swagger API 2.0](http://localhost:30414/swagger-ui/index.html)

## 사용자

| Path               | Method | Description   |
| ------------------ | ------ | ------------- |
| /users/{id}        | GET    | 회원정보 조회 |
| /users/{id}        | PATCH  | 탈퇴          |
| /users/{id}/pw     | PATCH  | 비밀번호 변경 |
| /users/{id}/follow | POST   | 팔로우        |
| /users/{id}/follow | DELETE | 팔로우 취소   |

### 인증

| Path    | Method | Description |
| ------- | ------ | ----------- |
| /login  | POST   | 로그인      |
| /signup | POST   | 회원가입    |

## 게시물

| Path              | Method | Description |
| ----------------- | ------ | ----------- |
| /boards/new       | POST   | 작성        |
| /boards/{id}      | GET    | 조회        |
| /boards/{id}      | PUT    | 수정        |
| /boards/{id}      | DELETE | 삭제        |
| /boards/{id}/like | POST   | 좋아요      |
| /boards/{id}/like | DELETE | 좋아요 취소 |

## 댓글

| Path                            | Method | Description |
| ------------------------------- | ------ | ----------- |
| /comments/{boardId}             | POST   | 작성        |
| /comments/{boardId}             | GET    | 조회        |
| /comments/{boardId}             | PUT    | 수정        |
| /comments/{boardId}/{commentId} | DELETE | 삭제        |
