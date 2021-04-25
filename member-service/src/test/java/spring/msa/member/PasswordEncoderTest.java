package spring.msa.member;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.*;

public class PasswordEncoderTest {
    @Test
    public void delegatingPasswordEncodingTest() {
        //given
        String givenPassword = "hello";

        //when
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(givenPassword);

        //then
        assertThat(passwordEncoder.matches(givenPassword, encodedPassword)).isTrue() ;
    }

    @Test
    public void defaultDelegatingPasswordEncoder(){
        String givenPassword = "hello";
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(givenPassword);
        assertThat(encodedPassword.startsWith("{bcrypt}")).isTrue();
    }

    @Test
    public void bcrytPasswordEncoding_서로_다른_버전_인코딩() {
        //given
        String givenPassword = "hello";

        //when
        BCryptPasswordEncoder bCryptPasswordEncoder$2A = new BCryptPasswordEncoder(BCryptVersion.$2A);
        String encodedPasswordVersion$2A = bCryptPasswordEncoder$2A.encode(givenPassword);

        BCryptPasswordEncoder bCryptPasswordEncoder$2B = new BCryptPasswordEncoder(BCryptVersion.$2B);
        String encodedPasswordVersion$2B = bCryptPasswordEncoder$2B.encode(givenPassword);

        //then
        assertThat(encodedPasswordVersion$2A).isNotEqualTo(encodedPasswordVersion$2B);
    }

    @Test
    public void delegatingPasswordEncoding_서로_다른_버전_호환테스트() {
        //given
        String givenPassword = "hello";

        String idForEncode = "bcrypt_2A";
        Map encoders = new HashMap<>();
        encoders.put(idForEncode, new BCryptPasswordEncoder(BCryptVersion.$2A));
        encoders.put("bcrypt_2B", new BCryptPasswordEncoder(BCryptVersion.$2B));
        DelegatingPasswordEncoder delegatingPasswordEncoder = new DelegatingPasswordEncoder(idForEncode,encoders);

        //when
        PasswordEncoder otherPasswordEncoder = new BCryptPasswordEncoder(BCryptVersion.$2B);
        String encodedPassword = "{bcrypt_2B}" + otherPasswordEncoder.encode(givenPassword);

        //then
        assertThat(delegatingPasswordEncoder.matches(givenPassword, encodedPassword)).isTrue();
    }

    @Test
    public void delegatingPasswordEncoding_서로_다른_인코더_호환() {
        //given
        String givenPassword = "hello";

        String idForEncode = "bcrypt";
        Map encoders = new HashMap<>();
        encoders.put(idForEncode, new BCryptPasswordEncoder());
        encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
        DelegatingPasswordEncoder delegatingPasswordEncoder = new DelegatingPasswordEncoder(idForEncode,encoders);

        //when
        PasswordEncoder otherPasswordEncoder = new Pbkdf2PasswordEncoder();
        String encodedPassword = "{pbkdf2}" + otherPasswordEncoder.encode(givenPassword);

        //then
        assertThat(delegatingPasswordEncoder.matches(givenPassword, encodedPassword)).isTrue();
    }
}
