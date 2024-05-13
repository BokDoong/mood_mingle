package uni.capstone.moodmingle.member.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uni.capstone.moodmingle.member.application.MemberQueryService;
import uni.capstone.moodmingle.member.application.dto.response.MemberInfo;

/**
 * 멤버 도메인 컨트롤러
 *
 * @author ijin
 */
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberQueryService memberQueryService;

    @GetMapping("/api/v1/member/{id}")
    public MemberInfo getMemberInfo(@PathVariable(name = "id") Long id) {
        return memberQueryService.findMember(id);
    }
}
