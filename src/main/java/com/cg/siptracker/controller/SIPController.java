package com.cg.siptracker.controller;

import com.cg.siptracker.dto.ResponseDTO;
import com.cg.siptracker.dto.SipDTO;
import com.cg.siptracker.service.SIPService;
import com.cg.siptracker.utility.JwtUtility;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/sips")
public class SIPController {

    @Autowired
    private SIPService sipService;

    @Autowired
    private JwtUtility jwtUtil;

    // Add a new SIP
    @PostMapping
    public ResponseEntity<ResponseDTO> addSIP(@Valid @RequestBody SipDTO sipDTO, @RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractEmail(token.substring(7));
        log.info("Request to add SIP for user: {}", email);
        ResponseDTO response = sipService.addSIP(sipDTO, email);
        log.info("SIP added successfully for user: {}", email);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Update an existing SIP
    @PutMapping("/{sipId}")
    public ResponseEntity<ResponseDTO> updateSIP(@PathVariable Long sipId, @Valid @RequestBody SipDTO sipDTO, @RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractEmail(token.substring(7));
        log.info("Request to update SIP with id: {} for user: {}", sipId, email);
        ResponseDTO response = sipService.updateSIP(sipId, sipDTO, email);
        log.info("SIP updated successfully with id: {} for user: {}", sipId, email);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Delete an existing SIP
    @DeleteMapping("/{sipId}")
    public ResponseEntity<ResponseDTO> deleteSIP(@PathVariable Long sipId, @RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractEmail(token.substring(7));
        log.info("Request to delete SIP with id: {} for user: {}", sipId, email);
        ResponseDTO response = sipService.deleteSIP(sipId, email);
        log.info("SIP deleted successfully with id: {} for user: {}", sipId, email);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Get SIP details by ID
    @GetMapping("/{sipId}")
    public ResponseEntity<ResponseDTO> getSIPById(@PathVariable Long sipId, @RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractEmail(token.substring(7));
        log.info("Request to fetch SIP details for sipId: {} by user: {}", sipId, email);
        ResponseDTO response = sipService.getSIPById(sipId, email);
        log.info("SIP details fetched successfully for sipId: {} by user: {}", sipId, email);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Get all SIPs for a user
    @GetMapping
    public ResponseEntity<ResponseDTO> getAllSIPsByUser(@RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractEmail(token.substring(7));
        log.info("Request to fetch all SIPs for user: {}", email);
        ResponseDTO response = sipService.getSIPsByUser(email);
        log.info("Fetched all SIPs successfully for user: {}", email);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Get summary (current value, XIRR) for a specific SIP
//    @GetMapping("/{sipId}/summary")
//    public ResponseEntity<ResponseDTO> getSIPSummary(@PathVariable Long sipId, @RequestHeader("Authorization") String token) {
//        String email = jwtUtil.extractUsername(token.substring(7));
//        log.info("Request to get summary for SIP id: {} by user: {}", sipId, email);
//        ResponseDTO response = sipService.getSipSummary(sipId, email);
//        log.info("SIP summary fetched successfully for SIP id: {} by user: {}", sipId, email);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
}
