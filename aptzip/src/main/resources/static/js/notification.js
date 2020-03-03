/**
 * STOMP notification
 */


/**
 * Basic(Pure) WebSocket notification
 */
let ws = null;
let wsConnnectInterval = null;
function connectWS(URL) {

  // local
  // let socket = new WebSocket("ws://localhost:8888" + URL);
  // let socket = new WebSocket("ws://localhost:8888/ws/comment");

  // server
  let socket = new WebSocket("wss://markruler.com:443" + URL);
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
      connectWS(URL);
    }, 1000 * 60); // retry conntection
  }
  
}