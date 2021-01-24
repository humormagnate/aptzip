export {
  selectCategoryTypes,
  calcTitleLength,
  keyUpCalcRestTitle,
  deleteBoard,
  updateBoard,
  validateForm,
};

/*
  write - select category
*/
function selectCategoryTypes(event, categories) {
  const hiddenCategoryTypes = document.querySelector("input[name=categoryId]");
  // hiddenCategoryTypes.value = event.currentTarget.childNodes.item(5).value;
  hiddenCategoryTypes.value = event.currentTarget.querySelector("input").value;
  categories.forEach((categoryComponent) => {
    categoryComponent.classList.remove("active");
  });
  event.currentTarget.classList.add("active");
}

/*
  write - typing title
*/
function calcTitleLength() {
  const maxInputTopicTitle = 99;
  let newBoardTitle = document.getElementById("boardTitle");
  document.getElementsByClassName("tt-value-input").item(0).textContent =
    maxInputTopicTitle - newBoardTitle.value.length;
}

// keydown은 누를 때 발동되기 때문에 글자수가 정확하게 카운트돼서 보이지 않음
// keypress는 backspace 키를 누를 때 발동하지 않음
// keyup은 꾹 누르고 있을 때 발동하지 않음 -> 그나마 정확하고 어느 키에서나 발동됨
function keyUpCalcRestTitle() {
  const maxInputTopicTitle = 99;
  let newBoardTitle = document.getElementById("boardTitle");
  document.getElementsByClassName("tt-value-input").item(0).textContent =
    maxInputTopicTitle - newBoardTitle.value.length;
}

/*
  write - delete, put ajax method
  https://html.spec.whatwg.org/multipage/form-control-infrastructure.html#attr-fs-method
*/
function deleteBoard(requestPath) {
  fetch(requestPath, {
    method: "delete",
    headers: {
      "Content-Type": "application/json",
      "X-HTTP-Method-Override": "DELETE",
    },
  })
    .then((res) => res.text())
    .then((data) => {
      alert(data);
      // window.location.href='/';
      window.location.replace("/");
    })
    .catch((err) => console.error(err));
}

function updateBoard(requestPath, boardId) {
  const hiddenCategoryTypes = document.querySelector("input[name=categoryId]");
  if (!hiddenCategoryTypes.value) {
    alert("카테고리는 필수 입력사항입니다.");
    return;
  }

  const newBoardTitle = document.getElementById("boardTitle");
  const newBoardContent = document.getElementById("boardContent");

  fetch(requestPath, {
    method: "put",
    headers: {
      "Content-Type": "application/json",
      // "X-HTTP-Method-Override":"PUT"
    },
    body: JSON.stringify({
      id: boardId,
      boardTitle: newBoardTitle.value,
      boardContent: newBoardContent.value,
      categoryId: hiddenCategoryTypes.value,
    }),
  })
    .then((res) => res.json())
    .then((data) => {
      alert(data.message);
      window.location.replace(document.referrer);
    })
    .catch((err) => console.error(err));
}

function validateForm(event) {
  const boardTitle = document.getElementById("boardTitle");
  const boardContent = document.getElementById("boardContent");

  if (!boardTitle.value) {
    boardTitle.focus({ preventScroll: false });
    alert("제목을 입력해주세요.");
    event.preventDefault();
    return false;
  }

  const hiddenCategoryTypes = document.querySelector("input[name=categoryId]");
  if (!hiddenCategoryTypes.value) {
    alert("카테고리를 선택해주세요.");
    event.preventDefault();
    return false;
  }

  if (!boardContent.value) {
    boardContent.focus({ preventScroll: false });
    alert("내용을 입력해주세요.");
    event.preventDefault();
    return false;
  }

  return true;
}
