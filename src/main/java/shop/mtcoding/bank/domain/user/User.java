package shop.mtcoding.bank.domain.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.PrintConversionEvent;

import lombok.Getter;
import shop.mtcoding.bank.config.enums.UserEnum;
import shop.mtcoding.bank.domain.AudingTime;

@Getter
@Table(name = "User")
@Entity
public class User extends AudingTime {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String username;
  private String password;
  private String email;
  private UserEnum role; // ADMIN, CUSTOMER
}
