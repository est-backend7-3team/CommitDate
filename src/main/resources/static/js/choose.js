const list = document.getElementById("list");

document.addEventListener('DOMContentLoaded',()=>{

   const postId = document.getElementById('postId').textContent.trim();

   fetch("/choose/Jsons",{
      method: "POST",
      headers: {
         "Content-Type":"application/json"
      },
      body: JSON.stringify({postId: postId})
   })
       .then(response => {
          console.log(response);
          if(!response.ok){
             const result = confirm("권한이 없습니다.");
             if(result){
                window.location.href = "http://localhost:8080/login";
                throw new Error("Unauthorized");
             } else {
                throw new Error("Unauthorized");
             }
          }
          return response.json();
       })
       .then(chooseDtoList =>{

          chooseDtoList.forEach(chooseDto => {
             createItem(chooseDto);
          });

       })
});




function createItem(chooseDto){

   const itemDiv = document.createElement("div");
   itemDiv.className = "flex items-center bg-white shadow-md rounded-lg p-4 border";

   itemDiv.innerHTML = `
    <div id="suitorId" hidden="hidden">${chooseDto.userId}</div>
    <div class="avatar">
      <div class="avatar">
        <div class="w-12 rounded-full" >
            <img id="profileImageURL" src = "https://img.daisyui.com/images/stock/photo-1534528741775-53994a69daeb.webp" alt="User Avatar"/>
<!--             src="{chooseDto.profileImageURL}"-->
             
              
        </div>
      </div>
    </div>
    <div class="ml-4 flex-1">
      <p id="suitorTime" class="text-sm text-gray-500 mb-1">${chooseDto.timestamp}</p>
      <p class="text-gray-800 font-semibold mb-1">
        <span id="suitor" class="font-bold">${chooseDto.userName}</span>
      </p>
      <p id="suitorComment" class="text-gray-600 text-sm">${chooseDto.comment}</p>
    </div>
    <button class="btn btn-primary shadow-lg bg-pink-500 border-none rounded-full text-white" data-suitor-id="${chooseDto.userId}">
      수락
    </button>
  `;

   // 알림 리스트에 추가
   list.appendChild(itemDiv);
}