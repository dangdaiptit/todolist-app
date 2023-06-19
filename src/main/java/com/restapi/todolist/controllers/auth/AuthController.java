package com.restapi.todolist.controllers.auth;


import com.restapi.todolist.exception.TokenRefreshException;
import com.restapi.todolist.models.RefreshToken;
import com.restapi.todolist.models.users.ERole;
import com.restapi.todolist.models.users.Role;
import com.restapi.todolist.models.users.User;
import com.restapi.todolist.payload.request.LoginRequest;
import com.restapi.todolist.payload.request.SignupRequest;
import com.restapi.todolist.payload.request.TokenRefreshRequest;
import com.restapi.todolist.payload.response.JwtResponse;
import com.restapi.todolist.payload.response.MessageResponse;
import com.restapi.todolist.payload.response.TokenRefreshResponse;
import com.restapi.todolist.repository.users.RoleRepository;
import com.restapi.todolist.repository.users.UserRepository;
import com.restapi.todolist.security.jwt.JwtUtils;
import com.restapi.todolist.security.services.RefreshTokenService;
import com.restapi.todolist.security.services.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    final AuthenticationManager authenticationManager;

    final JwtUtils jwtUtils;

    final UserRepository userRepository;

    final RoleRepository roleRepository;

    final PasswordEncoder encoder;

    final RefreshTokenService refreshTokenService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder, RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        User user = userRepository.findUserByUsernameOrEmail(loginRequest.getUsername(), loginRequest.getUsername()).orElseThrow(() -> new EntityNotFoundException("User not found with username or email: " + loginRequest.getUsername()));
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
    }

    @PostMapping(path = "/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        if (userRepository.existsUserByUsername(signupRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsUserByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        if (!signupRequest.getPassword().equals(signupRequest.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Password and confirm password do not match"));
        }

        //Create new user's account
        User user = new User(signupRequest.getUsername(), signupRequest.getEmail(), encoder.encode(signupRequest.getPassword()));

        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                if ("admin".equals(role)) {
                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(adminRole);
                } else {
                    Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<TokenRefreshResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                }).orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();
        refreshTokenService.delelteByUserId(userId);
        return ResponseEntity.ok(new MessageResponse("Logout successfuly!"));
    }
}
