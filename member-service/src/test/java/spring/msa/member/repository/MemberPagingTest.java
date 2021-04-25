package spring.msa.member.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import spring.msa.member.entity.Member;
import spring.msa.member.entity.Role;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@DataJpaTest
public class MemberPagingTest {

    @Autowired
    private EntityManager em;

    public void makeMemberData(int startClosed, int endExclusive) {
        IntStream.range(startClosed, endExclusive)
                .forEach(i -> em.persist(new Member("name"+i, "password"+i, Role.USER)));
    }

    @Test
    public void getResultListTests() {
        makeMemberData(0, 10);

        List<Member> members = em.createQuery("select m from Member m", Member.class)
                .getResultList();

        assertThat(members.size()).isEqualTo(10);
    }

    @Test
    public void testPagingOnlyJPQL() {
        makeMemberData(0, 10);

        List<Member> members = em.createQuery("select m from Member m order by m.id asc", Member.class)
                .setFirstResult(3)
                .setMaxResults(4)
                .getResultList();

        assertThat(members.size()).isEqualTo(4);
        assertThat(members.stream().findFirst().get().getName()).isEqualTo("name3");
        assertThat(members.stream().skip(3).findFirst().get().getName()).isEqualTo("name6");
    }

}
