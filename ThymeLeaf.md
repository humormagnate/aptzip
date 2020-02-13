# ThymeLeaf 알게 된 것들

> 기초
1. java에서 반환된 객체
```html
<div>[[${ model }]]</div>
```
2. 사용자가 로그인 인증을 하지 않았다면
```html
<tag sec:authorize="isAnonymous()"></tag>
```
3. th:href
```html
<a sec:authorize="isAnonymous()" th:href="@{/user/go/login}" class="btn">Log in</a>
```
4. 날짜 포맷
```html
<div class="tt-col-value">[[${ #temporals.format(board.createDate, 'HH:mm') }]]</div>
```
5. 3항 연산자
```html
<span class="tt-badge">
  [[${ board.category } ? 'common' : ${ board.category }]]
</span>
```
6. 3항 연산자
```html
<span class="tt-badge">
  [[${ board.category } ? 'common' : ${ board.category }]]
</span>
```
7. fragment import
```html
<div th:replace="fragments/navbar" />
```
8. 반복문
```html
<div class="tt-item" th:each="board : ${ list }">
  <a th:href="${'/board/' + board.id }">
</div>
```
9. Collection 사이즈
```html
<div class="tt-col-value">[[${ #lists.size(board.comments) }]]</div>
```

# To-Do
- Spring EL 과의 호환성?
- $, #, @ 차이 알아보기