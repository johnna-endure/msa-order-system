package spring.msa.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.msa.member.controller.request.member.MemberCreatRequest;
import spring.msa.member.entity.Member;
import spring.msa.member.entity.Role;
import spring.msa.member.repository.MemberRepository;
import spring.msa.member.service.exception.FailedCreationException;
import spring.msa.member.service.exception.NotFoundMemberException;

import static java.lang.String.format;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public Member create(MemberCreatRequest createDto) {
        Member member = new Member(createDto.getUsername(), createDto.getPassword(), Role.USER);

        boolean exists = memberRepository.existsByName(member.getName());
        if(exists) throw new FailedCreationException(format("[name=%s] is duplicated.", member.getName()));

        return memberRepository.save(member);
    }

    public void delete(Long memberId) {
        memberRepository.deleteById(memberId);
    }

    public Member findOneById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberException(format("id : {} 인 회원이 존재하지 않습니다.", memberId)));
    }

    public Member findOneByName(String name) {
        return memberRepository.findByName(name)
                .orElseThrow(() -> new NotFoundMemberException(format("username : {} 인 회원이 존재하지 않습니다.", name)));
    }

    public Page<Member> findMembers(int page, int pageSize) {
        return memberRepository.findAll(PageRequest.of(page, pageSize));
    }

}
