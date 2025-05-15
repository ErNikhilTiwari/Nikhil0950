package com.cg.siptracker.service;

import com.cg.siptracker.dto.SipSummaryDto;
import com.cg.siptracker.model.Frequency;
import com.cg.siptracker.model.SIP;

import java.time.LocalDate;
import java.util.TreeMap;

public interface IAnalyticsService {
    SipSummaryDto analyzeSIP(SIP sip);

    LocalDate incrementDate(LocalDate date, Frequency frequency);

    double calculateCAGR(double invested, double current, long years);

    double calculateXIRR(TreeMap<LocalDate, Double> cashFlows);

    double calculateTotalUnits(SIP sip);

    void checkAndSendDropAlert(SIP sip, String userEmail);
}
