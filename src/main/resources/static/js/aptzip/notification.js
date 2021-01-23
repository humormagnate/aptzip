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
  let socket = new WebSocket("wss://localhost:443" + URL);
  ws = socket;

  ws.onopen = (event) => {
    // console.log('Info : ws connection opened\n', event.currentTarget);

    if (wsConnnectInterval != null) {
      clearInterval(wsConnnectInterval);
    }

    ws.onmessage = (event) => {
      // console.log("ws.onmessage : ", event.data + '\n');
      document.getElementsByClassName("alert-trigger")[0].click();
      document.getElementById("alertContent").innerText = event.data;
      document.getElementById("alertBadge").innerText = "comment";
    };

    ws.onclose = (event) => {
      // console.log('Info : connection closed');
    };
  };

  ws.onerror = (event) => {
    console.log("Info : connection error and close");
    wsConnnectInterval = setTimeout(function () {
      connectWS(URL);
    }, 1000 * 60); // retry conntection
  };
}
