import { likeFetch } from './like.js';

const likeButton = document.getElementById('likeButton');
const likeCount = document.getElementById('likeCount');

document.addEventListener('DOMContentLoaded', () => {
    likeButton.addEventListener('click',()=>toggleLike(likeButton));
})

function toggleLike(likeButton) {
    const postId = document.getElementById('postId').textContent.trim();



    likeFetch(postId)
        .then(status => {
            // 상태에 따라 버튼 스타일 업데이트
            if (status === "Success") {
                if (likeButton.classList.contains("liked")) {
                    likeButton.classList.remove("liked");
                    likeButton.style.backgroundColor = "";
                    likeButton.style.color = "";
                    likeCount.textContent = Number(likeCount.textContent) - 1;
                } else {
                    likeButton.classList.add("liked");
                    likeButton.style.backgroundColor = "magenta";
                    likeButton.style.color = "white";
                    likeCount.textContent = parseInt(likeCount.textContent) + 1;
                }
            }
        })
        .catch(error => {
            console.error("Error:", error);
        });
}