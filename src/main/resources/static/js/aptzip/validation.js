export { validateApartment, validateBoardForm };

function validateApartment(event) {
  event.preventDefault();
  if (document.getElementById("commentUserId").value) {
    const BOARD_APT_ID = document.getElementById("boardAptId").value;
    const USER_APT_ID = document.getElementById("commentUserAptId").value;
    if (BOARD_APT_ID != USER_APT_ID) {
      alert("같은 아파트 단지의 주민만 댓글을 입력하실 수 있습니다.");
    }
    document.getElementById("commentContent").focus();
  } else {
    location.href = LOGIN_PAGE;
  }
}

function validateBoardForm(event) {
  const boardTitle = document.getElementById("boardTitle");
  const boardContent = document.getElementById("boardContent");

  if (!boardTitle.value) {
    boardTitle.focus({ preventScroll: false });
    alert("제목을 입력해주세요.");
    event.preventDefault();
    return;
  }

  const hiddenCategoryTypes = document.querySelector("input[name=categoryId]");
  if (!hiddenCategoryTypes.value) {
    alert("카테고리를 선택해주세요.");
    event.preventDefault();
    return;
  }

  if (!boardContent.value) {
    boardContent.focus({ preventScroll: false });
    alert("내용을 입력해주세요.");
    event.preventDefault();
    return;
  }
}
