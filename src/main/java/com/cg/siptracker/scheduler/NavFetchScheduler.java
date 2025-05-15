package com.cg.siptracker.scheduler;

import com.cg.siptracker.dto.ResponseDTO;
import com.cg.siptracker.model.SIP;
import com.cg.siptracker.repository.SIPRepository;
import com.cg.siptracker.service.IAnalyticsService;
import com.cg.siptracker.service.INAVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NavFetchScheduler {

    @Autowired
    private INAVService navService;

    @Autowired
    private IAnalyticsService IAnalyticsService;

    @Autowired
    private SIPRepository sipRepository;


    @Scheduled(cron = "0 20 10 * * *") // Runs every day at 9:30 AM
    public void scheduleNAVFetch() {
        try {
            ResponseDTO response = navService.fetchAndStoreNAVs();
            System.out.println("NAV Scheduler " + response.getMessage() + ": " + response.getData());

            // 🚨 Trigger NAV drop alert for each SIP
            List<SIP> sips = sipRepository.findAll();
            for (SIP sip : sips) {
                String userEmail = sip.getUser().getEmail(); // Assumes SIP has User and User has Email
                IAnalyticsService.checkAndSendDropAlert(sip, userEmail);
            }

        } catch (Exception e) {
            System.err.println("[NAV Scheduler Error] " + e.getMessage());
        }
    }
}
