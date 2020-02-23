function Like() {
  this.insert = function(obj) {
    $.ajax({
      url: '/like/' + obj.boardId,
      method: 'post',
      data: JSON.stringify(obj),
      // client -> server
      contentType: 'application/json',
      // server -> client
      dataType: 'text',
      success: function(data) {
        if (data === 'insert') {
          console.log('좋아요!');
          document.getElementById('iconLike').style.fill = '#ff5722';
        } else if (data === 'delete') {
          console.log('좋아요 취소');
          document.getElementById('iconLike').style.fill = '#666f74';
        } else {
          console.error('server error');
        }
      },
      error: function() {
        console.error('ajax error');
      }
    });
  },
  this.delete = function(obj) {
    console.log(obj);
  }
}