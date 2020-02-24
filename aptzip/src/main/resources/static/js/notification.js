/**
 * STOMP notification
 */


/**
 * Basic(Pure) WebSocket notification
 */
let ws = null;
let wsConnnectInterval = null;
function connectWS(URL) {

  // let socket = new WebSocket("ws://localhost:8888/ws/comment");
  let socket = new WebSocket("ws://localhost:8888" + URL);
  ws = socket;

  ws.onopen = function(event) {
    // console.log('Info : ws connection opened\n', event.currentTarget);

    if (wsConnnectInterval != null) {
      clearInterval(wsConnnectInterval);
    }
    
    ws.onmessage = function(event) {
      // console.log("ws.onmessage : ", event.data + '\n');
      $('.alert-trigger').trigger('click');
      $('#alertContent').text(event.data);
      $('#alertBadge').text('comment');
      // console.log($alert);
    }
    
    ws.onclose = function(event) {
      // console.log('Info : connection closed');
    }
  }
  
  ws.onerror = function(event) {
    console.log('Info : connection error and close')
    wsConnnectInterval = setTimeout(function(){
      connectWS();
    }, 1000 * 60); // retry conntection
  }
  
}