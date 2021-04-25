package spring.msa.member.controller.request.authentication;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class AuthenticationRequest {
    private String name;
    private String rawPassword;

    protected AuthenticationRequest() {}

    public AuthenticationRequest(String name, String rawPassword) {
        this.name = name;
        this.rawPassword = rawPassword;
    }
}
