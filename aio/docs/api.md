# 앞집 API

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

| Path              | Method | Description |
| ----------------- | ------ | ----------- |
| /boards/new       | POST   | 작성        |
| /boards/{id}      | GET    | 조회        |
| /boards/{id}      | DELETE | 삭제        |
| /boards/{id}      | PUT    | 수정        |
| /boards/{id}/like | POST   | 좋아요      |
| /boards/{id}/like | DELETE | 좋아요 취소 |

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
