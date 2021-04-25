package spring.msa.util;

import org.junit.jupiter.api.Test;
import spring.msa.member.controller.response.ApiResponse;
import spring.msa.member.controller.response.ResponseStatus;

public class TestJsonConverter {
    @Test
    public void nullFieldTest() {
        ApiResponse response = new ApiResponse(ResponseStatus.OK, null, null);
        JsonConverter jsonConverter = new JsonConverter();
        String json = jsonConverter.toJson(response);
        System.out.println(json);
    }
}
