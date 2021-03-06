import { validateApartment } from "./validation.js";
export {
  createComment,
  updateComment,
  removeComment,
  listComments,
  renderComment,
};

const createComment = (obj, path, callback) => {
  fetch(path, {
    method: "post",
    cache: "no-cache",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(obj),
  })
    .then((res) => res.json())
    .then(callback)
    .catch((err) => console.error(err));
};

const updateComment = (obj, path, callback) => {
  fetch(path, {
    method: "put",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(obj),
  })
    .then((res) => res.json())
    .then(callback)
    .catch((err) => console.error(err));
};

const removeComment = (path, callback) => {
  fetch(path, {
    method: "delete",
    cache: "no-cache",
    headers: {
      "Content-Type": "application/json",
    },
  })
    .then((res) => res.json())
    .then(callback)
    .catch((err) => console.error(err));
};

const listComments = (path, callback) => {
  fetch(path, {
    method: "get",
    headers: {
      "Content-Type": "application/json",
    },
  })
    .then((res) => res.json())
    .then(callback)
    .catch((err) => console.error(err));
};

const renderComment = (list) => {
  const commentContainer = document.getElementById("commentList");
  const readerId = document.getElementById("readerId").value;
  let str = "";
  list.forEach((renderObject) => {
    let temporal = new Date(renderObject.lastModifiedDate);

    str += `<div class="tt-item">
            <div class="tt-single-topic">
            <input type="hidden" value="${renderObject.id}">
                <div class="tt-item-header pt-noborder">
                <div class="tt-item-info info-top">
                <div class="tt-avatar-icon">
                <i class="tt-icon">
                  <svg>
                    <use xlink:href="/fonts/forum.svg#icon-ava-${renderObject.user.username.substring(
                      0,
                      1
                    )}"></use>
                  </svg>
                </i>
              </div>
              <div class="tt-avatar-title">
                <a href="/users/${renderObject.user.id}">${
      renderObject.user.username
    }</a>
              </div>
              <a href="#" class="tt-info-time">
                <i class="tt-icon">
                  <svg>
                    <use xlink:href="/fonts/forum.svg#icon-time"></use>
                  </svg>
                </i>
                ${
                  temporal.getFullYear() +
                  "-" +
                  (temporal.getMonth() + 1) +
                  "-" +
                  temporal.getDate()
                }
                </a>
              </div>
            </div>

            <div class="tt-item-description">
              ${renderObject.content}
            </div>
            <div class="tt-item-info info-bottom">
              <!--
              <a href="#" class="tt-icon-btn">
                  <i class="tt-icon">
                    <svg>
                      <use xlink:href="/fonts/forum.svg#icon-favorite"></use>
                    </svg>
                  </i>
                <span class="tt-text">0</span>
                -->
              </a>
            <div class="col-separator"></div>`;

    if (readerId && readerId == renderObject.user.id) {
      str += `<a href="#" class="tt-icon-btn tt-hover-02 tt-small-indent edit-comment">
                <i class="tt-icon">
                  <svg>
                    <use xlink:href="/fonts/forum.svg#icon-edit"></use>
                  </svg>
                </i>
              </a>
              <!--
              <a href="#" class="tt-icon-btn tt-hover-02 tt-small-indent">
                <i class="tt-icon">
                  <svg>
                    <use xlink:href="/fonts/forum.svg#icon-share"></use>
                  </svg>
                </i>
              </a>
              <a href="#" class="tt-icon-btn tt-hover-02 tt-small-indent">
                <i class="tt-icon">
                  <svg>
                    <use xlink:href="/fonts/forum.svg#icon-reply"></use>
                  </svg>
                </i>
              </a>
              -->
              `;
    }
    str += `</div>
          </div>
        </div>
      </div>
    </div>
    `;
    commentContainer.innerHTML = str;
  });
  editBtn();
};

if (document.body.contains(document.getElementById("saveEditCommentBtn"))) {
  document.getElementById("saveEditCommentBtn").addEventListener(
    "click",
    (event) => {
      event.preventDefault();
      event.stopPropagation();

      const commendId = document.getElementById("commentHiddenId").value;
      const newCommentContent = document.getElementById("updateCommentContent")
        .value;
      const boardId = document.getElementById("boardId").value;

      const obj = {
        boardId: boardId,
        id: commendId,
        content: newCommentContent,
      };

      updateComment(obj, `/comments/${boardId}`, function (list) {
        alert("댓글이 성공적으로 수정되었습니다.");
        renderComment(list);
        // document.getElementsByClassName("modal-filter").item(0).click();
        document.querySelector(".tt-btn-col-close").click();
      });
    },
    false
  );
}

