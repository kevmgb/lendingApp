package com.example.lendingApp.service;


import com.example.lendingApp.entity.Loan;
import com.example.lendingApp.enums.LoanStatus;
import com.example.lendingApp.repository.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LoanDefaultingServiceTest {
    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private LoanDefaultsService loanDefaultsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void clearDefaultedLoans_shouldUpdateStatusOfDefaultedLoans() {
        // Given
        List<Loan> defaultedLoans = new ArrayList<>();
        defaultedLoans.add(new Loan(1L, "0700000000", BigDecimal.valueOf(100L), LoanStatus.ACTIVE,
                BigDecimal.valueOf(0), LocalDateTime.of(2023, 1, 1, 0, 0), LocalDateTime.of(2023, 1, 1, 0, 0)));

        when(loanRepository.findByStatusAndDateCreatedBefore(any(), any())).thenReturn(defaultedLoans);

        // When
        loanDefaultsService.clearDefaultedLoans();

        // Assert
        verify(loanRepository, times(1)).saveAll(defaultedLoans);
        assertEquals(LoanStatus.DEFAULTED, defaultedLoans.get(0).getStatus());
    }
}