package ru.practicum.intershop.mapper;

import ru.practicum.intershop.domain.User;
import ru.practicum.intershop.dto.UserSignUpDTO;

public interface UserMapper {

    User fromDTO(UserSignUpDTO userSignUpDTO);

}
