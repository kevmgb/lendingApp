package com.example.lendingApp.enums;

public enum LoanStatus {
    ACTIVE("Active"),
    REPAID("Repaid"),
    DEFAULTED("Defaulted");

    private final String displayName;

    LoanStatus(String displayName) {
        this.displayName = displayName;
    }
}

