package spring.msa.member.controller.request.member;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class MemberCreatRequest {
    private String username;
    private String password;

    protected MemberCreatRequest() {}

    public MemberCreatRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
