package com.charter.codeTest.rewardsCalculator.domain;

import java.time.LocalDate;
import java.util.Date;

public class Transaction {

    private LocalDate date;

    private Double amountSpent;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getAmountSpent() {
        return amountSpent;
    }

    public void setAmountSpent(Double amountSpent) {
        this.amountSpent = amountSpent;
    }
}
