package com.example.lendingApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/*
 * This class simulates the dumping of database record to a sftp server
 * on midnight on the first day of every month.
 * */
@Service
@EnableScheduling
@RequiredArgsConstructor
public class DatabaseDumpService {
    
    @Scheduled(cron = "0 0 0 1 * ?")
    public void generateAndUploadDump() {
        // Generate the database dump
        String dumpFilePath = generateDatabaseDump();

        // Upload the dump file to the SFTP server
        uploadDumpToSftp(dumpFilePath);
    }

    private String generateDatabaseDump() {
       // Fetch all records from table (or based on a criteria) and generate a file
        // Returns location of the file
        return "test-file-path";
    }

    private void uploadDumpToSftp(String dumpFilePath) {
        // Fetch file and upload to sftp server
    }
}
