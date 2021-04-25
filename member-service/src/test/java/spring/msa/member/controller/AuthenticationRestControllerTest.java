package spring.msa.member.controller;

import org.junit.jupiter.api.BeforeEach;
import spring.msa.member.service.MemberAuthenticationService;

import static org.mockito.Mockito.mock;

public class AuthenticationRestControllerTest {

    private MemberAuthenticationService authenticationService;
    private AuthenticationRestController restController;

    @BeforeEach
    public void beforeEach() {
        authenticationService = mock(MemberAuthenticationService.class);
        restController = new AuthenticationRestController(authenticationService);
    }


}
