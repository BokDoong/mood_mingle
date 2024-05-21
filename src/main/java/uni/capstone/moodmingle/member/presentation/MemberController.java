package uni.capstone.moodmingle.member.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uni.capstone.moodmingle.config.security.jwt.entity.JwtUserDetails;
import uni.capstone.moodmingle.config.security.oidc.entity.OidcUserInfo;
import uni.capstone.moodmingle.member.application.LoginService;
import uni.capstone.moodmingle.member.application.MemberQueryService;
import uni.capstone.moodmingle.member.application.dto.response.MemberInfo;
import uni.capstone.moodmingle.member.application.dto.response.TokenResponse;
import uni.capstone.moodmingle.member.presentation.dto.MemberDtoMapper;
import uni.capstone.moodmingle.member.presentation.dto.request.TokenReissueDto;

/**
 * 멤버 도메인 컨트롤러
 *
 * @author ijin
 */
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberQueryService memberQueryService;
    private final LoginService loginService;
    private final MemberDtoMapper mapper;

    /**
     * 회원정보 조회
     *
     * @param userDetails Jwt 인증 엔티티
     * @return 회원 정보
     */
    @GetMapping("/api/v1/member")
    public MemberInfo getMemberInfo(@AuthenticationPrincipal JwtUserDetails userDetails) {
        return memberQueryService.findMemberInfo(userDetails.getUserId());
    }

    /**
     * 회원가입
     *
     * @param oidcUserInfo Oidc 인증 엔티티
     * @return 액세스 토큰+리프레쉬 토큰
     */
    @PostMapping("/api/v1/member/join")
    public TokenResponse join(@AuthenticationPrincipal OidcUserInfo oidcUserInfo) {
        return loginService.register(mapper.toCommand(oidcUserInfo));
    }

    /**
     * 로그인
     *
     * @param oidcUserInfo Oidc 인증 엔티티
     * @return 액세스 토큰+리프레쉬 토큰
     */
    @PostMapping("/api/v1/member/login")
    public TokenResponse login(@AuthenticationPrincipal OidcUserInfo oidcUserInfo) {
        return loginService.login(oidcUserInfo.getEmail());
    }

    /**
     * 토큰 재발급
     *
     * @param dto 액세스 토큰, 리프레쉬 토회
     * @return 액세스 토큰+리프레쉬 토큰
     */
    @PostMapping("/api/v1/member/reissue")
    public TokenResponse reissue(@RequestBody TokenReissueDto dto) {
        return loginService.reissue(dto.getRefreshToken());
    }

    /**
     * 로그아웃
     *
     * @param userDetails
     */
    @PostMapping("/api/v1/member/logout")
    public void logout(@AuthenticationPrincipal JwtUserDetails userDetails) {
        loginService.logout(userDetails.getUserId());
    }
}
