# 앞집 API

## TO-DO

- `/user/{id}/follow` => 팔로우 취소는 PATCH로 분리
- `/like/{id}` => 좋아요 취소는 DELETE로 분리
  - 좋아요(like) API는 게시글/댓글/사용자로 분리
- `/comment/{id}` => DELETE는 PATCH로 수정
- `/apt/{code}`는 아파트 조회 기능으로 만드는 것이 더 직관적, 특정 아파트 게시글들은 별도의 API 필요

## 사용자

| Path              | Method | Description   |
| ----------------- | ------ | ------------- |
| /login            | POST   | 로그인        |
| /user/signup      | POST   | 가입          |
| /user/{id}        | GET    | 정보 조회     |
| /user/{id}        | PATCH  | 탈퇴          |
| /user/{id}/pw     | PATCH  | 비밀번호 변경 |
| /user/{id}/follow | POST   | 팔로우/취소   |

## 게시글

| Path         | Method | Description |
| ------------ | ------ | ----------- |
| /board/write | POST   | 작성        |
| /board/{id}  | GET    | 조회        |
| /board/{id}  | DELETE | 삭제        |
| /board/{id}  | PUT    | 수정        |

## 좋아요

| Path       | Method | Description |
| ---------- | ------ | ----------- |
| /like/{id} | POST   | 좋아요/취소 |

## 댓글

| Path          | Method | Description |
| ------------- | ------ | ----------- |
| /comment/{id} | POST   | 작성        |
| /comment/{id} | GET    | 조회        |
| /comment/{id} | DELETE | 삭제        |
| /comment/{id} | PUT    | 수정        |

## 아파트

| Path        | Method | Description                                |
| ----------- | ------ | ------------------------------------------ |
| /apt/{code} | GET    | 현재 로그인 중인 회원의 아파트 게시글 조회 |
