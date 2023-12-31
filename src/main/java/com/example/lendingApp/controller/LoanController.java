package com.example.lendingApp.controller;

import com.example.lendingApp.model.request.LoanRepaymentRequest;
import com.example.lendingApp.model.request.LoanRequest;
import com.example.lendingApp.model.response.Response;
import com.example.lendingApp.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/loan")
@RequiredArgsConstructor
public class LoanController {
    private final LoanService loanService;

    @PostMapping("/requests")
    public ResponseEntity<Response> loanRequest(@RequestBody @Validated LoanRequest request) {
        return loanService.requestLoan(request);
    }

    @PostMapping("/repayments")
    public ResponseEntity<Response> repayLoan(@RequestBody @Validated LoanRepaymentRequest request) {
        return loanService.repayLoan(request);
    }
}
