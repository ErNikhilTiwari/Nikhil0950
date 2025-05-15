package com.cg.siptracker.controller;

import com.cg.siptracker.dto.ResponseDTO;
import com.cg.siptracker.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    // Get all users
    @GetMapping("/users")
    public ResponseEntity<ResponseDTO> getAllUsers() {
        log.info("Admin fetching all users....");
        ResponseDTO responseDTO = userService.getAllUsers();
        return ResponseEntity.ok(responseDTO);
    }

}
