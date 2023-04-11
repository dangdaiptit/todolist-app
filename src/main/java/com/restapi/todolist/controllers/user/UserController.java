package com.restapi.todolist.controllers.user;


import com.restapi.todolist.payload.request.ChangePasswordRequest;
import com.restapi.todolist.payload.response.MessageResponse;
import com.restapi.todolist.service.users.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        userService.changePassword(changePasswordRequest);
        return ResponseEntity.ok(new MessageResponse("Your password has been changed successfully"));
    }



}


