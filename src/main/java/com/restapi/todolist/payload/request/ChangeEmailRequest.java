package com.restapi.todolist.payload.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Data
public class ChangeEmailRequest {
    @NotBlank
    private String password;
    @NotBlank
    @Email
    private String email;
}
