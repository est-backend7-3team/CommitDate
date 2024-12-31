package est.commitdate.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RandomCodes {

    public String code;

    public String getCodes(int number){

        switch (number){
            case 1:
                code = """
                                // 취소버튼 \n(Test)이벤트
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
                                    likeCount.textContent = Number(likeCount.textContent) - 1;
                                } else {
                                    likeButton.classList.add("liked");
                                    likeCount.textContent = parseInt(likeCount.textContent) + 1;
                                }
                            }
                        })
                        .catch(error => {
                            console.error("Error:", error);
                        });
                }
                
                """;
                break;

            case 2:
                code = """
                    <!DOCTYPE html>
                    <html xmlns:th="http://www.thymeleaf.org" lang="ko" >
                    <head>
                      <meta charset="UTF-8">
                      <meta name="viewport" content="width=device-width, initial-scale=1.0">
                      <title>커밋데이트</title>
                      <link href="https://cdn.jsdelivr.net/npm/daisyui@4.10.2/dist/full.min.css" rel="stylesheet" type="text/css" />
                      <script src="https://cdn.tailwindcss.com"></script>
                      <script src="/js/swipe.js" type="module" defer></script>
                      <style>
                      </style>
                    </head>
                    <body class="bg-gray-100 grid grid-cols-[1fr_2fr_1fr] items-center relative h-screen min-h-0 min-w-0" >
                    
                    <!-- 왼쪽 버튼 (흰색 카드 외부, 녹색 배경 위) -->
                    
                    <div class="relative w-full h-64 flex items-center justify-center bg-gray-100">
                    <button id = 'leftButton' class="btn btn-circle btn-ghost btn-lg absolute top-3/5">
                      <svg xmlns="http://www.w3.org/2000/svg"
                           class="h-12 w-12"
                           fill="none"
                           viewBox="0 0 24 24"
                           stroke="currentColor">
                        <path stroke-linecap="round"
                              stroke-linejoin="round"
                              stroke-width="2"
                              d="M15 19l-7-7 7-7" />
                      </svg>
                    </button>
                    </div>
                    
                    """;
                    break;
            case 3:
                code = """
                    @Slf4j
                    @Controller
                    @RequiredArgsConstructor
                    @RequestMapping("/choose")
                    public class ChooseController {
                    
                        private final ChooseService chooseService;
                        private final SwipeService swipeService;
                    
                        @GetMapping("/{id}")//최초들어오는 부분
                        public String getChoicePage(Model model, @PathVariable String id, HttpSession session) {
                            if ("null".equals(id) || id == null || id.isBlank()) {
                                return null;
                            }
                    
                    
                            log.info("Received id: {}",id);
                            model.addAttribute("id", Long.valueOf(id));
                            return "view/choose";
                        }
                    
                        @GetMapping("/likes/{id}")//최초들어오는 부분
                        public String getLikesPage(Model model, @PathVariable String id, HttpSession session) {
                            if ("null".equals(id) || id == null || id.isBlank()) {
                                return null;
                            }
                            log.info("Received MemberId: {}",id);
                            model.addAttribute("id", Long.valueOf(id));
                            return "view/likeList";
                        }
                    
                        @ResponseBody
                        @PostMapping("/Jsons")
                        public List<ChooseDto> getJsons(@RequestBody Map<String,Object> payload, HttpSession session) {
                            return chooseService.getChooseDtos(payload, session);
                        }
                    
                        @ResponseBody
                        @PostMapping("/likeJsons")
                        public List<LikeDto> getLikeJsons(@RequestBody Map<String,Object> payload, HttpSession session) {
                            return chooseService.getLikeDtos(payload, session);
                        }
                """;
                break;

            case 4:
                   code = """
                    printf("고고고\n123411234\nsdlkfjlwekjf");
                    """;
                break;
        }

        return code;
    };




}
