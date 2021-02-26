package com.vlados.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    @NotEmpty(message = "{username.not_empty}")
    @Size(min = 2, max = 15, message = "{username.size}")
    private String username;

    @NotEmpty(message = "{password.not_empty}")
    private String password;


    @NotEmpty(message = "{fullname.not_empty}")
    @Size(min = 4, max = 50, message = "{fullname.size}")
    private String fullName;

    @NotEmpty(message = "{email.not_empty}")
    @Email(message = "{email.valid}")
    private String email;

    @NotEmpty(message = "{phone.not_empty}")
    private String phoneNumber;
    private String role;
    private boolean active;
    private boolean locked;
}
