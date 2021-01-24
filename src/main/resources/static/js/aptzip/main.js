import * as board from "./board.js";
import * as comment from "./comment.js";
import * as user from "./user.js";

if (
  window.location.href.includes("/board/write") ||
  window.location.href.includes("/edit")
) {
  window.onload = board.calcTitleLength();

  const categories = document.querySelectorAll(".select-category");
  categories.forEach((element) => {
    element.addEventListener("click", (event) => {
      board.selectCategoryTypes(event, categories);
    });
  });

  document
    .getElementById("boardTitle")
    .addEventListener("keyup", board.keyUpCalcRestTitle, false);

  document
    .getElementById("form-create-topic")
    .addEventListener("submit", board.validateForm, false);
}

if (document.body.contains(document.getElementById("deleteBoardButton"))) {
  document.getElementById("deleteBoardButton").addEventListener(
    "click",
    (event) => {
      event.preventDefault();
      event.stopPropagation();
      const boardId = document.getElementById("boardId");
      board.deleteBoard(`/board/${boardId.value}`);
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
      board.updateBoard("/board", boardId.value);
    },
    false
  );
}
