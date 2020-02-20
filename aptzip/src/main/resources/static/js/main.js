/*
  write - select category
*/
function selectCategoryTypes(input) {
  input.addEventListener("click", function(event) {
    // console.log(input);
    // console.log(event);
    hiddenCategoryTypes.value = this.childNodes.item(3).textContent;
  });
}

/*
  write - typing title
*/
const maxInputTopicTitle = 99;
// let inputValue = parseInt(document.getElementsByClassName('tt-value-input').item(0).textContent);
function loadCalcRestTitle() {
  document.getElementsByClassName("tt-value-input").item(0).textContent =
    maxInputTopicTitle - boardTitle.value.length;
}

// keydown은 누를 때 발동되기 때문에 글자수가 정확하게 카운트돼서 보이지 않음
// keypress는 backspace 키를 누를 때 발동하지 않음
// keyup은 꾹 누르고 있을 때 발동하지 않음 -> 그나마 정확하고 어느 키에서나 발동됨
function keyUpCalcRestTitle() {
  document.getElementsByClassName("tt-value-input").item(0).textContent =
    maxInputTopicTitle - boardTitle.value.length;
}

/*
  write - delete, put ajax method
  https://html.spec.whatwg.org/multipage/form-control-infrastructure.html#attr-fs-method
*/
function deletePost(BOARD_ID) {
  // http://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#javascript-inlining
  $.ajax({
    url: "/board/" + BOARD_ID,
    method: "delete",
    headers:{
      "Content-Type":"application/json",
      "X-HTTP-Method-Override":"DELETE"
    },
    success: function(data) {
      alert(data);
      // window.location.href='/';
      window.location.replace("/");
    },
    error: function() {
      console.log("ajax error");
    }
  });
}

function updatePost(BOARD_ID, boardTitle, boardContent) {
  // http://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#javascript-inlining
  console.log(BOARD_ID);
  console.log(boardTitle);
  console.log(boardContent);
  $.ajax({
    url: "/board/" + BOARD_ID,
    method: "put",
    // headers:{
      // "Content-Type":"application/json",
      // "X-HTTP-Method-Override":"PUT"
    // },
    data: {
      "boardTitle": boardTitle,
      "boardContent": boardContent
    },
    success: function(data) {
      alert(data);
      // window.location.href='/';
      window.location.replace("/");
    },
    error: function() {
      console.log("ajax error");
    }
  });
}

function userFollow() {
  console.log('follow test');
  event.preventDefault();

  $.ajax({
    url: "/user/" + TARGET_ID + "/follow",
    method: "post",
    // data: {
    //   "userId": USER_ID
    // },
    success: function(data) {
      $('#userFollowBtn').removeClass('btn-secondary');
      $('#userFollowBtn').addClass('btn-primary');
      console.log('팔로우 성공');
    },
    error: function(){
      console.log('ajax error');
    }
  })
}

function validateForm(event) {
  // console.log('form event');
  const boardContent = document.getElementById('boardContent');
  
  if (BOARD_TITLE.value == '') {
    BOARD_TITLE.focus();
    alert('제목을 입력해주세요.');
    event.preventDefault();
  }
  if (hiddenCategoryTypes.value == '') {
    alert('카테고리를 선택해주세요.');
    event.preventDefault();
  }
  if (boardContent.value == '') {
    boardContent.focus({preventScroll:false});
    alert('내용을 입력해주세요.');
    event.preventDefault();
  }
  return true;
}