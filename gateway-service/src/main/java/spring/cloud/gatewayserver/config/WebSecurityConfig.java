package spring.cloud.gatewayserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private String MEMBER_SERVICE = "/api/member-service";

    private String ANONYMOUS = "ANONYMOUS";
    private String ADMIN = "ADMIN";
    private String USER = "USER";

    // 테스트용
    @Bean
    public UserDetailsService users() {
        UserDetails user = User.builder()
                .username("user")
                .password(createDelegatingPasswordEncoder().encode("1234"))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource()).and()

                .authorizeRequests()
                .antMatchers("/login/**", "/hello/**").permitAll()
                .antMatchers(OPTIONS,"/**").permitAll()
                .antMatchers(POST, MEMBER_SERVICE+"/member", MEMBER_SERVICE+"/authentication").permitAll()
                .antMatchers(GET,MEMBER_SERVICE+"/members").hasRole(ADMIN)
                .antMatchers(GET, MEMBER_SERVICE+"/members/{id}").hasRole(USER)
//                .anyRequest().denyAll()
                .and()

                .formLogin()
                .defaultSuccessUrl("http://localhost:8080/");

    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:8080");
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "OPTIONS", "PUT","DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
