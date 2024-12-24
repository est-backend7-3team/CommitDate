import { likeFetch, removeFetch, editFetch } from './Fetch.js';

const likeButton = document.getElementById('likeButton');
const likeCount = document.getElementById('likeCount');
const postId = document.getElementById('postId').textContent.trim();

document.addEventListener('DOMContentLoaded',  () =>{

    likeButton.addEventListener('click',()=>toggleLike(likeButton));


    // 수정 버튼 이벤트 핸들러
    const editButtons = document.querySelectorAll('.edit-comment-btn');
    editButtons.forEach(button => {
        button.addEventListener('click', function () {
            const commentId = this.getAttribute('data-comment-id'); // commentId 가져오기
            const commentElement = document.querySelector(`li.comment-item[data-comment-id="${commentId}"]`); // 해당 댓글 요소

            // 수정 로직
            if (commentElement) {

                //기존 버튼 가져오기
                const editButton = commentElement.querySelector('.edit-comment-btn');
                const removeButton = commentElement.querySelector('.delete-comment-btn'); //기존 댓글 저장
                const datetime  = commentElement.querySelector('.comment-date');

                // 기존 댓글 내용을 가져오기
                const contentElement = commentElement.querySelector('.comment-content');
                const originalContent = contentElement.textContent; //기존 댓글 저장



                //댓글 수정 폼 만들기
                const editForm = document.createElement('div');

                editForm.innerHTML = `
                 
                <div class="comment-form">
                        <textarea class="edit-textarea" placeholder="${originalContent}" required></textarea>
                        <button class="save-edit-btn">저장</button>
                        <button class="cancel-edit-btn">취소</button>
                </div>
                `;

                //그 안에 수정폼 삽입.
                commentElement.appendChild(editForm);

                //기존 댓글 내용 숨기기
                contentElement.style.display = `none`;
                editButton.style.display = `none`;
                removeButton.style.display = `none`;
                datetime.style.display = `none`;

                //저장 버튼 이벤트
                editForm.querySelector('.save-edit-btn').addEventListener(`click`, () => {
                    const editText = editForm.querySelector(`.edit-textarea`).value;

                    const scrollPosition = window.scrollY;
                    //서버로Fetch 보내기
                    editFetch(editText, commentId)
                        .then(status => {
                            if (status === "success") {
                                alert("수정되었습니다.")
                                // window.location.href = `/post/view/${postId}`
                                // document.querySelector(`.comment-item[data-comment-id="${commentId}"]`).remove();
                                // window.scrollTo(0, scrollPosition);
                                contentElement.textContent = editText;
                                contentElement.style.display = ''; // 기존 댓글 내용 다시 보여주기
                                editButton.style.display = ``;
                                removeButton.style.display = ``;
                                datetime.style.display = ``;
                                editForm.remove();
                            } else if (status === "accessDenied_작성자불일치") {
                                alert("타인의 댓글은 수정할 수 없습니다.")
                                contentElement.style.display = ''; // 기존 댓글 내용 다시 보여주기
                                editButton.style.display = ``;
                                removeButton.style.display = ``;
                                datetime.style.display = ``;
                                editForm.remove(); // 수정 폼 제거
                            } else {
                                alert("로그인이 필요한 서비스입니다.")
                                //로그인으로 보내기
                                contentElement.style.display = ''; // 기존 댓글 내용 다시 보여주기
                                editButton.style.display = ``;
                                removeButton.style.display = ``;
                                datetime.style.display = ``;
                                editForm.remove(); // 수정 폼 제거
                            }
                        });

                });

                // 취소버튼 이벤트
                editForm.querySelector('.cancel-edit-btn').addEventListener('click', () => {
                    contentElement.style.display = ''; // 기존 댓글 내용 다시 보여주기
                    editButton.style.display = ``;
                    removeButton.style.display = ``;
                    datetime.style.display = ``;
                    editForm.remove(); // 수정 폼 제거
                });
            }
        })
    })
});

    // 삭제 버튼 이벤트 핸들러
    const deleteButtons = document.querySelectorAll('.delete-comment-btn');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function () {
            const commentId = this.getAttribute('data-comment-id'); // commentId 가져오기

            // 현재 스크롤 위치 저장
            const scrollPosition = window.scrollY;

            removeFetch(postId,commentId)
                .then(status => {
                    if(status === "success"){
                        alert("삭제되었습니다.")
                        // window.location.href = `/post/view/${postId}`
                        document.querySelector(`.comment-item[data-comment-id="${commentId}"]`).remove();
                        window.scrollTo(0, scrollPosition);
                    }else if(status === "accessDenied_작성자불일치"){
                        alert("타인의 댓글은 삭제할 수 없습니다.")
                    }else{
                        confirm("로그인이 필요한 서비스입니다. 로그인 페이지로 이동하시겠습니까?")
                    }
                })

        });
    })


//토글 메서드
function toggleLike(likeButton) {
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

