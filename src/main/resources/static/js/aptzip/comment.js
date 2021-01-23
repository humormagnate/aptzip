// export {
//   createComment,
//   update,
//   remove,
//   listComments,
//   renderComment,
//   deletePopup,
// };

const createComment = (obj, callback) => {
  fetch(obj.url, {
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

const update = function (obj, callback) {
  fetch(obj.url, {
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

const remove = function (obj, callback) {
  fetch(obj.url, {
    method: "delete",
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

const listComments = (obj, callback) => {
  fetch(obj.url, {
    method: "get",
    headers: {
      "Content-Type": "application/json",
    },
  })
    .then((res) => res.json())
    .then(callback)
    .catch((err) => console.error(err));
};

const renderComment = (list, container, USER_ID) => {
  console.log(list);
  // hbs("comments-template", list, container, USER_ID);

  let str = "";
  let renderObject = "";

  for (let i = 0; i < list.length; i++) {
    renderObject = list[i];
    console.log(renderObject);
    let temporal = new Date(renderObject.updateDate);

    str += `<div class="tt-item">
            <div class="tt-single-topic">
  	          <input type="hidden" value="${renderObject.id}">
                <div class="tt-item-header pt-noborder">
                <div class="tt-item-info info-top">
  		          <div class="tt-avatar-icon">
                <i class="tt-icon">
                  <svg>
                    <use xlink:href="#icon-ava-${renderObject.user.username.substring(
                      0,
                      1
                    )}"></use>
                  </svg>
                </i>
              </div>
  	          <div class="tt-avatar-title">
  		          <a href="/user/info/${renderObject.user.id}">${
      renderObject.user.username
    }</a>
  		        </div>
              <a href="#" class="tt-info-time">
                <i class="tt-icon">
                  <svg>
                    <use xlink:href="#icon-time"></use>
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
              ${renderObject.commentContent}
            </div>

            <div class="tt-item-info info-bottom">
              <a href="#" class="tt-icon-btn">
                  <i class="tt-icon">
                    <svg>
                      <use xlink:href="#icon-favorite"></use>
                    </svg>
                  </i>
                <span class="tt-text">0</span>
              </a>
            </div>
            <div class="col-separator"></div>`;

    if (USER_ID == renderObject.user.id) {
      `<a href="#" class="tt-icon-btn tt-hover-02 tt-small-indent edit-comment">
          <i class="tt-icon">
            <svg>
              <use xlink:href="#icon-edit"></use>
            </svg>
          </i>
        </a>
        <a href="#" class="tt-icon-btn tt-hover-02 tt-small-indent">
          <i class="tt-icon">
            <svg>
              <use xlink:href="#icon-share"></use>
            </svg>
          </i>
        </a>
        <a href="#" class="tt-icon-btn tt-hover-02 tt-small-indent">
          <i class="tt-icon">
            <svg>
              <use xlink:href="#icon-reply"></use>
            </svg>
          </i>
        </a>`;
    }
    str += `
          </div>
        </div>
      </div>
    </div>
    `;
    container.innerHTML = str;
    editBtn();
  }
};

const deletePopup = (event) => {
  const POPUP = event.target.parentNode.parentNode.parentNode;
  POPUP.classList.add("d-none");
};

const UPDATE_COMMENT_BTN = document.getElementById("saveEditCommentBtn");
if (UPDATE_COMMENT_BTN) {
  UPDATE_COMMENT_BTN.addEventListener(
    "click",
    (event) => {
      event.preventDefault();
      event.stopPropagation();

      const COMMENT_ID = document.getElementById("commentHiddenId").value;
      const COMMENT_CONTENT = document.getElementById("updateCommentContent")
        .value;
      // const URL = '[[@{/comment}]]/' + BOARD_ID;
      const URL = `/comment/${BOARD_ID}`;

      const obj = {
        boardId: BOARD_ID,
        id: COMMENT_ID,
        commentContent: COMMENT_CONTENT,
        url: URL,
      };

      comment.update(obj, function (list) {
        alert("댓글이 성공적으로 수정되었습니다.");

        commentList.innerHTML = "";
        renderComment(list, commentList, USER_ID);
        document.getElementsByClassName("modal-filter").item(0).click();
      });
    },
    false
  );
}

/*
	Comment Edit Popup switching
*/
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
    // .scrollTop(top);
    // document.querySelectorAll(".modal-filter")[0].off().remove();
    // document.querySelectorAll(".modal-filter")[0].remove();
    document.getElementById("commentHiddenId").value = "";
    document.getElementById("updateCommentContent").value = "";
  });
};