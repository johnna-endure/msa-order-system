package spring.cloud.gatewayserver.client.response.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
    private String status;
    private String message;
    private Object data;

    protected ApiResponse() {}

    public ApiResponse(ResponseStatus status) {
        this(status, null, null);
    }

    public ApiResponse(ResponseStatus status, String message) {
        this(status, message, null);
    }

    public ApiResponse(ResponseStatus status, Object data) {
        this(status, null, data);
    }

    public ApiResponse(ResponseStatus status,String message, Object data) {
        this.status = status.name();
        this.message = message;
        this.data = data;
    }
}
