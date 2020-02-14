/*
  write - select category
*/
const categoryTypes = document.querySelectorAll('.tt-button-icon')
categoryTypes.forEach(function (input) {
  input.addEventListener('click', function (event) {
    document.querySelector('input[name=category]').value = this.childNodes.item(3).textContent;
  });
});

/*
  write - typing title
*/
const maxInputTopicTitle = 99;
// let inputValue = parseInt(document.getElementsByClassName('tt-value-input').item(0).textContent);
const boardTitle = document.getElementById('boardTitle');

// window.onload는 javascript
// document.ready는 jquery
window.onload = function () {
  document.getElementsByClassName('tt-value-input').item(0).textContent = (maxInputTopicTitle - boardTitle.value.length);
};

// keydown은 누를 때 발동되기 때문에 글자수가 정확하게 카운트돼서 보이지 않음
// keypress는 backspace 키를 누를 때 발동하지 않음
// keyup은 꾹 누르고 있을 때 발동하지 않음 -> 그나마 정확하고 어느 키에서나 발동됨
boardTitle.addEventListener('keyup', function () {
  document.getElementsByClassName('tt-value-input').item(0).textContent = (maxInputTopicTitle - boardTitle.value.length);
}, false);


/*
  write - delete, put ajax method
  https://html.spec.whatwg.org/multipage/form-control-infrastructure.html#attr-fs-method
*/
const DELETE_POST_BTN = document.getElementById('deletePostBtn');
const UPDATE_POST_BTN = document.getElementById('updatePostBtn');

if (DELETE_POST_BTN != null) {
  DELETE_POST_BTN.addEventListener('click', function (event) {
    event.preventDefault();
    event.stopPropagation();
    // http://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#javascript-inlining
    $.ajax({
      url: '/board/' + boardId,
      method: 'delete',
      success: function (data) {
        alert(data);
        // window.location.href='/';
        window.location.replace('/');
      },
      error: function () {
        console.log('ajax error');
      }
    });
  }, false);
}

if (UPDATE_POST_BTN != null) {
  UPDATE_POST_BTN.addEventListener('click', function (event) {
    event.preventDefault();
    event.stopPropagation();
    // http://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#javascript-inlining
    $.ajax({
      url: '/board/' + boardId,
      method: 'put',
      data: {
        boardTitle: document.getElementById('boardTitle').value,
        boardContent: document.getElementById('boardContent').value
      },
      success: function (data) {
        alert(data);
        // window.location.href='/';
        window.location.replace('/');
      },
      error: function () {
        console.log('ajax error');
      }
    });
  }, false);
}