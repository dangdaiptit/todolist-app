package com.restapi.todolist.controllers.user;

import com.restapi.todolist.payload.request.EmailRequest;
import com.restapi.todolist.payload.request.OtpVerificationRequest;
import com.restapi.todolist.payload.request.ResetPasswordRequest;
import com.restapi.todolist.payload.response.MessageResponse;
import com.restapi.todolist.payload.response.ResetPasswordResponse;
import com.restapi.todolist.service.users.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/reset-password")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ResetPasswordController {
    final UserService userService;
    private boolean isOtpVerified = false;

    public ResetPasswordController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/request")
    public ResponseEntity<MessageResponse> requestPasswordReset(@Valid @RequestBody EmailRequest emailRequest) {
        userService.requestPasswordReset(emailRequest);
        return ResponseEntity.ok().body(new MessageResponse("An email has been sent to your email address with instructions to reset your password."));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ResetPasswordResponse> verifyOtp(@Valid @RequestBody OtpVerificationRequest otpVerificationRequest) {
        String email = otpVerificationRequest.getEmail();
        String otp = otpVerificationRequest.getOtp();
        if (!userService.verifyOtp(email, otp)) {
            isOtpVerified = false;
            return ResponseEntity.badRequest().body(new ResetPasswordResponse(false, "Invalid OTP code."));
        }
        isOtpVerified = true;
        return ResponseEntity.ok(new ResetPasswordResponse(true, "OTP verified successfully."));
    }


    @PutMapping()
    public ResponseEntity<MessageResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest, @RequestParam String email) {
        if (!isOtpVerified) {
            return ResponseEntity.badRequest().body(new MessageResponse("OTP code not verified."));
        }
        userService.resetPassword(email, resetPasswordRequest);
        return ResponseEntity.ok(new MessageResponse("Reset password successfully."));
    }
}
