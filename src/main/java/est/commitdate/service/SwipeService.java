package est.commitdate.service;

import est.commitdate.dto.swipe.ChooseDto;
import est.commitdate.dto.swipe.SwipeDto;
import est.commitdate.entity.Board;
import est.commitdate.entity.Member;
import est.commitdate.entity.Post;
import est.commitdate.repository.BoardRepository;
import est.commitdate.repository.MemberRepository;
import est.commitdate.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class SwipeService {


    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final PostRepository postRepository;


    public SwipeDto getDummyDTO(){
        //예제

        return SwipeDto.builder()
                .title("스프링하실 새럼~?")
                .profileImageURL("https://via.placeholder.com/48")
                .userName("싱글개발자")
                .introduce("백엔드 7 년차 개발자")
                .likeCount(76)
                .comment("장고, 리액트, 카프카, 도커")
                .sourceCode("public static void main(String[] args) {\n    System.out.println(\"Hello, World\");\n}<div class=\"flex items-center mb-4\">\n" +
                        "    <div class=\"avatar\">\n" +
                        "      <div class=\"w-12 rounded-full\">\n" +
                        "        <img th:src=\"${swipeDTO.profileImageURL}\" alt=\"User Avatar\">\n" +
                        "      </div>\n" +
                        "    </div>\n" +
                        "    <div class=\"ml-3\">\n" +
                        "      <p class=\"font-semibold\" th:text=\"${swipeDTO.userName}\">k User</p>\n" +
                        "      <p class=\"text-gray-500 text-sm\" th:text=\"${swipeDTO.introduce}\">소개글</p>\n" +
                        "    </div>\n" +
                        "    <div class=\"ml-auto flex items-center\">\n" +
                        "      <span class=\"mr-1 text-lg\" th:text=\"${swipeDTO.likeCount}\">8</span>\n" +
                        "      <span>\uD83D\uDC93</span>\n" +
                        "    </div>\n" +
                        "  </div>") //예제
                .build();
    }

    public List<ChooseDto> getDummyChooseDTO(int volume){

        List<ChooseDto> chooseDTOList = new ArrayList<>();

        for (int i = 0; i < volume; i++) {
            chooseDTOList.add(ChooseDto.builder()
                                .userName("username"+i)
                                .comment("comment"+i)
                                .profileImageURL("https://via.placeholder.com/48")
                                .timestamp(1)
                                .build()
            );
        }

        return chooseDTOList;
    }

    //postEntity 를 SwipeDto 로 변환
    public SwipeDto postToSwipeDto(Post post){

        SwipeDto swipeDto = new SwipeDto();

        swipeDto.setId(post.getPostId());
        swipeDto.setTitle(post.getTitle());
        swipeDto.setProfileImageURL(post.getMember().getProfileImage());
        swipeDto.setUserName(post.getMember().getUsername());
        swipeDto.setIntroduce(post.getMember().getIntroduce());
        swipeDto.setLikeCount(post.getLikeCount());
        swipeDto.setComment(post.getMember().getComment());
        swipeDto.setSourceCode(post.getText());

        return swipeDto;
    }


    public void testInput2(int randNum){

        Member member = Member.builder()
                .password("1234")
                .email("test@gmail.com")
                .nickname("test")
                .username("사람이름")
                .phoneNumber("010-5261-7904")
                .role("MEMBER")
                .profileImage(null)
                .introduce("안녕하세요라리 진짜 이거 개쩔지 않냐 크흐 ;;;   ㅗㅁㄴ아러ㅣ;자더ㅏㅣ;렂;디ㅓㄹ")
                .comment("쌔끈빠끈한 제 키보드좀 보세요")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(1)
                .build();

        //member 저장
        memberRepository.save(member);

        //Board 엔티티 생성
        Board board = Board.builder()
                .boardName("스와이프")
                .status(1)
                .build();

        boardRepository.save(board);

        //Post 엔티티 생성
        Post tmpPost;

        for (int i = 1; i < randNum; i++) {
        tmpPost = Post.builder()
                .board(board)
                .title("허허허 좋십니더.."+i)
                .text("저랑 같이 Hello World? 하실분?"+i)
                .description("TEEEEEEEST"+i)
                .member(member)
                .likeCount(i)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(1)
                .build();

        postRepository.save(tmpPost);

        }

    }

    public void testInput(int randNum){

        Post post = null;

        try {
            post = postRepository.findById(1L).orElseThrow(EntityNotFoundException::new);
        }catch(EntityNotFoundException e){
            testInput2(randNum);
            log.info(e.getMessage());
            log.info("엔티티 없음");
       }

        if(post != null){
            return;
        }

    }

}
