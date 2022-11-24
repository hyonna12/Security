package shop.mtcoding.bank.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.bank.dto.UserReqDto.JoinReqDto;

//@Transactional 도 ROLLBACK되지만 truncate 사용 - PK auto_increment 초기화 하기위해
@ActiveProfiles("test")
@AutoConfigureMockMvc // MOCKMVC 객체를 줌
@SpringBootTest(webEnvironment = WebEnvironment.MOCK) // 가짜환경으로 테스트 - 데이터가 없는 상태
public class UserApiControllerTest {

  private static final String APPLICATION_JSON_UTF8 = "application/json; charset=utf-8";
  private static final String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded; charset=utf-8";

  // DI
  @Autowired
  private MockMvc mvc; // HTTP 통신하는 객체

  @Autowired
  private ObjectMapper om;

  @Test
  public void join_test() throws Exception {
    // given
    JoinReqDto joinReqDto = new JoinReqDto();
    joinReqDto.setUsername("ssar");
    joinReqDto.setPassword("1234");
    joinReqDto.setEmail("ssar@nate.com");
    String requestBody = om.writeValueAsString(joinReqDto);

    // when
    ResultActions resultActions = mvc
        .perform(post("/api/join").content(requestBody)
            .contentType(APPLICATION_JSON_UTF8));
    String responseBody = resultActions.andReturn().getResponse().getContentAsString();
    System.out.println("디버그 : " + responseBody);

    // then
    resultActions.andExpect(status().isCreated());
    resultActions.andExpect(jsonPath("$.data.username").value("ssar"));
  }
}
