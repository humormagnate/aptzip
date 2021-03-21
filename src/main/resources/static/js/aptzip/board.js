import { validateBoardForm } from "./validation.js";
export { selectCategoryTypes, keyUpCalcRestTitle, deleteBoard, updateBoard };

if (
  window.location.href.includes("/boards/new") ||
  window.location.href.includes("/edit")
) {
  window.onload = keyUpCalcRestTitle;

  const categories = document.querySelectorAll(".select-category");
  categories.forEach((element) => {
    element.addEventListener("click", (event) => {
      selectCategoryTypes(event, categories);
    });
  });

  document.getElementById("boardTitle").addEventListener(
    "keyup",
    () => {
      // keydown은 누를 때 발동되기 때문에 글자수가 정확하게 카운트돼서 보이지 않음
      // keypress는 backspace 키를 누를 때 발동하지 않음
      // keyup은 꾹 누르고 있을 때 발동하지 않음 -> 그나마 정확하고 어느 키에서나 발동됨
      let newBoardTitle = document.getElementById("boardTitle");
      document
        .getElementsByClassName("tt-value-input")
        .item(0).textContent = keyUpCalcRestTitle(newBoardTitle.value.length);
    },
    false
  );

  document
    .getElementById("form-create-topic")
    .addEventListener("submit", validateBoardForm, false);
}

if (document.body.contains(document.getElementById("deleteBoardButton"))) {
  document.getElementById("deleteBoardButton").addEventListener(
    "click",
    (event) => {
      event.preventDefault();
      event.stopPropagation();
      const boardId = document.getElementById("boardId");
      deleteBoard(`/boards/${boardId.value}`);
    },
    false
  );
}

if (document.body.contains(document.getElementById("updateBoardButton"))) {
  document.getElementById("updateBoardButton").addEventListener(
    "click",
    (event) => {
      event.preventDefault();
      event.stopPropagation();
      const boardId = document.getElementById("boardId");
      updateBoard(`/boards/${boardId.value}`, boardId.value);
    },
    false
  );
}

function selectCategoryTypes(event, categories) {
  const hiddenCategoryTypes = document.querySelector("input[name=category]");
  // hiddenCategoryTypes.value = event.currentTarget.childNodes.item(5).value;
  hiddenCategoryTypes.value = event.currentTarget.querySelector("input").value;
  categories.forEach((categoryComponent) => {
    categoryComponent.classList.remove("active");
  });
  event.currentTarget.classList.add("active");
}

function keyUpCalcRestTitle(titleLength) {
  return 99 - titleLength;
}

// https://html.spec.whatwg.org/multipage/form-control-infrastructure.html#attr-fs-method
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
  const hiddenCategoryTypes = document.querySelector("input[name=category]");
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
      title: newBoardTitle.value,
      content: newBoardContent.value,
      category: hiddenCategoryTypes.value,
    }),
  })
    .then((res) => res.text())
    .then((data) => {
      alert(data.message);
      window.location.replace(document.referrer);
    })
    .catch((err) => console.error(err));
}
