document.addEventListener('DOMContentLoaded', () => {
    const userId = document.getElementById('userId').textContent.trim();
    console.log(userId);
    fetch('/choose/likeJsons', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ userId: userId })
    })
        .then(response => {
            if (!response.ok) {
                const result = confirm("권한이 없습니다.");
                if (result) {
                    window.location.href = "http://localhost:8080/login";
                }
                throw new Error("Unauthorized");
            }
            return response.json();
        })
        .then(likeDtoList => {
            likeDtoList.forEach(likeDto => {
                createItem(likeDto);
            });
        })
        .catch(err => console.error(err));
});


// 1) DTO 객체를 받아서 '전체 HTML' 문자열을 만드는 함수
function createItemHTML(likeDto) {
    // matchingResult === 0 이면 수락 버튼, 아니면 입장하기 버튼
    if (likeDto.matchingResult === 0) {
        return `
       <div class="avatar">
        <div class="w-12 rounded-full">
          <img src="${likeDto.profileImageUrl}" alt="User Avatar"/>
        </div>
      </div>
      <div class="ml-4 flex-1">
        <p class="text-sm text-gray-500 mb-1">${likeDto.timestamp}</p>
        <p class="text-gray-800 font-semibold mb-1">
          <span class="font-bold">${likeDto.receiveName}</span>
        </p>
        <a class="w-26 border-none text-gray-800" href="/post/view/${likeDto.postId}">
            ${likeDto.postTitle}
        </a>
      </div>
      <div class="buttonSection">
      <!--  <form> -->
      <!-- <input type="hidden" name="likeId" value="${likeDto.likeId}" /> -->
        <button id="acceptButton" class="btn btn-primary bg-pink-500 border-none rounded-full text-white">
          대기 중
        </button>
      <!--</form>  -->
      </div>
    `;
    } else {
        // matchingResult === 1 이라면 '입장하기' 버튼 (form)
        return `
      <div class="avatar">
        <div class="w-12 rounded-full">
          <img src="${likeDto.profileImageUrl}" alt="User Avatar"/>
        </div>
      </div>
      <div class="ml-4 flex-1">
        <p class="text-sm text-gray-500 mb-1">${likeDto.timestamp}</p>
        <p class="text-gray-800 font-semibold mb-1">
          <span class="font-bold">${likeDto.receiveName}</span>
        </p>
        <a class="w-26 border-none text-gray-800" href="/post/view/${likeDto.postId}">
            ${likeDto.postTitle}
        </a>
      </div>
      <div class="buttonSection flex flex-content ">
        <form action="/chat" method="post">
          <input type="hidden" name="likeId" value="${likeDto.likeId}" />
          <button class="w-16 btn btn-primary bg-pink-500 border-none rounded-full text-white" type="submit">
            입장
          </button>
        </form>
        
        <form class = "pl-1" action="/chat/reject" method="post">
        <input type="hidden" name="likeId" value="${likeDto.likeId}" />
          <button class="w-18  btn btn-primary bg-gray-300 border-none rounded-full" type="submit">
            나가기
          </button>
        </form>
      </div>
    `;
    }
}


// 2) 실제 DOM 요소를 만들고, 이벤트 바인딩까지 해주는 함수
function createItem(likeDto) {
    const list = document.getElementById("list");
    // itemDiv 라는 컨테이너를 만든다.
    const itemDiv = document.createElement("div");
    itemDiv.className = "flex items-center bg-white shadow-md rounded-lg p-4 border";

    // matchingResult 상태에 맞게 안쪽 HTML을 만든다.
    itemDiv.innerHTML = createItemHTML(likeDto);

    // 만약 matchingResult 가 0이면 '채팅' 버튼이 있을 것이므로 이벤트 달아준다.
    if (likeDto.matchingResult === 0) {
        const acceptButton = itemDiv.querySelector("#acceptButton");
        acceptButton.addEventListener("click", () => {
            // (1) 서버에 '수락' 알림을 보내거나, matchingResult 를 1로 바꾸는 API 호출
            // fetch('/choose/accept', {...}) 등으로 DB에 반영할 수도 있음
            // 여기서는 단순히 'UI만' 바뀐다고 가정
            // likeDto.matchingResult = 0;
            fetch('/swipe/api/requestMatchingResult', {
                method: "POST",
                headers:{
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(likeDto)
            }).then(response =>{
               if(!response.ok){
                   alert("예기치 못한 에러!")
               }
               return response.test();
            }).then(message => {
                if(message === "Success"){
                    console.log("Success")
                }else if(message => "로그인이 필요합니다."){
                    window.location.href = '/swipe/api/requestMatchingResult';
                }else{
                    alert("문제가 생겼습니다.")
                }
            });

            // (2) 기존 아이템 내용 전체를 다시 '입장하기' 버튼 형태로 교체
            itemDiv.innerHTML = createItemHTML(likeDto);
        });
    }

    // 완성된 요소를 #list에 붙인다.
    list.appendChild(itemDiv);
}
