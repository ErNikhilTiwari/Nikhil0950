package com.cg.siptracker.service;

import com.cg.siptracker.dto.SipSummaryDto;
import com.cg.siptracker.model.Frequency;
import com.cg.siptracker.model.NAVRecord;
import com.cg.siptracker.model.SIP;
import com.cg.siptracker.repository.NAVRecordRepository;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

@Service
public class AnalyticsServiceImpl implements IAnalyticsService {

    @Autowired
    private NAVRecordRepository navRecordRepository;

    @Autowired
    EmailService emailService;

    @Override
    public SipSummaryDto analyzeSIP(SIP sip) {
        LocalDate today = LocalDate.now();
        LocalDate date = sip.getStartDate();
        double amountPerInstallment = sip.getAmount();
//        double latestNav = sip.getLatestNav();

        double latestNav = navRecordRepository.findTopByFundNameOrderByDateDesc(sip.getFundName())
                .map(NAVRecord::getNavValue)
                .orElseThrow(() -> new RuntimeException("No NAV found for " + sip.getFundName()));


        TreeMap<LocalDate, Double> cashFlows = new TreeMap<>();
        double totalUnits = 0;
        double totalInvested = 0;

        while (!date.isAfter(today)) {
            cashFlows.put(date, -amountPerInstallment);
            totalInvested += amountPerInstallment;
            totalUnits += amountPerInstallment / latestNav; // assume latest NAV was constant
            date = incrementDate(date, sip.getFrequency());
        }

        double currentValue = totalUnits * latestNav;
        cashFlows.put(today, currentValue);

        long years = Math.max(1, java.time.temporal.ChronoUnit.DAYS.between(sip.getStartDate(), today) / 365);

        double xirr = calculateXIRR(cashFlows);
        double cagr = calculateCAGR(totalInvested, currentValue, years);

        return new SipSummaryDto(
                sip.getFundName(),
                totalInvested,
                currentValue,
                xirr * 100,
                cagr * 100
        );
    }

    @Override
    public LocalDate incrementDate(LocalDate date, Frequency frequency) {
        return switch (frequency) {
            case DAILY -> date.plusDays(1);
            case WEEKLY -> date.plusWeeks(1);
            case MONTHLY -> date.plusMonths(1);
//            case QUARTERLY -> date.plusMonths(3);
//            case YEARLY -> date.plusYears(1);
        };
    }

    @Override
    public double calculateCAGR(double invested, double current, long years) {
        if (invested <= 0 || years <= 0) return 0;
        return Math.pow(current / invested, 1.0 / years) - 1;
    }

    @Override
    public double calculateXIRR(TreeMap<LocalDate, Double> cashFlows) {
        UnivariateFunction irrFunction = rate -> {
            double result = 0.0;
            LocalDate start = cashFlows.firstKey();
            for (var entry : cashFlows.entrySet()) {
                long days = java.time.temporal.ChronoUnit.DAYS.between(start, entry.getKey());
                result += entry.getValue() / Math.pow(1 + rate, days / 365.0);
            }
            return result;
        };

        try {
            UnivariateSolver solver = new BrentSolver(1e-10);
            return solver.solve(1000, irrFunction, -0.9999, 10);
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public double calculateTotalUnits(SIP sip) {
        LocalDate date = sip.getStartDate();
        LocalDate today = LocalDate.now();
        double totalUnits = 0;

        while (!date.isAfter(today)) {
            Optional<NAVRecord> navOpt = navRecordRepository.findByFundNameAndDate(sip.getFundName(), date);
            if (navOpt.isPresent()) {
                double nav = navOpt.get().getNavValue();
                totalUnits += sip.getAmount() / nav;
            }
            date = incrementDate(date, sip.getFrequency());
        }
        return totalUnits;
    }
    @Override
    public void checkAndSendDropAlert(SIP sip, String userEmail) {
        List<NAVRecord> navs = navRecordRepository.findTop2ByFundNameOrderByDateDesc(sip.getFundName());


        if (navs.size() < 2) return; // need 2 dates to compare

        NAVRecord latest = navs.get(0);
        NAVRecord previous = navs.get(1);

        double dropPercentage = ((previous.getNavValue() - latest.getNavValue()) / previous.getNavValue()) * 100;

        if (dropPercentage >= 10.0) {
            double units = calculateTotalUnits(sip); // based on historical navs
            double currentValue = units * latest.getNavValue();
            double previousValue = units * previous.getNavValue();
            double loss = previousValue - currentValue;

            String subject = "⚠️ SIP Alert: NAV Dropped >10% for " + sip.getFundName();
            String body = String.format("""
                Alert! The NAV for your fund '%s' dropped by %.2f%%.

                Previous NAV (on %s): %.4f
                Latest NAV (on %s): %.4f

                Estimated Loss on Your Holdings: ₹%.2f

                Keep monitoring your investments.
                """,
                    sip.getFundName(),
                    dropPercentage,
                    previous.getDate(),
                    previous.getNavValue(),
                    latest.getDate(),
                    latest.getNavValue(),
                    loss
            );

            emailService.sendEmail(userEmail, subject, body);
        }
    }

}



