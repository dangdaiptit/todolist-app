package com.restapi.todolist.service.users;

import com.restapi.todolist.models.users.User;
import com.restapi.todolist.payload.request.ChangePasswordRequest;
import com.restapi.todolist.payload.request.ResetPasswordRequest;
import com.restapi.todolist.payload.request.admin.ChangeRoleRequest;

import java.io.IOException;
import java.util.List;

public interface UserService {

    //user
    void changePassword(ChangePasswordRequest changePasswordRequest);

    void changeEmail(String email);

    void requestPasswordReset(String email) throws IOException;

    void resetPassword(String email, ResetPasswordRequest resetPasswordRequest);

    void sendOtpEmail(String to, String subject, String text) throws IOException;

    boolean isOtpExpired(User user);

    boolean verifyOtp(String email, String otp);

    String generateOtp();

    //admin
    User getUserById(Long id);

    User getUserByUserName(String username);

    List<User> getAllUser();

    void changePasswordById(Long id, User user);

    void changeEmailById(Long id, User user);

    void changeRoleById(Long id, ChangeRoleRequest changeRoleRequest);

    void deleteUserById(Long id);


}
