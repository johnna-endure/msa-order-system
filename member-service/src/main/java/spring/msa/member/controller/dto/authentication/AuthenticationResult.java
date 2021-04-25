package spring.msa.member.controller.dto.authentication;

import lombok.Getter;

@Getter
public class AuthenticationResult {
    private String result;

    protected AuthenticationResult() {}

    public AuthenticationResult(AuthenticationResultState resultState) {
        this.result = resultState.name();
    }
}
