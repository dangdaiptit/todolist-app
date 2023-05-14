package com.restapi.todolist.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class PasswordValidResponse {

    @NotBlank
    private String oldPassword;
}
