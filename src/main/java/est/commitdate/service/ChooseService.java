package est.commitdate.service;

import est.commitdate.dto.like.LikeDto;
import est.commitdate.dto.swipe.ChooseDto;
import est.commitdate.entity.Like;
import est.commitdate.entity.Member;
import est.commitdate.entity.Post;
import est.commitdate.repository.LikeRepository;
import est.commitdate.repository.MemberRepository;
import est.commitdate.repository.PostRepository;
import est.commitdate.service.member.MemberService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChooseService {

    private final PostRepository postRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final LikeRepository likeRepository;


    public List<ChooseDto> getChooseDtos(Map<String,Object> payload, HttpSession session) {


        log.info("[getChooseDtos] postId 파싱준비. 값={}", payload.get("postId"));
        Long postId = Long.valueOf((String) payload.get("postId"));

        log.info("[getChooseDtos] postId 파싱완료. 값={}", postId);

        Member loggedInMember = memberService.getLoggedInMember(session);

        List<ChooseDto> chooseDtos = new ArrayList<>();


        postRepository.findById(postId).ifPresent(post -> {

            if (loggedInMember == null) {
                return; //anonymous 의 요청
            } else if (!loggedInMember.equals(post.getMember())) {
                return; //올바르지않은 사용자의 요청
            }

            post.getLikes().forEach(like -> {

                Member member = like.getMember();

                ChooseDto chooseDto = new ChooseDto();

                chooseDto.setUserId(String.valueOf(member.getId()));
                chooseDto.setUserName(member.getNickname());
                chooseDto.setComment(member.getComment());
                chooseDto.setProfileImageURL(member.getProfileImage());
                chooseDto.setTimestamp(like.getLikeDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                chooseDto.setLikeId(like.getLikeId());
                chooseDto.setMatchingResult(like.getMatchingResult());
                chooseDto.setPostId(like.getPost().getPostId());


                chooseDtos.add(chooseDto);
            });


        });

        return chooseDtos;

    }

    public void makeTestLike(){
        Member findMember;
        Post findPost = postRepository.findByPostId(500L) //내가 쓴 게시물 id = 500번.
                .orElseThrow(EntityNotFoundException::new);

        Random r = new Random(System.currentTimeMillis());


        for (int i = 0; i < 300; i++) {
            findMember = memberRepository.findById(r.nextLong(19) + 1)
                    .orElseThrow(EntityNotFoundException::new);

            Like likeEntity = Like.of(findMember,findPost);
            likeRepository.save(likeEntity);

        }




    }

    public List<LikeDto> getLikeDtos(Map<String, Object> payload, HttpSession session) {

        List<LikeDto> likeDtos = new ArrayList<>();
        log.info("[getLikeDtos] userId 파싱준비. 값={}", payload.get("userId"));
        Long userId = Long.valueOf((String) payload.get("userId"));
        log.info("[getLikeDtos] userId 파싱완료. 값={}", userId);
        Member loggedInMember = memberService.getLoggedInMember(session);

        if (loggedInMember == null) {
            return null; //anonymous 의 요청
        }

        Member findMember = memberRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        if (!findMember.equals(loggedInMember)) {
            return null; //수상한 세션: 불일치
        }

        loggedInMember.getLikes().forEach(like -> {
            likeDtos.add(LikeDto.from(like));
        });

        return likeDtos;
    }
}
