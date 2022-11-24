package shop.mtcoding.bank.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.bank.config.auth.LoginUser;
import shop.mtcoding.bank.dto.ResponseDto;
import shop.mtcoding.bank.dto.UserReqDto.JoinReqDto;
import shop.mtcoding.bank.dto.UserRespDto.JoinRespDto;
import shop.mtcoding.bank.service.UserService;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class UserApiController {

  private final UserService userService;

  // @GetMapping("/user/session")
  // public String userSession(@AuthenticationPrincipal LoginUser loginUser) {
  // return "username : " + loginUser.getUsername();
  // } // 페이지 들어가려면 인증 필요한데 세션값을 넣어줌

  @PostMapping("/join")
  public ResponseEntity<?> join(@RequestBody JoinReqDto joinReqDto) {
    JoinRespDto joinResepDto = userService.회원가입(joinReqDto);
    return new ResponseEntity<>(new ResponseDto<>("회원가입 성공", joinResepDto), HttpStatus.CREATED);
  }
}
