package shop.mtcoding.bank.config.exception;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

public class CustomApiExceptionTest {

  @Test
  public void customApi_test() throws Exception {
    // given
    String msg = "해당 id가 없습니다.";
    HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    Logger log = LoggerFactory.getLogger(getClass());

    // when
    CustomApiException ex = new CustomApiException(msg, httpStatus);
    System.out.println(ex.getHttpStatus());
    System.out.println(ex.getHttpStatus().value());
    System.out.println(ex.getMessage());
    log.debug("디버그 : " + ex.getMessage());
    // then
    // assertThat(ex.getHttpStatus().value()).isEqualTo(400);
    // assertThat(ex.getMessage()).isEqualTo("해당 id가 없습니다.");
  }
}