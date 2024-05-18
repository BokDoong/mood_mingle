package uni.capstone.moodmingle.config.security.jwt.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 인증된 Jwt User
 *
 * @author ijin
 */
@Getter
@RequiredArgsConstructor
public class JwtUserDetails implements UserDetails {

    private long userId;

    public JwtUserDetails(long userId) {
        this.userId = userId;
    }

    /**
     * ROLE_USER 권한 반환 - 관리자 권한이 없기 때문에 무조건 ROLE_USER 반환
     *
     * @return ROLE_USER 이 담긴 Collection
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return authorities;
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

    public long getUserId() {
        return userId;
    }
}
