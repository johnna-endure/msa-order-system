package spring.msa.member.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import spring.msa.member.entity.Member;
import spring.msa.util.MemberDataGenerator;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    private MemberDataGenerator memberDataGenerator;

    @BeforeEach
    public void beforeEach() {
        memberDataGenerator = new MemberDataGenerator(memberRepository);
    }

    @Test
    public void finaAll_pagination() {
        //given
        memberDataGenerator.saveMemberData(0,10);

        //when
        PageRequest pageRequest = PageRequest.of(0,5);
        Page<Member> members = memberRepository.findAll(pageRequest);

        //then
        assertThat(members.getTotalPages()).isEqualTo(2);
        assertThat(members.getTotalElements()).isEqualTo(10);
        assertThat(members.getNumber()).isEqualTo(0);
        assertThat(members.hasNext()).isTrue();
        assertThat(members.hasPrevious()).isFalse();
        assertThat(members.isFirst()).isTrue();
        assertThat(members.isLast()).isFalse();
    }

}
