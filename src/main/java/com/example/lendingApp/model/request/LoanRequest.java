package com.example.lendingApp.model.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequest {

    @NotBlank
    private String msisdn;

    @NotNull
    @DecimalMin("${minimum.loan.amount}")
    @DecimalMax("${maximum.loan.amount}")
    private BigDecimal amount;
}
