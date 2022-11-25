package shop.mtcoding.bank.domain.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.mtcoding.bank.config.enums.UserEnum;
import shop.mtcoding.bank.domain.AudingTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE) // new 못하게 걸어주기(hibernater만 new 하도록)
@Getter
@Table(name = "users")
@Entity
public class User extends AudingTime {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(unique = true, nullable = false, length = 20)
  private String username;
  @Column(nullable = false, length = 60)
  private String password;
  @Column(nullable = false, length = 50)
  private String email;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private UserEnum role; // ADMIN, CUSTOMER

  @Builder
  public User(Long id, String username, String password, String email, UserEnum role) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.email = email;
    this.role = role;
  }

}
