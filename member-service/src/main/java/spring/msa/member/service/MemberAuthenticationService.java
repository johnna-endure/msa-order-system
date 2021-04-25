package spring.msa.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.msa.member.controller.request.authentication.AuthenticationRequest;
import spring.msa.member.repository.MemberRepository;

@RequiredArgsConstructor
@Service
public class MemberAuthenticationService {

    private final MemberRepository memberRepository;

    public boolean authenticate(AuthenticationRequest request) {
        return memberRepository.findByName(request.getName())
                .filter(m -> m.getPassword().match(request.getRawPassword()))
                .map(m -> true)
                .orElse(false);
    }
}
