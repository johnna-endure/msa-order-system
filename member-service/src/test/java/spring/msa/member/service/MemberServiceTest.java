package spring.msa.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import spring.msa.member.controller.request.member.MemberCreatRequest;
import spring.msa.member.entity.Member;
import spring.msa.member.repository.MemberRepository;
import spring.msa.member.service.exception.FailedCreationException;
import spring.msa.member.service.exception.NotFoundMemberException;
import spring.msa.util.MemberDataGenerator;

import java.util.List;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@DataJpaTest
public class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;
    private MemberService memberService;
    private MemberDataGenerator memberDataGenerator;

    @BeforeEach
    public void beforeEach() {
        memberService = new MemberService(memberRepository);
        memberDataGenerator = new MemberDataGenerator(memberRepository);
    }

    @Test
    public void memberRepositoryIsNotNull() {
        assertThat(memberRepository).isNotNull();
    }

    @Test
    public void create_성공() {
        //given
        memberDataGenerator.saveMemberData(0,3);
        MemberCreatRequest createDto = new MemberCreatRequest("name4", "password4");

        //when
        Member createdMember = memberService.create(createDto);
        //then
        Member storedMember = memberRepository.findById(createdMember.getId()).get();
        assertThat(createdMember.getId()).isEqualTo(storedMember.getId());
        assertThat(createdMember).usingRecursiveComparison().isEqualTo(storedMember);
    }

    @Test
    public void create_중복_등록() {
        //given
        memberDataGenerator.saveMemberData(0,3);
        MemberCreatRequest createDto = new MemberCreatRequest("name2", "password4");

        //when, then
        assertThatThrownBy(() -> memberService.create(createDto))
                .hasMessage("[name=name2] is duplicated.")
                .isExactlyInstanceOf(FailedCreationException.class);
    }

    @Test
    public void delete_성공() {
        //given
        memberDataGenerator.saveMemberData(0,3);
        Long memberId = memberRepository.findAll().stream().findFirst().get().getId();

        //when
        memberService.delete(memberId);

        //then
        List<Member> members = memberRepository.findAll();
        assertThat(members.size()).isEqualTo(2);
    }

    @Test
    public void findOneById_조회_성공() {
        //given
        memberDataGenerator.saveMemberData(0,3);
        Long memberId = memberRepository.findAll().stream().findFirst().get().getId();

        //when
        Member foundMember = memberService.findOneById(memberId);
        //then
        Member storedMember = memberRepository.findById(foundMember.getId()).get();
        assertThat(foundMember).usingRecursiveComparison().isEqualTo(storedMember);
    }

    @Test
    public void findOneById_조회_실패() {
        //given
        memberDataGenerator.saveMemberData(0,3);
        Long memberId = 10000L;

        //when, then
        assertThatThrownBy(() -> memberService.findOneById(memberId))
                .isExactlyInstanceOf(NotFoundMemberException.class)
                .hasMessage(format("id : {} 인 회원이 존재하지 않습니다.", memberId));
    }

    @Test
    public void findMembers_조회_성공() throws JsonProcessingException {
        //given
        memberDataGenerator.saveMemberData(0, 5);
        int pageSize = 3;

        //when
        Page<Member> firstPage = memberService.findMembers(0, pageSize);
        Page<Member> lastPage = memberService.findMembers(1, pageSize);

        //then
        assertThat(firstPage.getTotalElements()).isEqualTo(5);
        assertThat(firstPage.getNumberOfElements()).isEqualTo(3);
        assertThat(firstPage.isFirst()).isTrue();

        assertThat(lastPage.getNumberOfElements()).isEqualTo(2);
        assertThat(lastPage.isLast()).isTrue();
    }

    @Test
    public void findMembers_조회_실패() {
        //given
        int page = 2;
        int pageSize = 3;

        //when
        Page<Member> firstPage = memberService.findMembers(page, pageSize);

        //then
        assertThat(firstPage.getTotalElements()).isEqualTo(0);
        assertThat(firstPage.getNumberOfElements()).isEqualTo(0);
    }

    @Test
    public void findMemberById_성공() {
        //given
        Member expected = memberDataGenerator.saveMemberAppendedNumber(0);
        //when
        Member actual = memberService.findOneByName("name0");
        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void findMemberById_실패() {
        //when
        assertThatThrownBy(() -> memberService.findOneByName("name0"))
                .isExactlyInstanceOf(NotFoundMemberException.class);
    }

    @Test
    public void findMember_name으로_조회성공() {

    }
}