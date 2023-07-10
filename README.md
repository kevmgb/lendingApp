# Lending App

The lending app service provides simple functionality for loan requests and repayments.

DATABASE SCHEMA
FIELD   TYPE    NULL    KEY     DEFAULT  
ID  BIGINT  NO  PRI NULL
AMOUNT  NUMERIC(38, 2)  YES     NULL
DATE_CREATED    TIMESTAMP(6)    YES     NULL
DATE_UPDATED    TIMESTAMP(6)    YES     NULL
MSISDN  CHARACTER VARYING(255)  YES     NULL
REPAYMENT_AMOUNT    NUMERIC(38, 2)  YES     NULL
STATUS  CHARACTER VARYING(255)  YES     NULL

Assumptions:
1. Only one loan can be active at a time.
2. Minimum loan request amount of 100.
3. Maximum loan request of amount 100,000.
4. Minimum loan repayment amount of 1.


# Features

1. Request for a loan
2. Repay Loan
3. Clear defaulted loans
4. Dump records to sftp server

## Prerequisites

Before installing and running the service, ensure that you have the following prerequisites:

-   Java 17 or higher
-   Maven 3.x

## Installation
To install and run the Sample Service locally, follow these steps:

1.  Clone the repository
2.  Navigate to the project directory
3.  Build the application using Maven
    `mvn clean package`
4.  Start service:
    `java -jar target/{name of jar file in target folder}.jar`

## Swagger UI

Once service is running, use the link http://localhost:8080/swagger-ui/index.html to view documentation.

## Usage

Use the collection in the static folder to interact with the service