if (document.body.contains(document.getElementById("deleteCommentBtn"))) {
  document.getElementById("deleteCommentBtn").addEventListener(
    "click",
    (event) => {
      event.preventDefault();
      event.stopPropagation();
      if (confirm("정말 삭제하시겠습니까?")) {
        const boardId = document.getElementById("boardId").value;
        const commentId = document.getElementById("commentHiddenId").value;
        removeComment(`/comments/${boardId}/${commentId}`, function (list) {
          document.getElementById("updateCommentContent").value = "";
          renderComment(list);
          document.getElementById("replyCount").innerText = list.length;
          // document.getElementsByClassName("modal-filter").item(0).click();
          document.querySelector(".tt-btn-col-close").click();
        });
      }
    },
    false
  );
}

const editCommentPopUp = document.getElementById("js-popup-edit-comment");
const closeButton = document.getElementsByClassName("tt-btn-col-close");

const editBtn = () => {
  document.querySelectorAll(".edit-comment").forEach((btn) => {
    btn.addEventListener("click", (e) => {
      e.preventDefault();
      // e.stopPropagation();

      if (
        e.currentTarget.classList.contains("column-open") ||
        editCommentPopUp.classList.contains("column-open")
      ) {
        closeButton[0].click();
        e.currentTarget.classList.remove("column-open");
        editCommentPopUp.classList.remove("column-open");
      } else {
        e.currentTarget.classList.add("column-open");
        editCommentPopUp.classList.add("column-open");

        const commentHiddenId = document.getElementById("commentHiddenId");
        commentHiddenId.value = e.currentTarget.parentElement.parentElement.children.item(
          0
        ).value;

        const updateCommentContent = document.getElementById(
          "updateCommentContent"
        );
        updateCommentContent.value = e.currentTarget.parentElement.parentElement.children.item(
          2
        ).innerText;
        updateCommentContent.focus();
      }

      // let ptScrollValue = body.scrollTop || html.scrollTop;
      // editCommentPopUp.classList.toggle("column-open").perfectScrollbar();
      // // body.style.top = -ptScrollValue;
      // body.classList.add("no-scroll");
      // body.append('<div class="modal-filter"></div>');

      // let modalFilter = document.querySelector(".modal-filter")[0].fadeTo("fast", 1);
      // let modalFilter = document.querySelectorAll(".modal-filter")[0];
      // if (modalFilter) {
      //   modalFilter.addEventListener("click", (e) => {
      //     console.log(e.currentTarget);
      //     closeButton.click();
      //   });
      // }
    });
  });

  document.querySelector(".tt-btn-col-close").addEventListener("click", (e) => {
    e.preventDefault();
    // e.stopPropagation();

    document.querySelectorAll(".edit-comment").forEach((btn) => {
      btn.classList.remove("column-open");
    });
    editCommentPopUp.classList.remove("column-open");

    document.getElementsByTagName("html")[0].removeAttribute("style");
    document.getElementById("commentHiddenId").value = "";
    document.getElementById("updateCommentContent").value = "";
  });
};

if (document.body.contains(document.getElementById("createReplyBtn"))) {
  document.getElementById("createReplyBtn").addEventListener(
    "click",
    (event) => {
      event.preventDefault();
      event.stopPropagation();
      const boardId = document.getElementById("boardId").value;
      const commentContent = document.getElementById("commentContent").value;
      const obj = {
        boardId: boardId,
        content: commentContent,
      };
      createComment(obj, `/comments/${boardId}`, (list) => {
        // https://developer.mozilla.org/en-US/docs/Web/API/WebSocket/readyState
        // 0	CONNECTING	Socket has been created. The connection is not yet open.
        // 1	OPEN	The connection is open and ready to communicate.
        // 2	CLOSING	The connection is in the process of closing.
        // 3	CLOSED	The connection is closed or couldn't be opened.
        // if (ws.readyState === 1) {
        // 	let msg = commentContent + "|+|" + "[[${#authentication?.principal} ne 'anonymousUser' ? ${#authentication?.principal.username} : '']]" + "|+|" + "[[${ board.user.username }]]";
        // 	ws.send(msg);
        // }
        document.getElementById("commentContent").value = "";
        renderComment(list);
        document.getElementById("replyCount").innerText = list.length;
      });
    },
    false
  );
}

if (document.body.contains(document.getElementById("iconReply"))) {
  document
    .getElementById("iconReply")
    .addEventListener("click", validateApartment);
}

if (
  document.location.href.includes("boards") &&
  !document.location.href.includes("edit") &&
  !document.location.href.includes("new")
) {
  document.addEventListener("DOMContentLoaded", () => {
    listComments(
      `/comments/${document.getElementById("boardId").value}`,
      (list) => {
        renderComment(list);
      }
    );
  });
}

// const deletePopup = (event) => {
//   const POPUP = event.target.parentNode.parentNode.parentNode;
//   POPUP.classList.add("d-none");
// };

if (document.body.contains(document.getElementById("removePopup"))) {
  document.getElementById("removePopup").addEventListener(
    "click",
    (event) => {
      event.preventDefault();
      event.stopPropagation();
      event.currentTarget.parentNode.parentNode.parentElement.remove();
    },
    false
  );
}
