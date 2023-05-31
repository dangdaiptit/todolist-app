package com.restapi.todolist.service.users;

import com.restapi.todolist.models.users.User;
import com.restapi.todolist.payload.request.ChangeEmailRequest;
import com.restapi.todolist.payload.request.ChangePasswordRequest;
import com.restapi.todolist.payload.request.EmailRequest;
import com.restapi.todolist.payload.request.ResetPasswordRequest;
import com.restapi.todolist.payload.request.admin.AdminChangeEmailRequest;
import com.restapi.todolist.payload.request.admin.ChangeRoleRequest;

import java.util.List;

public interface UserService {

    //user
    void changePassword(ChangePasswordRequest changePasswordRequest);

    void changeEmail(ChangeEmailRequest changeEmailRequest);

    void requestPasswordReset(EmailRequest emailRequest);

    void resetPassword(String email, ResetPasswordRequest resetPasswordRequest);

    void sendOtpEmail(String to, String subject, String text);

    boolean isOtpExpired(User user);

    boolean verifyOtp(String email, String otp);

    String generateOtp();

    User getUser();

    boolean checkPassword(String password);


    //admin
    User getUserById(Long id);

    User getUserByUserName(String username);

    List<User> getAllUser();

    void changePasswordById(Long id, User user);

    void changeEmailById(Long id, AdminChangeEmailRequest adminChangeEmailRequest);

    void changeRoleById(Long id, ChangeRoleRequest changeRoleRequest);

    void deleteUserById(Long id);


}
