/**
 * STOMP (topic subscription) : chat room subscription
 */
let stomp = null;
function connectSTOMP(URL) {
  stomp = new SockJS(URL); // endpoint
  let client = Stomp.over(stomp);
  // isStomp = true;
  stomp = client;
  stomp.debug = null;

  client.connect({}, function(frame) {
    // Controller's MessageMapping, header, message(자유형식)
    // client.send("/nba", {}, "msg: string");
    // configureMessageBroker() 에서 지정한 Application Destination Prefixes
    stomp.send(
      "/sub/msg",
      {},
      JSON.stringify({
        "msg": "test",
        "user": {
          "id":"1",
          "username":"qqqqq"
        }
      })
    );

    // 해당 토픽을 구독한다.
    client.subscribe("/topic/message", function(event) {
      // console.log("topic >>\n" + event);
    });
  });
};

/**
 * SockJS : Basic(Pure) WebSocket과 같지만 IE 8부터 지원
 */
let socket = null;
function connectSockJS(URL){
  let socket = new SockJS(URL);
  socket.onopen = function() {
    // console.log('Info : connection opened');
    socket.send('hi|+|test|+|hello');

    socket.onmessage = function(event) {
      // console.log(event.data + '\n');
    }
  }
}
