package com.cg.siptracker.controller;

import com.cg.siptracker.dto.SipSummaryDto;
import com.cg.siptracker.exception.ResourceNotFoundException;
import com.cg.siptracker.model.SIP;
import com.cg.siptracker.repository.SIPRepository;
import com.cg.siptracker.service.IAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    @Autowired
    private SIPRepository sipRepository;

    @Autowired
    private IAnalyticsService IAnalyticsService;


    @GetMapping("/sip/{id}/summary")
    public ResponseEntity<SipSummaryDto> getSipSummary(@PathVariable Long id) {
        // Extract logged-in user's email
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // Fetch the SIP and verify ownership
        SIP sip = sipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SIP not found with id: " + id));

        if (!sip.getUser().getEmail().equals(email)) {
            throw new ResourceNotFoundException("You are not authorized to access this SIP");
        }

        SipSummaryDto summary = IAnalyticsService.analyzeSIP(sip);
        return ResponseEntity.ok(summary);
    }

}
