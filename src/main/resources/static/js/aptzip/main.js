import { deleteBoard, updateBoard } from "./board.js";
import { createComment, listComments } from "./comment.js";

if (document.body.contains(document.getElementById("deleteBoardButton"))) {
  document.getElementById("deleteBoardButton").addEventListener(
    "click",
    (event) => {
      event.preventDefault();
      event.stopPropagation();
      const boardId = document.getElementById("boardId");
      deleteBoard(`/board/${boardId.value}`);
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
      updateBoard("/board", boardId.value);
    },
    false
  );
}

function userFollow(URL) {
  fetch(URL, {
    method: "post",
    body: {
      userId: USER_ID,
    },
  })
    .then((res) => res.text())
    .then((result) => {
      const userFollowButton = document.getElementById("userFollowBtn");
      userFollowButton.classList.remove("btn-secondary");
      userFollowButton.classList.add("btn-primary");
      if (result === "save") {
        alert("팔로우 성공");
      } else if (result === "delete") {
        alert("팔로우 취소");
      } else {
        console.error(result);
      }
    })
    .catch((err) => console.error(err));
}
