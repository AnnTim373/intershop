package ru.practicum.intershop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserOutputDTO {

    private Long id;

    private String username;

    private Role role;

    public enum Role {USER, ANONYMOUS}

}
