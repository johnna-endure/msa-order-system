package spring.msa.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.msa.member.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByName(String name);
    Optional<Member> findByName(String name);
}
