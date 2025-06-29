package ru.practicum.intershop.mapper.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.practicum.intershop.domain.User;
import ru.practicum.intershop.dto.UserSignUpDTO;
import ru.practicum.intershop.mapper.UserMapper;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {

    private final PasswordEncoder encoder;

    @Override
    public User fromDTO(UserSignUpDTO userSignUpDTO) {
        return User.builder()
                .username(userSignUpDTO.username())
                .password(encoder.encode(userSignUpDTO.password()))
                .fullname(userSignUpDTO.fullName())
                .build();
    }

}
