package spring.msa.member.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import spring.msa.member.controller.dto.member.MemberQueryDto;
import spring.msa.member.controller.request.member.MemberCreatRequest;
import spring.msa.member.controller.response.ApiResponse;
import spring.msa.member.entity.Member;
import spring.msa.member.repository.MemberRepository;
import spring.msa.util.JsonConverter;
import spring.msa.util.MemberDataGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.jayway.jsonpath.JsonPath.parse;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static spring.msa.member.controller.response.ResponseStatus.ERROR;
import static spring.msa.member.controller.response.ResponseStatus.OK;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class MemberRestControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;


    private MemberDataGenerator memberDataGenerator;
    private JsonConverter jsonConverter = new JsonConverter();

    @BeforeEach
    public void beforeEach() {
        memberDataGenerator = new MemberDataGenerator(memberRepository);
    }


    @Test
    public void create_성공() throws Exception {
        //given
        MemberCreatRequest request = new MemberCreatRequest("name0", "password0");
        String requestContent = jsonConverter.toJson(request);

        //when
        String responseContent = mockMvc.perform(
                post("/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        ApiResponse apiResponse = jsonConverter.fromJson(responseContent, ApiResponse.class);
        //then
        assertThat(apiResponse.getStatus()).isEqualTo(OK.name());

        Member expectedMember = memberRepository.findByName("name0").get();
        Map<String, String> responseData = (Map<String, String>) apiResponse.getData();
        assertThat(responseData.get("name")).isEqualTo(expectedMember.getName());
        assertThat(responseData.get("role")).isEqualTo(expectedMember.getRole());
        assertThat(LocalDateTime.parse(responseData.get("created"))).isEqualTo(expectedMember.getCreatedDate());
        assertThat(LocalDateTime.parse(responseData.get("modified"))).isEqualTo(expectedMember.getModifiedDate());
    }


    @Test
    public void create_유저네임_중복으로_실패() throws Exception {
        //given
        memberDataGenerator.saveMemberAppendedNumber(0);
        MemberCreatRequest request = new MemberCreatRequest("name0", "password1234");
        String requestContent = jsonConverter.toJson(request);

        //when
        String responseContent = mockMvc.perform(
                post("/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        ApiResponse apiResponse = jsonConverter.fromJson(responseContent, ApiResponse.class);

        //then
        assertThat(apiResponse.getStatus()).isEqualTo(OK.name());
        assertThat(apiResponse.getMessage()).isEqualTo("[name=name0] is duplicated.");
    }

    @Test
    public void delete_성공() throws Exception {
        //given
        memberDataGenerator.saveMemberAppendedNumber(0);
        Long memberId = memberRepository.findByName("name0").get().getId();

        //when
        String responseContent = mockMvc.perform(delete("/members/"+memberId))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        ApiResponse response = jsonConverter.fromJson(responseContent, ApiResponse.class);

        //then
        assertThat(response.getStatus()).isEqualTo(OK.name());
    }

    @Test
    public void delete_존재하지_멤버_삭제로_실패() throws Exception {
        //when
        String responseContent = mockMvc.perform(delete("/members/12"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //then
        ApiResponse response = jsonConverter.fromJson(responseContent, ApiResponse.class);
        assertThat(response.getStatus()).isEqualTo(ERROR.name());
        assertThat(response.getMessage()).isEqualTo("No class spring.msa.member.entity.Member entity with id 12 exists!");
    }

    @Test
    public void findMember_조회_성공() throws Exception {
        //given
        memberDataGenerator.saveMemberAppendedNumber(0);
        MemberQueryDto expected = memberRepository.findByName("name0")
                .map(m -> new MemberQueryDto(m.getId(), m.getName(), m.getRole(), m.getCreatedDate(), m.getModifiedDate()))
                .get();
        //when
        String responseContent = mockMvc.perform(
                get("/members/" + expected.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        //then
        ApiResponse apiResponse = jsonConverter.fromJson(responseContent, ApiResponse.class);
        assertThat(apiResponse.getStatus()).isEqualTo(OK.name());

        Map<String, Object> responseData = (Map<String, Object>) apiResponse.getData();
        assertThat(responseData.get("id").toString()).isEqualTo(expected.getId().toString());
        assertThat(responseData.get("name").toString()).isEqualTo(expected.getName());
        assertThat(responseData.get("role").toString()).isEqualTo(expected.getRole());
    }

    @Test
    public void findMember_조회_실패() throws Exception {
        //given
        Long memberId = 10L;
        //when
        MockHttpServletResponse mockHttpServletResponse = mockMvc.perform(
                get("/members/" + memberId))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        mockHttpServletResponse.setCharacterEncoding("utf-8");
        String responseContent = mockHttpServletResponse.getContentAsString();
        //then
        ApiResponse apiResponse = jsonConverter.fromJson(responseContent, ApiResponse.class);
        assertThat(apiResponse.getStatus()).isEqualTo(OK.name());
        assertThat(apiResponse.getMessage()).isEqualTo(format("id : {} 인 회원이 존재하지 않습니다.", memberId));

    }

    @Test
    public void findMembers_페이징_조회_성공() throws Exception {
        //given
        memberDataGenerator.saveMemberData(0,5);
        //when
        String firstPageContent = mockMvc.perform(
                get("/members")
                        .param("page","0")
                        .param("pageSize", "3"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String secondPageContent = mockMvc.perform(
                get("/members")
                        .param("page","1")
                        .param("pageSize", "3"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //then
        List<String> firstPageResult = parse(firstPageContent).read("$.data.content",List.class);
        assertThat(firstPageResult.size()).isEqualTo(3);
        List<String> secondPageResult = parse(secondPageContent).read("$.data.content",List.class);
        assertThat(secondPageResult.size()).isEqualTo(2);
    }

    @Test
    public void findMembers_페이지_조회_실패() throws Exception {
        //when
        String responseContent = mockMvc.perform(
                get("/members")
                        .param("page", "3"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //then
        List<String> result = parse(responseContent).read("$.data.content", List.class);
        assertThat(result.size()).isEqualTo(0);
    }

    @Disabled
    @Test
    public void findMemberByName_성공() {
        fail("미구현");
    }

    @Disabled
    @Test
    public void findMemberByName_실패() {
        fail("미구현");
    }
}
