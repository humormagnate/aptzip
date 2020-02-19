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
<a sec:authorize="isAnonymous()" th:href="@{/login}" class="btn">Log in</a>
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
// 기본 표현법
```html
<span class="tt-badge">
  [[${ board.category } ? ${ board.category } : 'common']]
</span>

// 단축 표현법
<span class="tt-badge">
  [[${ board.category } ?: ${ board.category }]]
</span></div>
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

10. form 태그에서 Set 타입의 필드를 가지는 Post 객체가 있다고 할 때,
```html
<form th:object="${post}" th:action="@{/post}" th:method="post">
  <input type="text" th:field="*{title}">
  <input type="text" th:field="*{content}">

  <!-- Using select tag -->
  <select id="tags" th:field="*{tags}" size="3" multiple="multiple">
    <option th:each="tag : ${tags}" th:value="${tag}" th:text="${tag}">
      Tag
    </option>
  </select>

  <!-- Using checkbox tag -->
  <div th:each="tag : ${tags}">
    <input type="checkbox" th:id="${tag}" th:value="${tag}" th:field="*{tags}">
    <label th:for="${tag}" th:text="${tag}">Tag</label>
  </div>

  <button type="submit">저장</button>
</form>
```

11. 링크 경로
```html
<a th:href="@{http://localhost:8080/sample}"></a> // 절대 경로
<a th:href="@{/sample}"></a> // 상대 경로
<a th:href="@{~/sample}"></a> // 무조건 '/' 루트 경로
```

12. 레이아웃
소스 참고

# To-Do
- Spring EL 과의 호환성?
- $, #, @ 차이 알아보기