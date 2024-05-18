package uni.capstone.moodmingle.config.security.oidc.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * 인증된 Oidc User
 *
 * @author ijin
 */
@Getter
@RequiredArgsConstructor
public class OidcUserInfo implements UserDetails {

    private String nickname;
    private String email;
    private String picture;

    public OidcUserInfo(String nickname, String email, String picture) {
        this.nickname = nickname;
        this.email = email;
        this.picture = picture;
    }

    /**
     * 권한 반환 - Oidc User 이기 때문에 Null 리턴
     *
     * @return null
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
