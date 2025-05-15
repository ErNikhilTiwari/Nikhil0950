package com.cg.siptracker.service;

import com.cg.siptracker.dto.*;
import com.cg.siptracker.exception.ResourceNotFoundException;
import com.cg.siptracker.model.Role;
import com.cg.siptracker.model.User;
import com.cg.siptracker.repository.UserRepository;
import com.cg.siptracker.utility.JwtUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    EmailService emailService;

    @Autowired
    JwtUtility jwtUtility;

    @Autowired
    private OtpService otpService;

@Override
public ResponseDTO registerUser(RegisterDTO registerDTO) {
    log.info("Registering user with email: {}", registerDTO.getEmail());

    // Check if email already registered or not
    if (userRepository.findByEmail(registerDTO.getEmail()).isPresent()) {
        log.warn("Registration failed: Email already registered - {}", registerDTO.getEmail());
        throw new ResourceNotFoundException("Email already registered");
    }

    // Encode password
    String encodedPassword = bCryptPasswordEncoder.encode(registerDTO.getPassword());
    log.debug("Encoded password generated");

    User user = new User();
    user.setFullName(registerDTO.getFullName());
    user.setEmail(registerDTO.getEmail());
    user.setRole(registerDTO.getRole());
    user.setPassword(encodedPassword);

    userRepository.save(user);
    log.info("Registered successfully with email: {}", user.getEmail());

    emailService.sendEmail(user.getEmail(), "Registered in SIP Tracker", "Thank You! You are successfully registered in SIP Tracker!");
    LoginRegisterResponseDTO registerResponse = new LoginRegisterResponseDTO(user.getFullName(), user.getEmail(), user.getRole());
    log.info("Registration successful: {}", registerResponse.getEmail());
    return new ResponseDTO("Registered Successfully", registerResponse);
}

    @Override
    public ResponseDTO loginUser(LoginDTO loginDTO) {
        log.info("Attempting login for email: {}", loginDTO.getEmail());

        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login failed: Invalid email - {}", loginDTO.getEmail());
                    return new ResourceNotFoundException("Invalid Email or password");
                });

        if (!bCryptPasswordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            log.warn("Login failed: Incorrect password for email - {}", loginDTO.getEmail());
            throw new ResourceNotFoundException("Invalid Email or password");
        }
        String token = jwtUtility.generateToken(user.getEmail());
        log.debug("JWT token generated for user: {}", user.getEmail());


        emailService.sendEmail(user.getEmail(), "Login Successful", "Hi " + user.getFullName() + ",\n\n" + "You have successfully logged in to SIP Tracker. \n\n" + "Your JWT Token is:\n\n" + token + "\n\n");
        log.info("Login successful and token saved for user: {}", user.getEmail());

        LoginRegisterResponseDTO loginResponse = new LoginRegisterResponseDTO(user.getFullName(), user.getEmail(), token);
        return new ResponseDTO("Login Successful", loginResponse);
    }

    @Override
    public ResponseDTO forgotPassword(RegisterDTO request) {
        String email = request.getEmail();

        log.info("EMail: " + email);

        User user = userRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("User not found with this email: " + email));

        String otp = otpService.generateOtp(email);
        emailService.sendOtpEmail(email, otp);

        return new ResponseDTO("OTP sent to your email. It will expire in 5 minutes.", request.getEmail());
    }

    @Override
    public ResponseDTO resetPassword(ResetPasswordDTO request) {

        log.info("Reset Password for email: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));

        boolean isValidOtp = otpService.validateOtp(user.getEmail(),  request.getOtp());

        if(!isValidOtp) {
            log.warn("Invalid or expired OTP for email: {}", request.getEmail());
            throw new ResourceNotFoundException("Invalid or Expired OTP");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(request.getNewPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);

        log.info("Password reset successful for email: {}", user.getEmail());
        return new ResponseDTO("Password reset successfully", user.getEmail());
    }

    @Override
    public ResponseDTO changePassword(ChangePasswordDTO request, String token) {
        log.info("Changing Password...");
        String email = jwtUtility.extractEmail(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!bCryptPasswordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            log.warn("Old password is incorrect");
            throw new RuntimeException("Old password is incorrect");
        }

        user.setPassword(bCryptPasswordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        Map<String, Object> data = new HashMap<>();
        data.put("email", email);
        data.put("message", "Password updated successfully");
        data.put("timestamp", LocalDateTime.now());

        log.info("Password changed successfully for email: {}", user.getEmail());
        return new ResponseDTO("Password changed successfully", data);
    }

    // Admin
    @Override
    public ResponseDTO getAllUsers() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User admin = userRepository.findByEmailAndRole(email, Role.ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Access denied: Admin only"));

        List<User> users = userRepository.findByRole(Role.USER);
        List<LoginRegisterResponseDTO> userDTOs = users.stream()
                .map(u -> new LoginRegisterResponseDTO(u.getFullName(), u.getEmail(), u.getRole()))
                .toList();

        return new ResponseDTO("All users fetched successfully", userDTOs);
    }

}
