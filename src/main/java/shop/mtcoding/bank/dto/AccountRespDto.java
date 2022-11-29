package shop.mtcoding.bank.dto;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.bank.domain.account.Account;
import shop.mtcoding.bank.domain.user.User;

public class AccountRespDto {

    @Setter
    @Getter
    public static class AccountListRespDto {
        private UserDto user;
        private List<AccountDto> accounts;

        public AccountListRespDto(List<Account> accounts) {
            this.user = new UserDto(accounts.get(0).getUser()); // user객체를 userDto로 변환
            this.accounts = accounts.stream().map((account) -> new AccountDto(account)).collect(Collectors.toList());
            // stream api
            // account 객체를 accountDto로 변환/ account collection 에 있는 한개의 account를 dto에 담음
            // arraylist 타입의 accounts를 흩뿌려서 stream에 담음(타입 stream)
            // map 에 2건의 account가 있다면
            // map을 2번 돌면서 account를 accountDto로 리턴하고 stream 을 list 타입으로바꿔서 accounts에 담음
            // accounts는 list의 accountDto 타입
        }

        @Setter
        @Getter
        public class UserDto {
            private Long id; // user 것
            private String fullName; // account 필드

            public UserDto(User user) {
                this.id = user.getId();
                this.fullName = user.getFullName();
            }
        }

        @Setter
        @Getter
        public class AccountDto {
            private Long id;
            private Long number;
            private Long balance;

            public AccountDto(Account account) {
                this.id = account.getId();
                this.number = account.getNumber();
                this.balance = account.getBalance();
            }
        }
    }

    @Setter
    @Getter
    public static class AccountSaveRespDto {
        private Long id;
        private Long number;
        private Long balance;

        public AccountSaveRespDto(Account account) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
        }
    }

}
