let likeflag = true;
const likeColor = "#ff5722";
const notLikeColor = "#666f74";
// export default (() => {
export const Like = (() => {
  // Constructor
  function Like(boardId) {
    this.boardId = boardId;
    this.readerId = readerId;
  }

  Like.prototype.insert = (path) => {
    fetch(path, {
      method: "post",
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then((res) => res.json())
      .then((data) => {
        console.log(data);
        document.getElementById("iconLike").style.fill = likeColor;
        likeflag = false;
      });
  };

  Like.prototype.delete = (path) => {
    fetch(path, {
      method: "delete",
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then((res) => res.json())
      .then((data) => {
        console.log(data);
        document.getElementById("iconLike").style.fill = notLikeColor;
        likeflag = true;
      });
  };

  return Like;
})();

if (document.body.contains(document.getElementById("likeBtn"))) {
  document.getElementById("likeBtn").addEventListener("click", (event) => {
    event.preventDefault();
    let like = new Like();
    const boardId = document.getElementById("boardId").value;
    const readerId = document.getElementById("readerId").value;
    if (!readerId) location.href = `/login`;
    if (likeflag) {
      like.insert(`/boards/${boardId}/like/${readerId}`);
    } else {
      like.delete(`/boards/${boardId}/like/${readerId}`);
    }
  });

  if (document.getElementById("likeBoard").value) {
    document.getElementById("iconLike").style.fill = likeColor;
    likeflag = false;
  }
}
