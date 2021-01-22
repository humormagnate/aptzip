/*
  write - select category
*/
function selectCategoryTypes(input) {
  input.addEventListener("click", function (event) {
    console.log(this.childNodes);
    hiddenCategoryTypes.value = this.childNodes.item(5).value;
  });
}

/*
  write - typing title
*/
const maxInputTopicTitle = 99;
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
function deletePost(URL) {
  // http://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#javascript-inlining
  $.ajax({
    url: URL,
    method: "delete",
    headers: {
      "Content-Type": "application/json",
      "X-HTTP-Method-Override": "DELETE",
    },
    success: function (data) {
      alert(data);
      // window.location.href='/';
      window.location.replace('/');
    },
    error: function () {
      console.error("ajax error");
    },
  });
}

function updatePost(URL, boardTitle, boardContent) {
  // http://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#javascript-inlining
  $.ajax({
    url: URL,
    method: "put",
    // headers:{
    // "Content-Type":"application/json",
    // "X-HTTP-Method-Override":"PUT"
    // },
    data: {
      boardTitle: boardTitle,
      boardContent: boardContent,
      categoryId: hiddenCategoryTypes.value,
    },
    success: function (data) {
      alert(data);
      // window.location.href='/';
      window.location.replace('/');
    },
    error: function () {
      console.error("ajax error");
    },
  });
}

function userFollow(URL) {
  // console.log('follow test');
  event.preventDefault();

  $.ajax({
    url: URL,
    method: "post",
    // data: {
    //   "userId": USER_ID
    // },
    success: function (data) {
      $("#userFollowBtn").removeClass("btn-secondary");
      $("#userFollowBtn").addClass("btn-primary");
      if (data === "save") {
        alert("팔로우 성공");
      } else if (data === "delete") {
        alert("팔로우 취소");
      } else {
        console.error("server error");
      }
    },
    error: function () {
      console.error("ajax error");
    },
  });
}

function validateForm(event) {
  const boardContent = document.getElementById("boardContent");

  if (BOARD_TITLE.value == "") {
    BOARD_TITLE.focus({ preventScroll: false });
    alert("제목을 입력해주세요.");
    event.preventDefault();
    return false;
  }
  if (hiddenCategoryTypes.value == "") {
    alert("카테고리를 선택해주세요.");
    event.preventDefault();
    return false;
  }
  if (boardContent.value == "") {
    boardContent.focus({ preventScroll: false });
    alert("내용을 입력해주세요.");
    event.preventDefault();
    return false;
  }
  return true;
}
