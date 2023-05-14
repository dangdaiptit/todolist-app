package com.restapi.todolist.controllers.user;


import com.restapi.todolist.models.users.User;
import com.restapi.todolist.payload.request.ChangeEmailRequest;
import com.restapi.todolist.payload.request.ChangePasswordRequest;
import com.restapi.todolist.payload.response.MessageResponse;
import com.restapi.todolist.repository.users.UserRepository;
import com.restapi.todolist.service.users.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    final UserService userService;
    final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        userService.changePassword(changePasswordRequest);
        return ResponseEntity.ok(new MessageResponse("Your password has been changed successfully"));
    }

    @PostMapping("/change-email")
    public ResponseEntity<?> changeEmail(@Valid @RequestBody ChangeEmailRequest changeEmailRequest) {
        userService.changeEmail(changeEmailRequest);
        return ResponseEntity.ok(new MessageResponse("Your email has been changed successfully"));
    }

    @GetMapping("/check/exist-username")
    public Boolean checkExistUserByUsername(@RequestParam String username) {
        return userRepository.existsUserByUsername(username);
    }

    @GetMapping("/check/exist-email")
    public Boolean checkExistUserByEmail(@RequestParam String email) {
        return userRepository.existsUserByEmail(email);
    }


    @GetMapping("/user-information")
    public ResponseEntity<?> getUser() {
        User user = userService.getUser();
        return ResponseEntity.ok(user);
    }

    @GetMapping("/validate-password")
    public Boolean checkPassword(@RequestParam String oldPassword) {
        return userService.checkPassword(oldPassword);
    }


}


