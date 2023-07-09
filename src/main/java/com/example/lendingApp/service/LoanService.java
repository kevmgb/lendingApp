package com.example.lendingApp.service;

import com.example.lendingApp.entity.Loan;
import com.example.lendingApp.enums.LoanStatus;
import com.example.lendingApp.model.pojo.LoanPojo;
import com.example.lendingApp.model.request.LoanRepaymentRequest;
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
    private final NotificationService notificationService;

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

        notificationService.sendSMS(loan.getMsisdn(), "Dear client, your loan request of " + loan.getAmount() + " has been successfully granted.");

        return ResponseEntity
                .ok(new Response(
                        "Your loan request has been processed successfully.",
                        new LoanPojo(loan.getMsisdn(), loan.getAmount(), loan.getRepaymentAmount())));
    }

    public ResponseEntity<Response> repayLoan(LoanRepaymentRequest request) {
        // Check if user has an active loan
        Optional<Loan> existingLoan = loanRepository
                .findByMsisdnAndStatus(request.getMsisdn(), LoanStatus.ACTIVE);

        if (existingLoan.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new Response(
                            "No active loan found for the provided msisdn", null));
        }

        // Calculate outstanding loan balance
        BigDecimal outstandingBalance = existingLoan.get().getAmount().subtract(existingLoan.get().getRepaymentAmount());

        // Verify that repayment request doesn't exceed outstanding balance
        if (request.getRepaymentAmount().compareTo(outstandingBalance) > 0) {
            return ResponseEntity
                    .badRequest()
                    .body(new Response(
                            "Repayment amount exceeds outstanding balance",
                            new LoanPojo(existingLoan.get().getMsisdn(),
                                    existingLoan.get().getAmount(), existingLoan.get().getRepaymentAmount())));
        }

        // Update outstanding loan balance
        existingLoan.get().setRepaymentAmount(existingLoan.get().getRepaymentAmount().add(request.getRepaymentAmount()));

        // Check if loan has been full repaid
        if (existingLoan.get().getRepaymentAmount().compareTo(existingLoan.get().getAmount()) == 0) {
            existingLoan.get().setStatus(LoanStatus.REPAID);
        }
        loanRepository.save(existingLoan.get());

        String smsMessage = "Dear client, your loan repayment request of amount " + request.getRepaymentAmount() + " has been successfully processed.";

        if (existingLoan.get().getStatus().equals(LoanStatus.REPAID))
        {
            smsMessage += " Your loan is fully settled.";

        }

        notificationService.sendSMS(existingLoan.get().getMsisdn(), smsMessage);

        return ResponseEntity.ok(new Response(
                "Your loan repayment request has been processed successfully.",
                new LoanPojo(existingLoan.get().getMsisdn(), existingLoan.get().getAmount(), existingLoan.get().getRepaymentAmount())));
    }
}
