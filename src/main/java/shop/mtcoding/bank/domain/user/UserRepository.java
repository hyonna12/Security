package shop.mtcoding.bank.domain.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

  @Query("select u from User u where username = :username") // jpql - 객체로 조회
  Optional<User> findByUsername(@Param("username") String username);

  @Query("select u from User u left join u.accounts ac on ac.isActive = true where u.id = :userId")
  User findByActiveUserIdv3(@Param("userId") Long userId);

}
