package com.cg.siptracker.service;

public interface IReportService {
    byte[] generateCsvReport(String email);
}
