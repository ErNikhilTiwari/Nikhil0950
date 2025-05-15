package com.cg.siptracker.dto;


public class SipSummaryDto {

    private String fundName;
    private double investedAmount;
    private double currentValue;
    private double xirr; // in percentage (e.g. 12.5 means 12.5%)
    private double cagr; // in percentage

    public SipSummaryDto(String fundName, double investedAmount, double currentValue, double xirr, double cagr) {
        this.fundName = fundName;
        this.investedAmount = investedAmount;
        this.currentValue = currentValue;
        this.xirr = xirr;
        this.cagr = cagr;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public double getInvestedAmount() {
        return investedAmount;
    }

    public void setInvestedAmount(double investedAmount) {
        this.investedAmount = investedAmount;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    public double getXirr() {
        return xirr;
    }

    public void setXirr(double xirr) {
        this.xirr = xirr;
    }

    public double getCagr() {
        return cagr;
    }

    public void setCagr(double cagr) {
        this.cagr = cagr;
    }

    @Override
    public String toString() {
        return "SIPSummaryDTO{" +
                "fundName='" + fundName + '\'' +
                ", investedAmount=" + investedAmount +
                ", currentValue=" + currentValue +
                ", xirr (%)=" + xirr +
                ", cagr (%)=" + cagr +
                '}';
    }
}
