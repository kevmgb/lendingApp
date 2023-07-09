package com.example.lendingApp.service;

import com.example.lendingApp.entity.Loan;
import com.example.lendingApp.enums.LoanStatus;
import com.example.lendingApp.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/*
* This class marks loan records as defaulted.
* It determines whether a loan is defaulted based on a configurable value.
* This process runs at midnight on the first day of every month.
* */
@Service
@RequiredArgsConstructor
@EnableScheduling
public class LoanDefaultsService {
    @Value("${loan.clearing.months}")
    private int clearingMonths;
    private final LoanRepository loanRepository;

    @Scheduled(cron = "0 0 0 1 * ?")
    public void clearDefaultedLoans() {
        LocalDateTime clearingDate = LocalDateTime.now().minusMonths(clearingMonths);
        List<Loan> loansMarkedForDefault = loanRepository.findByStatusAndDateCreatedBefore(LoanStatus.ACTIVE, clearingDate);
        loansMarkedForDefault.forEach(loan -> loan.setStatus(LoanStatus.DEFAULTED));
        loanRepository.saveAll(loansMarkedForDefault);
    }
}
