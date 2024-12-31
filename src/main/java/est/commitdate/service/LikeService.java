package est.commitdate.service;


import est.commitdate.entity.Like;
import est.commitdate.entity.Member;
import est.commitdate.entity.Post;
import est.commitdate.repository.LikeRepository;
import est.commitdate.service.member.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final MemberService memberService;


    public int getIsLike(HttpSession session, Long id){

        Member member = memberService.getLoggedInMember(session);

        boolean isLike = likeRepository.findByMemberIdAndPostPostId(member.getId(), id).isPresent();

        if(isLike){
            return 1;
        }
        return 0;

    }


}
