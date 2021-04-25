package spring.msa.member.entity.value;

import lombok.Getter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Embeddable
public class Password {

    @Column(name = "password")
    private String encodedPassword;

    protected Password() {}

    public Password(String rawPassword) {
        this.encodedPassword = encode(rawPassword);
    }

    public String encode(String rawPassword) {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return encoder.encode(rawPassword);
    }

    public boolean match(String rawPassword) {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return encoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public String toString() {
        return "Password{" +
                "encodedPassword='" + encodedPassword + '\'' +
                '}';
    }
}
