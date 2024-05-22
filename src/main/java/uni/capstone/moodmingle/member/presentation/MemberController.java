package uni.capstone.moodmingle.member.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
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
@RequestMapping("/api/v1/member")
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
    @GetMapping()
    public MemberInfo getMemberInfo(@AuthenticationPrincipal JwtUserDetails userDetails) {
        return memberQueryService.findMemberInfo(userDetails.getUserId());
    }

    /**
     * 탈퇴
     *
     * @param userDetails
     */
    @DeleteMapping()
    public void withdraw(@AuthenticationPrincipal JwtUserDetails userDetails) {
        loginService.withdraw(userDetails.getUserId());
    }

    /**
     * 회원가입
     *
     * @param oidcUserInfo Oidc 인증 엔티티
     * @return 액세스 토큰+리프레쉬 토큰
     */
    @PostMapping("/join/{authServer}")
    public TokenResponse join(@PathVariable("authServer") String authServer, @AuthenticationPrincipal OidcUserInfo oidcUserInfo) {
        return loginService.register(mapper.toCommand(oidcUserInfo));
    }

    /**
     * 카카오 로그인
     *
     * @param oidcUserInfo Oidc 인증 엔티티
     * @return 액세스 토큰+리프레쉬 토큰
     */
    @PostMapping("/login/kakao")
    public TokenResponse kakaoLogin(@AuthenticationPrincipal OidcUserInfo oidcUserInfo) {
        return loginService.kakaoLogin(oidcUserInfo.getEmail());
    }

    /**
     * 애플 회원가입 및 로그ㅈㅌ
     *
     * @param oidcUserInfo Oidc 인증 엔티티
     * @return 액세스 토큰+리프레쉬 토큰
     */
    @PostMapping("/login/apple")
    public TokenResponse appleLogin(@AuthenticationPrincipal OidcUserInfo oidcUserInfo,
                                    @RequestParam(value = "user", required = false) String name) {
        return loginService.appleLogin(oidcUserInfo.getEmail(), name);
    }

    /**
     * 토큰 재발급
     *
     * @param dto 액세스 토큰, 리프레쉬 토회
     * @return 액세스 토큰+리프레쉬 토큰
     */
    @PostMapping("/reissue")
    public TokenResponse reissue(@RequestBody TokenReissueDto dto) {
        return loginService.reissue(dto.getRefreshToken());
    }

    /**
     * 로그아웃
     *
     * @param userDetails
     */
    @PostMapping("/logout")
    public void logout(@AuthenticationPrincipal JwtUserDetails userDetails) {
        loginService.logout(userDetails.getUserId());
    }
}
