package spring.cloud.gatewayserver.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import spring.cloud.gatewayserver.client.dto.MemberQueryDto;
import spring.cloud.gatewayserver.client.exception.NotFoundMemberException;
import spring.cloud.gatewayserver.client.response.response.ApiResponse;

import java.net.URI;

import static java.lang.String.format;

@RequiredArgsConstructor
@Component
public class MemberClient {

    private final RestTemplate restTemplate;

    public ApiResponse queryMemberByName(String name)  {
        try {
            RequestEntity requestEntity =
                    RequestEntity.get(new URI("http://member-service/member?name="+name)).build();
            ResponseEntity<ApiResponse> exchange = restTemplate.exchange(requestEntity, ApiResponse.class);
            return exchange.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw new NotFoundMemberException(format("username : {} 찾기 요청 실패.", name), e);
        }
    }
}
