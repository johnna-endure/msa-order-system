package spring.msa.member.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import spring.msa.member.entity.value.Password;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Member {

    @Id @GeneratedValue
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;

    @Embedded
    private Password password;

    private String role;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    protected Member() {}

    public Member(String name, String rawPassword, Role role) {
        this.name = name;
        this.password = new Password(rawPassword);
        this.role = role.name();
        this.createdDate = LocalDateTime.now();
        this.modifiedDate = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Member{" +
                "name='" + name + '\'' +
                ", password=" + password +
                ", role='" + role + '\'' +
                ", createdDate=" + createdDate +
                ", modifiedDate=" + modifiedDate +
                '}';
    }
}
