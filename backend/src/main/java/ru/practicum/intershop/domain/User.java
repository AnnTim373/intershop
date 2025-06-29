package ru.practicum.intershop.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(schema = "shop", name = "user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column("id")
    private Long id;

    @Column("username")
    private String username;

    @Column("password")
    private String password;

    @Column("full_name")
    private String fullname;

}
