import { Stack } from './stack.js';

const backStack = new Stack();
const frontStack = new Stack();
let currentJson = null;
const leftButton = document.getElementById('leftButton')
const rightButton = document.getElementById('rightButton')
const likeButton = document.getElementById('likeButton');
const likeCount = document.getElementById('likeCount');
const blockPostButton = document.getElementById('blockPostButton');

document.addEventListener('DOMContentLoaded', () => {
    //최초 데이터 불러오기
    loadPostData();

    // 좋아요 버튼 클릭 이벤트
    likeButton.addEventListener('click',()=>toggleLike(likeButton));
    leftButton.addEventListener('click',prePostData);
    rightButton.addEventListener('click',loadPostData);
    blockPostButton.addEventListener('click',blockPost);
    });

function blockPost(){
    const postId = document.getElementById('postId').textContent.trim();

    fetch("swipe/api/blockPost",{
        method: "POST",
        headers: {
            "Content-Type":"application/json"
        },
        body: JSON.stringify({postId: postId})
    })
        .then(response => {
            console.log(response);
            if(!response.ok){
                const result = confirm("로그인이 필요한 서비스입니다. 로그인 창으로 이동하시겠습니까?");
                if(result){
                    window.location.href = "http://localhost:8080/login";
                    throw new Error("Unauthorized");
                } else {
                    throw new Error("Unauthorized");
                }
            }
            return response.text();
        })
        .then(status =>{
            if(status === "Success"){
                currentJson.isBlocked = 1;
                alert("정상적으로 처리되었습니다. 해당 게시물은 7일간 표시되지 않습니다.");
                closeDrawer();
                loadPostData();
            }
        })
}


function toggleLike(likeButton) {
    const postId = document.getElementById('postId').textContent.trim();
    fetch("/swipe/api/toggleLike", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ postId: postId })
    })
        .then(response => {
            if (!response.ok) {
                // 로그인되지 않은 경우
                const result = confirm("로그인이 필요한 서비스입니다. 로그인 창으로 이동하시겠습니까?");
                if(result){
                    window.location.href = "http://localhost:8080/login";
                    throw new Error("Unauthorized");
                } else {
                    throw new Error("Unauthorized");
                }
            }
            return response.text(); // 응답을 텍스트로 변환
        })
        .then(status => {
            // 상태에 따라 버튼 스타일 업데이트
            if (status === "Success") {
                if (likeButton.classList.contains("liked")) {
                    likeButton.classList.remove("liked");
                    likeButton.style.backgroundColor = "";
                    likeButton.style.color = "";
                    likeCount.textContent = Number(likeCount.textContent) - 1;
                    currentJson.isLike = 0;
                    currentJson.likeCount = Number(currentJson.likeCount) - 1;
                } else {
                    likeButton.classList.add("liked");
                    likeButton.style.backgroundColor = "magenta";
                    likeButton.style.color = "white";
                    likeCount.textContent = parseInt(likeCount.textContent) + 1;
                    currentJson.isLike = 1;
                    currentJson.likeCount = Number(currentJson.likeCount) + 1;
                }
            }
        })
        .catch(error => {
            console.error("Error:", error);
        });
}

function loadPostData() {

    if(currentJson == null){
        console.log("null탐");
        fetchSwipeJsonData()
            .then(getJson => {
                currentJson = getJson;
                mappingData(currentJson);
            })
    }else if(frontStack.isEmpty()){
        fetchSwipeJsonData()
            .then(getJson =>{
                console.log("isEmpty탐");
                console.log(JSON.stringify(currentJson, null, 2)); //Json 보기
                if(currentJson.isBlocked == null || currentJson.isBlocked === 0){
                    console.log("콘솔에 잘 밀어줬음.");
                    backStack.push(currentJson);
                }
                currentJson = getJson;
                mappingData(currentJson);
            })
    }else{
        console.log("else 마지막 탐 ");
        if(currentJson.isBlocked == null || currentJson.isBlocked === 0){
            backStack.push(currentJson);
        }
        currentJson = frontStack.pop();
        mappingData(currentJson);
    }
}

function prePostData(){

    if(currentJson == null){ //최초로 눌렀을 때
        alert("마지막 입니다.")
    }else if (!backStack.isEmpty()){ //backStack 에 뭐가 있을 때

        if(currentJson.isBlocked == null || currentJson.isBlocked === 0){
            frontStack.push(currentJson); //지금 보던거 frontStack 으로 보내기.
        }
        currentJson = backStack.pop(); //backStack 의 헤드를 커런트로 옮기기
        mappingData(currentJson); // 화면에 쏴주기

    }else{ // backStack 에 아무것도 없을 때(계속 뒤로 누를 때)
        alert("마지막 입니다.")
    }
}


//Json 요소를 페이지에 뿌려줌.
function mappingData(data){
    // JSON 데이터로 th:text가 적용된 부분 업데이트
    document.getElementById('postId').textContent = data.id;
    document.getElementById('title').textContent = data.title;
    document.getElementById('profileImageURL').src = data.profileImageURL;
    document.getElementById('userName').textContent = data.userName;
    document.getElementById('introduce').textContent = data.introduce;
    document.getElementById('likeCount').textContent = data.likeCount;
    document.getElementById('comment').textContent = data.comment;
    document.getElementById('sourceCode').textContent = data.sourceCode;
    loadingLike(data.isLike);
}


//페이지에서 멤버와 포스트간 좋아요 표시
function loadingLike(isLike){
    if(isLike === 1){
        likeButton.classList.add("liked");
        likeButton.style.backgroundColor = "magenta";
        likeButton.style.color = "white";
    }else{
        likeButton.classList.remove("liked");
        likeButton.style.backgroundColor = "";
        likeButton.style.color = "";
    }
}

function closeDrawer() {
    const drawerCheckbox = document.getElementById("settings-drawer");
    drawerCheckbox.checked = false; // 드로어 닫기
}

function fetchSwipeJsonData(){
    return fetch('/swipe/jsons')
        .then(response => {
            if(!response.ok){
                throw new Error('네트워크 응답에 문제가 있습니다.');
            }
            return response.json();
        })
        .then(data => {
            if(data.isBlocked === 1 ){
                return fetchSwipeJsonData(); // 나올때까지
            }
            return data;
        })
}

