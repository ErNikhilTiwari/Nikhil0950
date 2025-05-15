package com.cg.siptracker.controller;


import com.cg.siptracker.service.EmailService;
import com.cg.siptracker.service.IReportService;
import com.cg.siptracker.utility.JwtUtility;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/api")
public class ReportController {


    @Autowired
    private IReportService IReportService;

    @Autowired
    private JwtUtility jwtUtil;

    @Autowired
    private EmailService mailService;

    @GetMapping("/summary")
    public void downloadAndEmailCsv(@RequestHeader("Authorization") String token, HttpServletResponse response) throws IOException {
        String email = jwtUtil.extractEmail(token.substring(7));
        byte[] csvBytes = IReportService.generateCsvReport(email);

        // 1. Write CSV to HTTP response
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=sip-summary.csv");
        response.getOutputStream().write(csvBytes);
        response.getOutputStream().flush();

        // 2. Send email
        mailService.sendCsvWithAttachment(
                email,
                "SIP Summary Report",
                "Attached is your SIP summary in CSV format.",
                csvBytes,
                "sip-summary.csv"
        );


    }





}



