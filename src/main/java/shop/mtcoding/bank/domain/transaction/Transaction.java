package shop.mtcoding.bank.domain.transaction;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.mtcoding.bank.config.enums.TransactionEnum;
import shop.mtcoding.bank.domain.AudingTime;
import shop.mtcoding.bank.domain.account.Account;

@NoArgsConstructor(access = AccessLevel.PROTECTED) // new 못하게 걸어주기(hibernater만 new 하도록)
@Getter
@Table(name = "transaction")
@Entity
public class Transaction extends AudingTime {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)) // fk 키 제약조건 없앰 - null 허용하기 위해
  @ManyToOne(fetch = FetchType.LAZY)
  private Account withdrawAccount; // 출금 계좌

  @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  @ManyToOne(fetch = FetchType.LAZY)
  private Account depositAccount; // 입금 계좌

  private Long amount; // 금액

  private Long withdrawAccountBalance; // 거래 후 잔액
  private Long depositAccountBalance; // 거래 후 잔액
  // account는 최종 잔액만 들고있음
  // 트랜잭션 시점의 잔액 필요

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private TransactionEnum gubun; // 입금(ATM으로부터), 출금(ATM으로부터), 이체(다른계좌로)

  @Builder
  public Transaction(Long id, Account withdrawAccount, Account depositAccount, Long amount, Long withdrawAccountBalance,
      Long depositAccountBalance, TransactionEnum gubun) {
    this.id = id;
    this.withdrawAccount = withdrawAccount;
    this.depositAccount = depositAccount;
    this.amount = amount;
    this.withdrawAccountBalance = withdrawAccountBalance;
    this.depositAccountBalance = depositAccountBalance;
    this.gubun = gubun;
  }

}
