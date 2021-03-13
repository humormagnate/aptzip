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
