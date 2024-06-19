package com.example.oauthPractice.service;

import com.example.oauthPractice.entity.CustomUserDetails;
import com.example.oauthPractice.entity.UserEntity;
import com.example.oauthPractice.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
//@RequiredArgsConstructor
@Service
public class JpaUserDetailsManager implements UserDetailsManager {
    private final UserRepository userRepository;

    public JpaUserDetailsManager(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;

        // 관리자
        createUser(CustomUserDetails.builder()
                .username("admin")
                .password(passwordEncoder.encode("password"))
                .email("admin@gmail.com")
                .authorities("ROLE_ADMIN")
                .build());
    }

    @Override
    // formLogin 등 Spring Security 내부에서 인증을 처리할때 사용하는 메서드
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Optional<UserEntity> optionalUser
                = userRepository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new UsernameNotFoundException(username);

        UserEntity userEntity = optionalUser.get();
        return CustomUserDetails.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .email(userEntity.getEmail())
                .authorities(userEntity.getAuthorities())
                .build();
    }

    @Override
    // 편의를 위해 같은 규약으로 회원가입을 하는 메서드
    public void createUser(UserDetails user) {
        if (userExists(user.getUsername())) // 이미 같은 이름의 사용자가 있다는 뜻 -> 오류
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        try {
            CustomUserDetails userDetails =
                    (CustomUserDetails) user;
            UserEntity newUser = UserEntity.builder()
                    .username(userDetails.getUsername())
                    .password(userDetails.getPassword())
                    .email(userDetails.getEmail())
                    .authorities(userDetails.getRawAuthorities())
                    .build();
            userRepository.save(newUser);
        } catch (ClassCastException e) {
            log.error("Failed Cast to: {}", CustomUserDetails.class);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }


    // 나중에...
    @Override
    public void updateUser(UserDetails user) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public void deleteUser(String username) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }
}
