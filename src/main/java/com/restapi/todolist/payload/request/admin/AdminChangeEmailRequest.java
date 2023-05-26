package com.restapi.todolist.payload.request.admin;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Getter
@Setter
@Data
@NoArgsConstructor
public class AdminChangeEmailRequest {
    @NotBlank
    @Email
    private String email;
}
