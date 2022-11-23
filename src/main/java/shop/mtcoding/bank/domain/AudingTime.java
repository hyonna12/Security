package shop.mtcoding.bank.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

@Getter
@MappedSuperclass // 엔티티 공통 필드가 존재할 때 중복 코드를 제거하기 위해 사용
// 자식이 이 친구를 상속할건데, 자식이 이 친구를 테이블에 컬럼으로 만들어라
@EntityListeners(AuditingEntityListener.class) // 리스너-insert, update 할 때 지켜보다가 시간을 넣어줌
public abstract class AudingTime {

  @LastModifiedDate // insert, update 시에 현재시간 들어감
  @Column(nullable = false)
  protected LocalDateTime updatedAt;

  @CreatedDate // insert 시에 현재시간 들어감
  @Column(nullable = false)
  protected LocalDateTime createdAt;
}
