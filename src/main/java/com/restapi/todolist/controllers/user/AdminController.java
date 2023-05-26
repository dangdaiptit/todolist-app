package com.restapi.todolist.controllers.user;


import com.restapi.todolist.models.users.User;
import com.restapi.todolist.payload.request.admin.AdminChangeEmailRequest;
import com.restapi.todolist.payload.request.admin.ChangeRoleRequest;
import com.restapi.todolist.payload.response.MessageResponse;
import com.restapi.todolist.service.users.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AdminController {
    final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        var user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUser() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @PutMapping("/users/{userId}/change-password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> changePassword(@PathVariable Long userId, @RequestBody User user) {
        userService.changePasswordById(userId, user);
        return ResponseEntity.ok(new MessageResponse("The password of the user with id " + userId + " has been changed successfully"));
    }

    @PutMapping("/users/{userId}/change-email")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> changeEmail(@PathVariable Long userId, @Valid @RequestBody AdminChangeEmailRequest adminChangeEmailRequest) {
        userService.changeEmailById(userId, adminChangeEmailRequest);
        return ResponseEntity.ok(new MessageResponse("The email of the user with id " + userId + " has been changed success"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.ok(new MessageResponse("Delete user successfully"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("users/{id}/change-roles")
    public ResponseEntity<?> editRoleById(@PathVariable Long id, @RequestBody ChangeRoleRequest changeRoleRequest) {
        userService.changeRoleById(id, changeRoleRequest);
        return ResponseEntity.ok(new MessageResponse("Change roles successfully"));
    }

}
