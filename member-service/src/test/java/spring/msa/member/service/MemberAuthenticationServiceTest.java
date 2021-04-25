package spring.msa.member.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import spring.msa.member.controller.request.authentication.AuthenticationRequest;
import spring.msa.member.repository.MemberRepository;
import spring.msa.member.controller.dto.authentication.AuthenticationResult;
import spring.msa.util.MemberDataGenerator;

import static org.assertj.core.api.Assertions.assertThat;
import static spring.msa.member.controller.dto.authentication.AuthenticationResultState.APPROVED;
import static spring.msa.member.controller.dto.authentication.AuthenticationResultState.DENIED;

@Transactional
@DataJpaTest
public class MemberAuthenticationServiceTest {

    @Autowired
    private MemberRepository memberRepository;
    private MemberDataGenerator memberDataGenerator = new MemberDataGenerator(memberRepository);
    private MemberAuthenticationService authenticationService;

    @BeforeEach
    public void beforeEach() {
        authenticationService = new MemberAuthenticationService(memberRepository);
        memberDataGenerator = new MemberDataGenerator(memberRepository);
    }

    @Test
    public void authenticate_인증_성공() {
        //given
        memberDataGenerator.saveMemberAppendedNumber(0);
        AuthenticationRequest request = new AuthenticationRequest("name0", "password0");
        //when
        boolean isApproved = authenticationService.authenticate(request);

        //then
        assertThat(isApproved).isTrue();
    }

    @Test
    public void authenticate_인증_실패() {
        //given
        memberDataGenerator.saveMemberAppendedNumber(0);
        AuthenticationRequest request = new AuthenticationRequest("name0", "password123");
        //when
        boolean isApproved = authenticationService.authenticate(request);

        //then
        assertThat(isApproved).isFalse();
    }
}
