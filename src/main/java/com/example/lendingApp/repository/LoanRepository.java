package com.example.lendingApp.repository;

import com.example.lendingApp.entity.Loan;
import com.example.lendingApp.enums.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    Optional<Loan> findByMsisdnAndStatus(String msisdn, LoanStatus status);
    List<Loan> findByStatusAndDateCreatedBefore(LoanStatus status, LocalDateTime dateCreated);
}
