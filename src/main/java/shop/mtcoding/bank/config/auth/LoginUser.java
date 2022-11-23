package shop.mtcoding.bank.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import shop.mtcoding.bank.domain.user.User;

@Getter
@RequiredArgsConstructor
public class LoginUser implements UserDetails {
  // LoginUser를 UserDetails 형식으로 담아서 세션에 저장

  private final User user;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // role 담아주기 - role 이 2개 이상일 수 있어서 collection 타입에 담아줌
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(() -> "ROLE_" + user.getRole()); // admin or customer
    return authorities;
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

}
