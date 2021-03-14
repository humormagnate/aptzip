export { createFollow, cancleFollow };

function createFollow(userId) {
  fetch(`/users/${userId}/follow`, {
    method: "post",
  })
    .then((res) => res.json())
    .then((result) => {
      const userFollowButton = document.getElementById("userFollowBtn");
      userFollowButton.classList.remove("btn-secondary");
      userFollowButton.classList.add("btn-primary");
      console.log(result);
    })
    .catch((err) => console.error(err));
}

function cancleFollow(userId) {
  fetch(`/users/${userId}/follow`, {
    method: "delete",
  })
    .then((res) => res.json())
    .then((result) => {
      const userFollowButton = document.getElementById("userFollowBtn");
      userFollowButton.classList.remove("btn-primary");
      userFollowButton.classList.add("btn-secondary");
      console.log(result);
    })
    .catch((err) => console.error(err));
}

if (document.body.contains(document.getElementById("userFollowBtn"))) {
  document.getElementById("userFollowBtn").addEventListener("click", (e) => {
    // e.preventDefault();
    // e.stopPropagation();
    console.log("test")
    const userId = document.getElementById("userId").value;
    const userFollowButton = document.getElementById("userFollowBtn");
    if (userFollowButton.classList.contains("btn-primary")) {
      createFollow(userId);
      return;
    }
    if (userFollowButton.classList.contains("btn-secondary")) {
      createFollow(userId);
      return;
    }
  });
}
