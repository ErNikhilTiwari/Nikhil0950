package com.cg.siptracker.controller;

import com.cg.siptracker.dto.*;

import com.cg.siptracker.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> registerUser(@Valid @RequestBody RegisterDTO registerDTO) {
        System.out.println("Register API hit");
        log.info("Registering User: {}", registerDTO.getEmail());
        ResponseDTO responseDTO = userService.registerUser(registerDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        System.out.println("Login API hit");
        log.info("Login User: {}", loginDTO.getEmail());
        ResponseDTO responseDTO = userService.loginUser(loginDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/forgot")
    public ResponseEntity<ResponseDTO> forgotPassword(@RequestBody RegisterDTO request){
        log.info("Forgot Password request for email: {}", request.getEmail());
        ResponseDTO responseDTO = userService.forgotPassword(request);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/reset")
    public ResponseEntity<ResponseDTO> resetPassword(@Valid @RequestBody ResetPasswordDTO request){
        log.info("Reset Password request for email: {}", request.getEmail());
        ResponseDTO responseDTO = userService.resetPassword(request);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/change-password")
    public ResponseEntity<ResponseDTO> changePassword(@Valid @RequestBody ChangePasswordDTO request, @RequestHeader("Authorization") String token){
        log.info("Changing Password...");
        String jwtToken = token.substring(7);
        ResponseDTO responseDTO = userService.changePassword(request,jwtToken);
        return ResponseEntity.ok(responseDTO);
    }
}
