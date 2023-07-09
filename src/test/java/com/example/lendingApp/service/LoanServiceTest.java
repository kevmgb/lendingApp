package com.example.lendingApp.service;

import com.example.lendingApp.entity.Loan;
import com.example.lendingApp.enums.LoanStatus;
import com.example.lendingApp.model.request.LoanRepaymentRequest;
import com.example.lendingApp.model.request.LoanRequest;
import com.example.lendingApp.model.response.Response;
import com.example.lendingApp.repository.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private LoanService loanService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void requestLoan_shouldReturnBadRequestWhenUserHasActiveLoan() {
        // Given
        LoanRequest request = new LoanRequest("0700000000", BigDecimal.valueOf(1000));
        when(loanRepository.findByMsisdnAndStatus(request.getMsisdn(), LoanStatus.ACTIVE))
                .thenReturn(Optional.of(new Loan()));

        // When
        ResponseEntity<Response> response = loanService.requestLoan(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Unable to process your request. You already have an active loan.", response.getBody().getResponseMessage());
        assertNull(response.getBody().getResponsePayload());
    }

    @Test
    void requestLoan_shouldCreateLoanAndReturnSuccessResponse() {
        // Given
        LoanRequest request = new LoanRequest("0700000000", BigDecimal.valueOf(1000));
        when(loanRepository.findByMsisdnAndStatus(request.getMsisdn(), LoanStatus.ACTIVE))
                .thenReturn(Optional.empty());

        // When
        ResponseEntity<Response> response = loanService.requestLoan(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Your loan request has been processed successfully.", response.getBody().getResponseMessage());
        assertNotNull(response.getBody().getResponsePayload());

        // Verify loanRepository and notificationService interactions
        verify(loanRepository, times(1)).save(any(Loan.class));
        verify(notificationService, times(1)).sendSMS(eq("0700000000"), anyString());
    }

    @Test
    void repayLoan_shouldReturnBadRequestWhenNoActiveLoanFound() {
        // Given
        LoanRepaymentRequest request = new LoanRepaymentRequest("0700000000", BigDecimal.valueOf(500));

        when(loanRepository.findByMsisdnAndStatus(request.getMsisdn(), LoanStatus.ACTIVE))
                .thenReturn(Optional.empty());

        // When
        ResponseEntity<Response> response = loanService.repayLoan(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("No active loan found for the provided msisdn", response.getBody().getResponseMessage());
        assertNull(response.getBody().getResponsePayload());
    }

    @Test
    void repayLoan_shouldReturnBadRequestWhenRepaymentAmountExceedsOutstandingBalance() {
        // Given
        LoanRepaymentRequest request = new LoanRepaymentRequest("0700000000", BigDecimal.valueOf(1500));
        Loan existingLoan = new Loan();
        existingLoan.setMsisdn("0700000000");
        existingLoan.setAmount(BigDecimal.valueOf(1000));
        existingLoan.setRepaymentAmount(BigDecimal.valueOf(500));

        when(loanRepository.findByMsisdnAndStatus(request.getMsisdn(), LoanStatus.ACTIVE))
                .thenReturn(Optional.of(existingLoan));

        // When
        ResponseEntity<Response> response = loanService.repayLoan(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Repayment amount exceeds outstanding balance", response.getBody().getResponseMessage());
        assertNotNull(response.getBody().getResponsePayload());

        // Verify loanRepository and notificationService interactions
        verify(loanRepository, times(0)).save(existingLoan);
        verify(notificationService, times(0)).sendSMS(eq("msisdn"), anyString());
    }

    @Test
    void repayLoan_shouldProcessRepaymentAndReturnSuccessResponse() {
        // Given
        LoanRepaymentRequest request = new LoanRepaymentRequest("0700000000", BigDecimal.valueOf(500));
        Loan existingLoan = new Loan();
        existingLoan.setMsisdn("0700000000");
        existingLoan.setAmount(BigDecimal.valueOf(1000));
        existingLoan.setRepaymentAmount(BigDecimal.valueOf(500));

        when(loanRepository.findByMsisdnAndStatus(request.getMsisdn(), LoanStatus.ACTIVE))
                .thenReturn(Optional.of(existingLoan));

        // When
        ResponseEntity<Response> response = loanService.repayLoan(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Your loan repayment request has been processed successfully.", response.getBody().getResponseMessage());
        assertNotNull(response.getBody().getResponsePayload());

        // Verify loanRepository and notificationService interactions
        verify(loanRepository, times(1)).save(existingLoan);
        verify(notificationService, times(1)).sendSMS(eq("0700000000"), anyString());
    }
}