package spring.msa.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import spring.msa.member.controller.dto.member.MemberQueryDto;
import spring.msa.member.controller.request.member.MemberCreatRequest;
import spring.msa.member.controller.response.ApiResponse;
import spring.msa.member.entity.Member;
import spring.msa.member.service.MemberService;
import spring.msa.member.service.exception.FailedCreationException;
import spring.msa.member.service.exception.NotFoundMemberException;

import static spring.msa.member.controller.response.ResponseStatus.*;

@RequiredArgsConstructor
@RestController
public class MemberRestController {

    private final MemberService memberService;

    @PostMapping("/member")
    public ApiResponse create(@RequestBody MemberCreatRequest creatRequestDto) {
        return createMemberAndRetrieveApiResponse(creatRequestDto);
    }

    private ApiResponse createMemberAndRetrieveApiResponse(MemberCreatRequest creatRequestDto) {
        try {
            Member member = memberService.create(creatRequestDto);
            MemberQueryDto queryDto = new MemberQueryDto(member.getId(), member.getName(), member.getRole(), member.getCreatedDate(), member.getModifiedDate());
            return new ApiResponse(OK, queryDto);
        }catch (FailedCreationException e){
            return new ApiResponse(OK, e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse(ERROR, e.getMessage());
        }
    }

    @DeleteMapping("/members/{memberId}")
    public ApiResponse delete(@PathVariable("memberId") Long memberId) {
        return deleteMemberAndRetrieveApiResponse(memberId);
    }

    private ApiResponse deleteMemberAndRetrieveApiResponse(Long memberId) {
        try {
            memberService.delete(memberId);
            return new ApiResponse(OK);
        }catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse(ERROR, e.getMessage());
        }
    }
    @GetMapping("/member")
    public ApiResponse findMemberByName(@RequestParam("name") String name){
        System.out.println();
        try{
            Member member = memberService.findOneByName(name);
            MemberQueryDto queryDto = new MemberQueryDto(
                    member.getId(),
                    member.getName(),
                    member.getRole(),
                    member.getCreatedDate(),
                    member.getModifiedDate());
            ApiResponse response = new ApiResponse(OK, queryDto);
            return response;
        } catch (NotFoundMemberException e) {
            return new ApiResponse(OK, e.getMessage());
        } catch (Exception e) {
            return new ApiResponse(ERROR, e.getMessage());
        }
    }

    @GetMapping("/members/{memberId}")
    public ApiResponse findMemberById(@PathVariable("memberId") Long memberId) {
        return findMemberAndRetrieveApiResponse(memberId);
    }

    private ApiResponse findMemberAndRetrieveApiResponse(Long memberId) {
        try {
            Member member = memberService.findOneById(memberId);
            MemberQueryDto queryDto = new MemberQueryDto(member.getId(), member.getName(), member.getRole(), member.getCreatedDate(), member.getModifiedDate());
            return new ApiResponse(OK, queryDto);
        }catch (NotFoundMemberException e){
            return new ApiResponse(OK, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse(ERROR, e.getMessage());
        }
    }

    @GetMapping("/members")
    public ApiResponse findMembers(@RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "pageSize",  defaultValue = "10") int pageSize) {
        Page<MemberQueryDto> members = memberService.findMembers(page, pageSize)
                .map(m -> new MemberQueryDto(m.getId(), m.getName(), m.getRole(), m.getCreatedDate(), m.getModifiedDate()));
        return new ApiResponse(OK, members);
    }
}
