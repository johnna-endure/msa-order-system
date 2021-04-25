package spring.cloud.gatewayserver.client;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import spring.cloud.gatewayserver.client.dto.MemberQueryDto;
import spring.cloud.gatewayserver.client.response.response.ApiResponse;
import spring.cloud.gatewayserver.client.response.response.ResponseStatus;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static spring.cloud.gatewayserver.client.response.response.ResponseStatus.*;

/*
name1~name4
password1~passwor4
자동화x
유레카, 멤버 서버 켜두고 테스트 할것.
 */
@SpringBootTest
public class MemberClientTest {

    @Autowired
    private MemberClient memberClient;

    @Tag("integration")
    @Test
    public void queryMemberByName_성공() {
        ApiResponse response = memberClient.queryMemberByName("name1");
        assertThat(response.getStatus()).isEqualTo(OK.name());

        Map<String, Object> data = (Map<String, Object>) response.getData();
        assertThat(data.get("name")).isEqualTo("name1");
        assertThat(data.get("role")).isEqualTo("USER");
    }

    @Tag("integration")
    @Test
    public void queryMemberByName_존재하지않는이름() {
        ApiResponse response = memberClient.queryMemberByName("noname");
        assertThat(response.getStatus()).isEqualTo(OK.name());
        assertThat(response.getData()).isNull();
    }
}
