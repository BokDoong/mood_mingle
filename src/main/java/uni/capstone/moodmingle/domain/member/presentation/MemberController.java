package uni.capstone.moodmingle.domain.member.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uni.capstone.moodmingle.domain.member.application.MemberCommandService;
import uni.capstone.moodmingle.domain.member.application.MemberQueryService;
import uni.capstone.moodmingle.domain.member.application.dto.response.MemberInfo;
import uni.capstone.moodmingle.domain.member.application.dto.response.TokenResponse;
import uni.capstone.moodmingle.global.security.jwt.entity.JwtUserDetails;
import uni.capstone.moodmingle.global.security.oidc.entity.OidcUserInfo;
import uni.capstone.moodmingle.domain.member.presentation.dto.MemberDtoMapper;
import uni.capstone.moodmingle.domain.member.presentation.dto.request.MemberCreateDto;
import uni.capstone.moodmingle.domain.member.presentation.dto.request.TokenReissueDto;

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
    private final MemberCommandService memberCommandService;
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
        memberCommandService.withdraw(userDetails.getUserId());
    }

    /**
     * 소셜 회원가입
     *
     * @param oidcUserInfo Oidc 인증 엔티티
     * @return 액세스 토큰+리프레쉬 토큰
     */
    @PostMapping("/join/{authServer}")
    public TokenResponse socialJoin(@PathVariable("authServer") String authServer, @AuthenticationPrincipal OidcUserInfo oidcUserInfo) {
        return memberCommandService.register(mapper.toCommand(oidcUserInfo));
    }

    /**
     * 카카오 로그인
     *
     * @param oidcUserInfo Oidc 인증 엔티티
     * @return 액세스 토큰+리프레쉬 토큰
     */
    @PostMapping("/login/kakao")
    public TokenResponse kakaoLogin(@AuthenticationPrincipal OidcUserInfo oidcUserInfo) {
        return memberCommandService.login(oidcUserInfo.getEmail());
    }

    /**
     * 기본 회원가입
     */
    @PostMapping("/basic-join")
    public TokenResponse join(@RequestBody MemberCreateDto dto) {
        return memberCommandService.register(mapper.toCommand(dto));
    }

    /**
     * 로그인
     *
     */
    @PostMapping("/basic-login")
    public TokenResponse login(@RequestParam("email") String email) {
        return memberCommandService.login(email);
    }

    /**
     * 토큰 재발급
     *
     * @param dto 액세스 토큰, 리프레쉬 토회
     * @return 액세스 토큰+리프레쉬 토큰
     */
    @PostMapping("/reissue")
    public TokenResponse reissue(@RequestBody TokenReissueDto dto) {
        return memberCommandService.reissue(dto.getRefreshToken());
    }

    /**
     * 로그아웃
     *
     * @param userDetails
     */
    @PostMapping("/logout")
    public void logout(@AuthenticationPrincipal JwtUserDetails userDetails) {
        memberCommandService.logout(userDetails.getUserId());
    }
}
