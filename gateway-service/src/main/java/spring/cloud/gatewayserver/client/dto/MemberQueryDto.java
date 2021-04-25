package spring.cloud.gatewayserver.client.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@EqualsAndHashCode
@ToString
@Getter
public class MemberQueryDto {

    private Long id;
    private String name;
    private String role;
    private LocalDateTime created;
    private LocalDateTime modified;

    protected MemberQueryDto() {}

    public MemberQueryDto(Long id, String name, String role, LocalDateTime created, LocalDateTime modified) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.created = created;
        this.modified = modified;
    }
}
