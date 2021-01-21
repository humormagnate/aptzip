# 앞집 (APT ZIP)

아파트 중심 무료 커뮤니티 서비스

사람 사는 곳에서는 마찰이 있기 마련입니다.
제가 사는 아파트는 피켓 들고 시위도 하시고, 전에 계시던 관리소장님은 해고를 당하셨습니다.
[2019 인구주택총조사](http://kostat.go.kr/portal/korea/kor_nw/1/2/2/index.board?bmode=read&bSeq=&aSeq=384690&pageNo=1&rowNum=10&navCount=10&currPg=&searchInfo=srch&sTarget=title&sTxt=2019)에 따르면 우리나라의 전체 가구 수 대비 아파트 가구 수는 51.1%에 달합니다.
이 비율이 상승할수록 아파트 주민 간 소통의 중요성은 늘어나고 있습니다.

![아파트 커뮤니티 활성화 필요성](apt-stat.jpg)
*출처: [아파트 갈등 해결책 "오늘도 눈인사 하셨나요? 그거예요" - 더스쿠프](https://m.post.naver.com/viewer/postView.nhn?volumeNo=29654178&memberNo=12494964)*


하지만 좁은 아파트 단지에서 괜히 껄끄러운 관계를 만들고 싶지 않은 분들도 많습니다.
제가 느끼기엔 소통이 완전히 단절된 느낌입니다.
흉흉한 뉴스들도 한몫했습니다.
그래서 불편한 일이 있더라도 마음에만 담아두고 나중에 조용히 이사를 가죠.
아파트에서 말할 수 있는 창구가 있는 것도 아닙니다.
기존 아파트 커뮤니티 서비스들은 각종 부가 서비스를 포함해서 유료로 판매하고요.

그래서 처음에 아파트 단지 주민이라는 인증만 거친다면
누구나 익명으로 참여할 수 있는 아파트 중심의 커뮤니티 서비스를 만들고 싶었습니다.
바로 앞집(APT ZIP) 입니다.

## 시작하기

### 도커 환경

- 먼저 MySQL 컨테이너를 생성합니다.

```bash
docker run \
--name mysql \
--publish 13306:3306 \
--detach \
--restart=always \
--env MYSQL_ROOT_PASSWORD=testmaria \
--env TZ=Asia/Seoul \
--volume /$PWD/aio/mysql/docker-entrypoint-initdb.d/aptzip.sql:/docker-entrypoint-initdb.d/aptzip.sql \
--volume /$PWD/aio/mysql/my.cnf:/etc/mysql/conf.d/aptzip.cnf,ro \
mysql:8.0.23
```

- 로컬 개발 환경에서는 아래 명령어를 실행해주세요.

```bash
make run
```

- 도커 컨테이너로 앞집을 실행시키고 싶으시다면 아래 명령어를 실행해주세요.

```bash
# 도커 이미지 빌드
make docker

# 앞집 컨테이너 생성
docker run \
--name aptzip \
--publish 9090:8080 \
--detach \
aptzip:0.1.0
```

### TEST 계정

| APT No. | Username | Password |
| ------- | -------- | -------- |
| 1       | admin1   | pass1    |
| 1       | user11   | pass11   |
| 1       | user12   | pass12   |
| 2       | admin2   | pass2    |
| 2       | user21   | pass21   |
| 2       | user22   | pass22   |
| 3       | admin3   | pass3    |
| 3       | user31   | pass31   |
| 3       | user32   | pass32   |

## API

### `USER`

| Path              | Method | Description                          |
| ----------------- | ------ | ------------------------------------ |
| /login            | GET    | 로그인 페이지 이동 (Spring Security) |
| /login            | POST   | 로그인 (Spring Security)             |
| /user/signup      | POST   | 회원 가입                            |
| /user/{id}        | GET    | 회원 정보 조회                       |
| /user/{id}        | PATCH  | 회원 탈퇴                            |
| /user/{id}/pw     | PATCH  | 비밀번호 변경                        |
| /user/{id}/follow | POST   | 회원 팔로우/취소 (추후 수정 필요)    |

### `BOARD`

| Path             | Method | Description             |
| ---------------- | ------ | ----------------------- |
| /board/write     | GET    | 게시글 작성 페이지 이동 |
| /board/write     | POST   | 게시글 작성             |
| /board/{id}      | GET    | 게시글 조회             |
| /board/{id}      | DELETE | 게시글 삭제             |
| /board/{id}/edit | GET    | 게시글 수정 페이지 이동 |
| /board/{id}      | PUT    | 게시글 수정             |

### `LIKE`

| Path       | Method | Description                         |
| ---------- | ------ | ----------------------------------- |
| /like/{id} | POST   | 게시글 좋아요/취소 (추후 수정 필요) |

### `COMMENT`

| Path          | Method | Description |
| ------------- | ------ | ----------- |
| /comment/{id} | POST   | 댓글 작성   |
| /comment/{id} | GET    | 댓글 조회   |
| /comment/{id} | DELETE | 댓글 삭제   |
| /comment/{id} | PUT    | 댓글 수정   |

### `APT`

| Path      | Method | Description                                |
| --------- | ------ | ------------------------------------------ |
| /apt/{id} | GET    | 현재 로그인 중인 회원의 아파트 게시글 조회 |

## 라이센스

- MIT
