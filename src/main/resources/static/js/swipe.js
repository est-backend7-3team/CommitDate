import { Stack } from './stack.js';


    const backStack = new Stack();
    const frontStack = new Stack();
    let currentJson = null;


function loadPostData() {

        fetch('/swipe/jsons')
            .then(response => {
                if (!response.ok) {
                    throw new Error('네트워크 응답에 문제가 있습니다.');
                }
                return response.json();
            })
            .then(data => {
                if(currentJson == null){ //최초로 눌렀을 때
                    currentJson = data; //받아온거 currentJson 으로 연결
                    mappingData(currentJson); // 화면에 쏴주기
                }else if (!frontStack.isEmpty()){ //frontStack 에 뭐가 있을 때
                    backStack.push(currentJson); //지금 보던거 backStack 으로 보내기.
                    currentJson = frontStack.pop(); //frontStack 의 헤드를 커런트로 옮기기
                    mappingData(currentJson); // 화면에 쏴주기
                }else{ // frontStack 아무것도 없을 때(계속 앞으로 누를 때)
                    backStack.push(currentJson); //보던 게시글 백으로 보내기
                    currentJson = data; //새로운 게시글 커런트로 옮기기
                    mappingData(currentJson); //화면에 쏴주기
                }
            })
            .catch(error => console.error('Error:', error));
}
window.loadPostData = loadPostData;


function prePostData(){

    if(currentJson == null){ //최초로 눌렀을 때
        alert("마지막 입니다.")
    }else if (!backStack.isEmpty()){ //backStack 에 뭐가 있을 때
        frontStack.push(currentJson); //지금 보던거 backStack 으로 보내기.
        currentJson = backStack.pop(); //backStack 의 헤드를 커런트로 옮기기
        mappingData(currentJson); // 화면에 쏴주기
    }else{ // backStack 에 아무것도 없을 때(계속 뒤로 누를 때)
        alert("마지막 입니다.")
    }
}
window.prePostData = prePostData;


function mappingData(data){
    // JSON 데이터로 th:text가 적용된 부분 업데이트
    document.getElementById('title').textContent = data.title;
    document.getElementById('profileImageURL').src = data.profileImageURL;
    document.getElementById('userName').textContent = data.userName;
    document.getElementById('introduce').textContent = data.introduce;
    document.getElementById('likeCount').textContent = data.likeCount;
    document.getElementById('comment').textContent = data.comment;
    document.getElementById('sourceCode').textContent = data.sourceCode;
}
