package com.example.lendingApp.service;

import com.example.lendingApp.entity.Loan;
import com.example.lendingApp.enums.LoanStatus;
import com.example.lendingApp.model.pojo.LoanPojo;
import com.example.lendingApp.model.request.LoanRequest;
import com.example.lendingApp.model.response.Response;
import com.example.lendingApp.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoanService {
    private final LoanRepository loanRepository;

    public ResponseEntity<Response> requestLoan(LoanRequest request) {
        // Check if the user already has an active loan
        Optional<Loan> existingLoan = loanRepository
                .findByMsisdnAndStatus(request.getMsisdn(), LoanStatus.ACTIVE);

        if (existingLoan.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new Response("Unable to process your request. " +
                            "You already have an active loan.", null));
        }

        Loan loan = new Loan();
        loan.setMsisdn(request.getMsisdn());
        loan.setAmount(request.getAmount());
        loan.setStatus(LoanStatus.ACTIVE);
        loan.setRepaymentAmount(BigDecimal.ZERO); // On creation of loan record, repayment amount is set to initial value of zero
        loanRepository.save(loan);

        return ResponseEntity
                .ok(new Response(
                        "Your loan request has been processed successfully.",
                        new LoanPojo(loan.getMsisdn(), loan.getAmount(), loan.getRepaymentAmount())));
    }
}
