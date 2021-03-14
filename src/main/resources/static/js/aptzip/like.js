let likeflag = true;
// export default (() => {
export const Like = (() => {
  // Constructor
  function Like(boardId) {
    this.boardId = boardId;
  }

  Like.prototype.insert = (obj) => {
    fetch(obj.url, {
      method: "post",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(obj),
    })
      .then((res) => res.text())
      .then((data) => {
        if (data === "insert") {
          document.getElementById("iconLike").style.fill = "#ff5722";
        } else if (data === "delete") {
          document.getElementById("iconLike").style.fill = "#666f74";
        } else {
          console.error("server error");
        }
      });
  };

  Like.prototype.delete = function (obj) {
    console.log(obj);
  };

  // return constructor
  return Like;
})();

if (document.body.contains(document.getElementById("likeBtn"))) {
  document.getElementById("likeBtn").addEventListener("click", (event) => {
    event.preventDefault();
    let like = new Like();
    const boardId = document.getElementById("boardId").value;
    const userId = document.getElementById("readerId");
    if (!userId) location.href = `/login`;
    const obj = { type: 1, boardId: boardId, url: `/like/${boardId}` };
    // FIXME:
    if (likeflag) {
      like.insert(obj);
    } else {
      like.insert(obj); // 우선 flag로 처리
      // like.delete(obj);
    }

    if (document.getElementById("likeBoard").value) {
      document.getElementById("iconLike").style.fill = "#ff5722";
      likeflag = false;
    }
  });
}
