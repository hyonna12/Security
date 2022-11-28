package shop.mtcoding.bank.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.bank.config.dummy.DummyEntity;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserRepository;
import shop.mtcoding.bank.dto.AccountReqDto.AccountSaveReqDto;
import shop.mtcoding.bank.dto.UserReqDto.JoinReqDto;

//@Transactional 도 ROLLBACK되지만 truncate 사용 - PK auto_increment 초기화 하기위해
// @Sql("classpath:db/truncate.sql") // 롤백 대신 사용 (auto_increment 초기화 + 데이터 비우기)
@ActiveProfiles("test")
@AutoConfigureMockMvc // MOCKMVC 객체를 줌
@SpringBootTest(webEnvironment = WebEnvironment.MOCK) // 가짜환경으로 테스트 - 데이터가 없는 상태
public class AccountApiControllerTest extends DummyEntity {

  private static final String APPLICATION_JSON_UTF8 = "application/json; charset=utf-8";
  private static final String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded; charset=utf-8";

  // DI
  @Autowired
  private MockMvc mvc; // HTTP 통신하는 객체

  @Autowired
  private ObjectMapper om;

  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  public void setUp() {
    User ssar = newUser("ssar");
    userRepository.save(ssar);
  }

  @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
  @Test
  public void save_test() throws Exception {
    // given
    AccountSaveReqDto accountSaveReqDto = new AccountSaveReqDto();
    accountSaveReqDto.setNumber(1111L);
    accountSaveReqDto.setPassword("1234");
    accountSaveReqDto.setOwnerName("쌀");
    String requestBody = om.writeValueAsString(accountSaveReqDto);
    System.out.println("테스트 : " + requestBody);

    // when
    ResultActions resultActions = mvc
        .perform(post("/api/account").content(requestBody)
            .contentType(APPLICATION_JSON_UTF8));
    String responseBody = resultActions.andReturn().getResponse().getContentAsString();
    System.out.println("테스트 : " + responseBody);

    // then
    resultActions.andExpect(status().isCreated());
    resultActions.andExpect(jsonPath("$.data.number").value(1111L));

  }
}