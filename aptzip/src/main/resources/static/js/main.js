/*
  write - select category
*/
function selectCategoryTypes(input) {
  input.addEventListener("click", function(event) {
    console.log(input);
    console.log(event);
    hiddenCategoryTypes.value = this.childNodes.item(3).textContent;
  });
}

/*
  write - typing title
*/
const maxInputTopicTitle = 99;
// let inputValue = parseInt(document.getElementsByClassName('tt-value-input').item(0).textContent);
function loadCalcRestTitle() {
  document.getElementsByClassName("tt-value-input").item(0).textContent =
    maxInputTopicTitle - boardTitle.value.length;
}

// keydown은 누를 때 발동되기 때문에 글자수가 정확하게 카운트돼서 보이지 않음
// keypress는 backspace 키를 누를 때 발동하지 않음
// keyup은 꾹 누르고 있을 때 발동하지 않음 -> 그나마 정확하고 어느 키에서나 발동됨
function keyUpCalcRestTitle() {
  document.getElementsByClassName("tt-value-input").item(0).textContent =
    maxInputTopicTitle - boardTitle.value.length;
}

/*
  write - delete, put ajax method
  https://html.spec.whatwg.org/multipage/form-control-infrastructure.html#attr-fs-method
*/
function deletePost(BOARD_ID) {
  // http://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#javascript-inlining
  $.ajax({
    url: "/board/" + BOARD_ID,
    method: "delete",
    headers:{
      "Content-Type":"application/json",
      "X-HTTP-Method-Override":"DELETE"
    },
    success: function(data) {
      alert(data);
      // window.location.href='/';
      window.location.replace("/");
    },
    error: function() {
      console.log("ajax error");
    }
  });
}

function updatePost(BOARD_ID, boardTitle, boardContent) {
  // http://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#javascript-inlining
  console.log(BOARD_ID);
  console.log(boardTitle);
  console.log(boardContent);
  $.ajax({
    url: "/board/" + BOARD_ID,
    method: "put",
    // headers:{
      // "Content-Type":"application/json",
      // "X-HTTP-Method-Override":"PUT"
    // },
    data: {
      "boardTitle": boardTitle,
      "boardContent": boardContent
    },
    success: function(data) {
      alert(data);
      // window.location.href='/';
      window.location.replace("/");
    },
    error: function() {
      console.log("ajax error");
    }
  });
}

/*
  Basic WebSocket
*/
let ws = null;
function connectWS() {

  let socket = new WebSocket("ws://localhost:8888/ws/comment");
  ws = socket;

  ws.onopen = function() {
    // console.log('Info : connection opened');
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
  
  ws.onerror = function(event) {
    // console.log('Info : connection error and close')
    // setTimeout(function(){
    //   connect();
    // }, 1000); // retry conntection
  }

}

/*
  STOMP
*/
let stomp = null;
window.onload = function() {
  this.connectWS();
  // closure 적용할 수 있을까?
  stomp = new SockJS("/ws/message"); // endpoint
  let client = Stomp.over(stomp);
  // isStomp = true;
  stomp = client;

  client.connect({}, function(frame) {
    console.log("Connected stomp!" + frame);
    // Controller's MessageMapping, header, message(자유형식)
    // client.send("/nba", {}, "msg: string");

    // 해당 토픽을 구독한다.
    client.subscribe("/topic/message", function(event) {
      console.log("!!!!!!!!!!!!event>>", event);
    });
  });
};