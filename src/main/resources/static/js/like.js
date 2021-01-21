function Like() {
  this.insert = function(obj) {
    $.ajax({
      url: obj.url,
      method: 'post',
      data: JSON.stringify(obj),
      // client -> server
      contentType: 'application/json',
      // server -> client
      dataType: 'text',
      success: function(data) {
        if (data === 'insert') {
          document.getElementById('iconLike').style.fill = '#ff5722';
        } else if (data === 'delete') {
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