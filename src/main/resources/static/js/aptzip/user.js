export { follow };

function follow(URL) {
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
