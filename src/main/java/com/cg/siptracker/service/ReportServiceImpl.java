package com.cg.siptracker.service;

import com.cg.siptracker.dto.SipSummaryDto;
import com.cg.siptracker.model.SIP;
import com.cg.siptracker.repository.SIPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.io.PrintWriter;
import java.util.List;

@Service
public class ReportServiceImpl implements IReportService {

    @Autowired
    private IAnalyticsService IAnalyticsService;

    @Autowired
    private SIPRepository sipRepository;

    @Override
    public byte[] generateCsvReport(String email) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));

        List<SIP> sips = sipRepository.findByUserEmail(email);
        writer.println("Fund Name,Invested Amount,Current Value,XIRR (%),CAGR (%)");

        double totalInvestment = 0.0;

        for (SIP sip : sips) {
            SipSummaryDto summary = IAnalyticsService.analyzeSIP(sip);

            writer.printf("%s,%.2f,%.2f,%.2f,%.2f%n",
                    summary.getFundName(),
                    summary.getInvestedAmount(),
                    summary.getCurrentValue(),
                    summary.getXirr(),
                    summary.getCagr());

            totalInvestment += summary.getInvestedAmount();
        }

        writer.println(); // blank line before total
        writer.printf("Total Investment,%.2f%n", totalInvestment);

        writer.flush();
        return out.toByteArray();
    }



}
