package com.example.lendingApp.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class LoanPojo {
    private String msisdn;
    private BigDecimal amount;
    private BigDecimal repaymentAmount;
}
