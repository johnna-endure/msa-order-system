package spring.msa.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.msa.member.controller.request.authentication.AuthenticationRequest;
import spring.msa.member.controller.dto.authentication.AuthenticationResult;
import spring.msa.member.service.MemberAuthenticationService;

import static spring.msa.member.controller.dto.authentication.AuthenticationResultState.APPROVED;
import static spring.msa.member.controller.dto.authentication.AuthenticationResultState.DENIED;

@RequiredArgsConstructor
@RestController
public class AuthenticationRestController {

    private final MemberAuthenticationService memberAuthenticationService;

    @PostMapping("/authentication")
    public AuthenticationResult authentication(AuthenticationRequest request) {
        boolean isApproved = memberAuthenticationService.authenticate(request);

        if(isApproved) return new AuthenticationResult(APPROVED);
        else return new AuthenticationResult(DENIED);
    }
}
