package com.example.oauthPractice.config;

import com.example.oauthPractice.jwt.JwtTokenFilter;
import com.example.oauthPractice.jwt.JwtTokenUtils;
import com.example.oauthPractice.oauth.OAuth2SuccessHandler;
import com.example.oauthPractice.oauth.OAuth2UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenUtils jwtTokenUtils;
    private final UserDetailsManager manager;
    private final OAuth2UserServiceImpl oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {
        http
                // csrf  보안 헤제
                .csrf(AbstractHttpConfigurer::disable)
                // url에 따른 요청 인가
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/users/home",
                                "/token/issue",
                                "/token/validate"
                        )
                        .permitAll()

                        .requestMatchers(
                                "/users/login"
                        )
                        .anonymous()
                )

                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/users/login")
                        .successHandler(oAuth2SuccessHandler)
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService))
                )

                // JWT를 사용하기 때문에 보안 관련 세션 헤제
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // JWT 필터를 권한 필터 앞에 삽입
                .addFilterBefore(
                        new JwtTokenFilter(
                                jwtTokenUtils,
                                manager
                        ),
                        AuthorizationFilter.class
                )
        ;
        return http.build();
    }

    // 사용자 정보 관리 클래스
//    public UserDetailsManager userDetailsManager(
//            PasswordEncoder passwordEncoder
//    ) {
//        // 사용자 1
//        UserDetails user1 = User.withUsername("user1")
//                .password(passwordEncoder.encode("password1"))
//                .build();
//        // Spring Security에서 기본으로 제공하는,
//        // 메모리 기반 사용자 관리 클래스 + 사용자 1
//        return new InMemoryUserDetailsManager(user1);
//    }
}