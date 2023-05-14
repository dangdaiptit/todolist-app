package com.restapi.todolist.service.users.impl;

import com.restapi.todolist.models.users.ERole;
import com.restapi.todolist.models.users.Role;
import com.restapi.todolist.models.users.User;
import com.restapi.todolist.payload.request.ChangeEmailRequest;
import com.restapi.todolist.payload.request.ChangePasswordRequest;
import com.restapi.todolist.payload.request.ResetPasswordRequest;
import com.restapi.todolist.payload.request.admin.ChangeRoleRequest;
import com.restapi.todolist.repository.users.RoleRepository;
import com.restapi.todolist.repository.users.UserRepository;
import com.restapi.todolist.service.users.UserService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {


    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;
    final RoleRepository roleRepository;
    private final JavaMailSender mailSender;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;

        this.mailSender = mailSender;
    }

    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));

        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Incorrect old password");
        }

        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirm password do not match");
        }

        String encodePassword = passwordEncoder.encode(changePasswordRequest.getNewPassword());
        user.setPassword(encodePassword);
        userRepository.save(user);

    }

    @Override
    public void changeEmail(ChangeEmailRequest changeEmailRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));

        if (!passwordEncoder.matches(changeEmailRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("User password: " + username + " is incorrect ");
        }

        if (userRepository.existsUserByEmail(changeEmailRequest.getEmail())) {
            throw new IllegalArgumentException("Email already exists!");
        }

        user.setEmail(changeEmailRequest.getEmail());
        userRepository.save(user);

    }

//    @Override
//    public void changeEmail(String email) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found with username " + username));
//        user.setEmail(email);
//        userRepository.save(user);
//    }

    @Override
    public void requestPasswordReset(String email) {
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
        String otp = generateOtp();
        user.setOtp(passwordEncoder.encode(otp));
        user.setOtpExpireTime(LocalDateTime.now().plusMinutes(5));
        userRepository.save(user);
        sendOtpEmail(user.getEmail(), "Password Reset Request", "Your OTP is " + otp + "\nNote: The OTP is only valid for 5 minutes!");
    }

    @Override
    public void resetPassword(String email, ResetPasswordRequest resetPasswordRequest) {
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found with email " + email));
        if (!resetPasswordRequest.getPassword().equals(resetPasswordRequest.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirm password do not match");
        }
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void sendOtpEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }


    @Override
    public boolean isOtpExpired(User user) {
        return LocalDateTime.now().isAfter(user.getOtpExpireTime());
    }

    @Override
    public boolean verifyOtp(String email, String otp) {
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found with emai: " + email));
        return passwordEncoder.matches(otp, user.getOtp()) && !isOtpExpired(user);
    }

    @Override
    public String generateOtp() {
        int otpLength = 6;
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < otpLength; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    @Override
    public User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findUserByUsername(username).orElseThrow(() -> {
            throw new EntityNotFoundException("User not found with username: " + username);
        });
    }

    @Override
    public boolean checkPassword(String password) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));

        return passwordEncoder.matches(password, user.getPassword());
    }


    //for admin...
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public void changePasswordById(Long id, User userUpdated) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
//        Check validate password
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,40}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(userUpdated.getPassword());
        boolean check = matcher.matches();
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid password");
        }
        String encodePassword = passwordEncoder.encode(userUpdated.getPassword().trim());
        user.setPassword(encodePassword);
        userRepository.save(user);
    }

    @Override
    public void changeEmailById(Long id, User userUpdated) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        if (userRepository.existsUserByEmail(userUpdated.getEmail())) {
            throw new IllegalArgumentException("Error: Email is already in use!");
        }
        user.setEmail(user.getEmail());
        userRepository.save(user);
    }

    @Override
    public void changeRoleById(Long id, ChangeRoleRequest changeRoleRequest) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        AtomicReference<Set<String>> strRoles = new AtomicReference<>(changeRoleRequest.getRole());
        Set<Role> roles = new HashSet<>();
        if (strRoles.get() == null || strRoles.get().isEmpty()) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.get().forEach(role -> {
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
    }


    @Override
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));
        userRepository.delete(user);

    }

    @Override
    public User getUserByUserName(String username) {
        return userRepository.findUserByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found with username " + username));
    }


}
