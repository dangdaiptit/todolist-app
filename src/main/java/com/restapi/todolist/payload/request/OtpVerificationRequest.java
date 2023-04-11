package com.restapi.todolist.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class OtpVerificationRequest {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String otp;
}
