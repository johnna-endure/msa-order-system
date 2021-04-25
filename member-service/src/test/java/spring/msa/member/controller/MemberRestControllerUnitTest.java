package spring.msa.member.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import spring.msa.member.controller.request.member.MemberCreatRequest;
import spring.msa.member.controller.dto.member.MemberQueryDto;
import spring.msa.member.controller.response.ApiResponse;
import spring.msa.member.entity.Member;
import spring.msa.member.entity.Role;
import spring.msa.member.service.MemberService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static spring.msa.member.controller.response.ResponseStatus.*;

public class MemberRestControllerUnitTest {

    private MemberRestController restController;
    private MemberService memberService;

    @BeforeEach
    public void beforeAll() {
        memberService = mock(MemberService.class);
        restController = new MemberRestController(memberService);
    }

    @Test
    public void create_멤버_생성_성공하는_경우() {
        //given
        MemberCreatRequest requestDto = new MemberCreatRequest("name", "password");
        Member createdMember = new Member("name", "password", Role.USER);
        MemberQueryDto expectedMemberQuery = new MemberQueryDto(
                createdMember.getId(),
                createdMember.getName(),
                createdMember.getRole(),
                createdMember.getCreatedDate(),
                createdMember.getModifiedDate());

        //when
        when(memberService.create(any())).thenReturn(createdMember);
        ApiResponse apiResponse = restController.create(requestDto);

        //then
        assertThat(apiResponse.getStatus()).isEqualTo("OK");
        assertThat(apiResponse.getData()).isEqualTo(expectedMemberQuery);
    }

    @Test
    public void create_멤버_생성_실패하는_경우() {
        //given
        MemberCreatRequest requestDto = new MemberCreatRequest("name", "password");

        //when
        when(memberService.create(any())).thenThrow(RuntimeException.class);
        ApiResponse apiResponse = restController.create(requestDto);

        //then
        assertThat(apiResponse.getStatus()).isEqualTo(ERROR.name());
        assertThat(apiResponse.getData()).isNull();
    }

    @Test
    public void delete_삭제_성공_하는_경우() {
        //given
        Long memberId = 1L;

        //when
        doNothing().when(memberService).delete(memberId);
        ApiResponse apiResponse = restController.delete(memberId);

        //then
        assertThat(apiResponse.getStatus()).isEqualTo(OK.name());
        assertThat(apiResponse.getData()).isNull();
    }

    @Test
    public void delete_실패하는_경우() {
        //given
        Long memberId = 1L;

        //when
        doThrow(RuntimeException.class).when(memberService).delete(memberId);
        ApiResponse apiResponse = restController.delete(memberId);

        //then
        assertThat(apiResponse.getStatus()).isEqualTo(ERROR.name());
        assertThat(apiResponse.getData()).isNull();
    }

    @Test
    public void findOneById_조회_성공() {
        //given
        Long memberId = 1L;
        Member foundMember = new Member("name", "password", Role.USER);
        MemberQueryDto expectedMemberQuery = new MemberQueryDto(
                foundMember.getId(),
                foundMember.getName(),
                foundMember.getRole(),
                foundMember.getCreatedDate(),
                foundMember.getModifiedDate());

        //when
        when(memberService.findOneById(memberId)).thenReturn(foundMember);
        ApiResponse apiResponse = restController.findMemberById(memberId);

        //then
        assertThat(apiResponse.getStatus()).isEqualTo(OK.name());
        assertThat(apiResponse.getData()).isEqualTo(expectedMemberQuery);
    }

    @Disabled
    @Test
    public void findOneById_존재하지_않는_경우() {
        fail("미구현");
    }

    @Disabled
    @Test
    public void findOneById_예상못한에러() {
        //given
        Long memberId = 1L;

        //when
        when(memberService.findOneById(memberId)).thenThrow(RuntimeException.class);
        ApiResponse apiResponse = restController.findMemberById(memberId);

        //then
        assertThat(apiResponse.getStatus()).isEqualTo(ERROR.name());
        assertThat(apiResponse.getData()).isNull();
    }

    @Test
    public void findMemberByName_성공() {
        //given
        String name = "name";
        Member foundMember = new Member("name", "password", Role.USER);
        MemberQueryDto expectedMemberQuery = new MemberQueryDto(
                foundMember.getId(),
                foundMember.getName(),
                foundMember.getRole(),
                foundMember.getCreatedDate(),
                foundMember.getModifiedDate());

        //when
        when(memberService.findOneByName(name)).thenReturn(foundMember);
        ApiResponse response = restController.findMemberByName(name);
        //then
        assertThat(response.getStatus()).isEqualTo(OK.name());
        assertThat(response.getData()).usingRecursiveComparison().isEqualTo(expectedMemberQuery);
    }

    @Disabled
    @Test
    public void findOneByName_존재하지_않는_경우() {
        fail("미구현");
    }

    @Disabled
    @Test
    public void findMemberByName_예상못한에러() {
        //given
        String name = "name";

        //when
        when(memberService.findOneByName(name)).thenThrow(RuntimeException.class);
        ApiResponse apiResponse = restController.findMemberByName(name);

        //then
        assertThat(apiResponse.getStatus()).isEqualTo(ERROR.name());
        assertThat(apiResponse.getData()).isNull();
    }
}


