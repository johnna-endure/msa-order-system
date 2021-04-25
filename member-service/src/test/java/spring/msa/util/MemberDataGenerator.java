package spring.msa.util;

import spring.msa.member.entity.Member;
import spring.msa.member.entity.Role;
import spring.msa.member.repository.MemberRepository;

import java.util.stream.IntStream;

import static spring.msa.member.entity.Role.*;

public class MemberDataGenerator {

    private MemberRepository memberRepository;

    public MemberDataGenerator(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void saveMemberData(int startClosed, int endExclusive) {
        IntStream.range(startClosed, endExclusive)
                .forEach(i -> memberRepository.save(new Member("name"+i, "password"+i, USER)));
    }

    /**
     * base : name, password 뒤에 숫자를 붙인 멤버를 저장, role USER
     */
    public Member saveMemberAppendedNumber(int i) {
        return memberRepository.save(new Member("name"+i, "password"+i, USER));
    }

}
