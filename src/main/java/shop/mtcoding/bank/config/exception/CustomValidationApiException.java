package shop.mtcoding.bank.config.exception;

import java.util.Map;

public class CustomValidationApiException extends RuntimeException {

  private final int httpStatusCode = 400; // bad request - vlidation 체크 실패
  private Map<String, String> errorMap;

  // msg = "유효성 검사 실패"
  public CustomValidationApiException(Map<String, String> errorMap) {
    super("유효성 검사 실패");
    this.errorMap = errorMap;
  }

}
