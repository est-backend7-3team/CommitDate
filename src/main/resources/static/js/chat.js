var socket = new SockJS('/chat'); // 서버의 엔드포인트로 연결
var stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/messages', function(message) {
        console.log(message.body); // 서버에서 받은 메시지 출력
    });
});

function sendMessage() {
    var message = document.getElementById('messageInput').value;
    stompClient.send("/app/sendMessage", {}, message); // 메시지 전송
}