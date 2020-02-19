// stomp alert
// configureMessageBroker() 에서 지정한 Application Destination Prefixes
stomp.send(
  "/sub/anb",
  {},
  JSON.stringify({
    id: BOARD_ID,
    content: commentContent
  })
);