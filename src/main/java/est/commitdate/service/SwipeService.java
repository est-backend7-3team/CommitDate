package est.commitdate.service;

import est.commitdate.dto.member.CustomUserDetails;
import est.commitdate.dto.member.FormUserDetails;
import est.commitdate.dto.swipe.ChooseDto;
import est.commitdate.entity.Like;
import est.commitdate.entity.Member;
import est.commitdate.entity.Post;
import est.commitdate.repository.BoardRepository;
import est.commitdate.repository.LikeRepository;
import est.commitdate.repository.MemberRepository;
import est.commitdate.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class SwipeService {


    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;



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

     public String toggleLike(Map<String, Object> postJson, HttpSession session) {

        // 로그인한 사용자 정보 추출
        Member member;
        try { //비 로그인자는 여기서 Exception 이 터짐.
            member = getLoggedInMember(session);
        } catch (RuntimeException e){
            log.info(e.getMessage());
            return "AccessDenied";
        }

        // postId 추출 및 변환
        Long postId = Long.valueOf((String) postJson.get("postId"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        // 중복된 Like 엔티티 확인 (status = 0인 경우)
        Optional<Like> duplicateLikeEntity = likeRepository.findByMemberIdAndPostPostIdAndStatus(member.getId(), postId);

        if (duplicateLikeEntity.isPresent()) {
            // 중복된 엔티티가 존재하면 복구
            duplicateLikeEntity.get().restore();
            return "LikeSuccess";
        }

        // 중복 확인 (status 조건 없이) status 가 1이고, 이미 조회하려는게 있다면, 좋아요를 취소하는 행위
         Optional<Like> byMemberIdAndPostPostId = likeRepository.findByMemberIdAndPostPostId(member.getId(), postId);
         if (byMemberIdAndPostPostId.isPresent()) {
             byMemberIdAndPostPostId.get().delete();
            return "CancelSuccess"; // 중복된 경우 삽입하지 않음
        }

        // 중복없고 완전 새로운 상태라면
        likeRepository.save(Like.of(member, post));
        return "LikeSuccess";
    }

    public Member getLoggedInMember(HttpSession session) {
        // 시큐리티 컨텍스트에서 사용자 정보 추출
        SecurityContext securityContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");

        //시큐리티에 머 있으면 즉, 로그인을 한 것이라면
        if (securityContext != null) {
            Object principal = securityContext.getAuthentication().getPrincipal();
            if (principal instanceof CustomUserDetails customUser) {
                return memberRepository.findById(customUser.getId())
                        .orElseThrow(() -> new EntityNotFoundException("Member not found"));
            } else if (principal instanceof FormUserDetails formUser) {
                return memberRepository.findById(formUser.getId())
                        .orElseThrow(() -> new EntityNotFoundException("Member not found"));
            }else{//로그인은 했는데 이상한것. RuntimeException 터트림.
                throw new RuntimeException("Unauthorized: Unable to identify the user");
            }
        }
        //손님 유저
        log.info("Anonymous");
        return null;
    }

    public Post getRandomPost(int range) {
        range--;
        Random r = new Random(System.currentTimeMillis());

        Post post =  postRepository.findByPostId(r.nextLong(range)+1).orElseThrow(
                () -> new EntityNotFoundException("Post not found")
        );

        return post;
    }

    //유저가 해당 포스트를 좋아요했는지 검사하는 메서드
    public Integer isLike(Member member, Post post) {
        //찾기
        Optional<Like> byMemberIdAndPostPostId = likeRepository.findByMemberIdAndPostPostId(member.getId(), post.getPostId());

        //해당 정보의 좋아요 정보가 있다면
        if (byMemberIdAndPostPostId.isPresent()) {
            //1반환
            return 1;
        }
        //없을 때
        return 0;
    }

}
