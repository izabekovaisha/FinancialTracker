package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class FinancialTracker {

    private static final ArrayList<Transaction> transactions = new ArrayList<>(); // Initialize an ArrayList to store transactions
    private static final String FILE_NAME = "transactions.csv"; // Define the file name for transactions

    // Define date and time formats
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";

    // Create the DateTimeFormatter objects for date and time formatting
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    // main method to execute the FinancialTracker program
    public static void main(String[] args) {
        loadTransactions(FILE_NAME);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Welcome to TransactionApp");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D":
                    addDeposit(scanner);
                    break;
                case "P":
                    addPayment(scanner);
                    break;
                case "L":
                    ledgerMenu(scanner);
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }

        scanner.close();
    }

    // Load transactions from the given CSV file
    public static void loadTransactions(String fileName) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split("\\|");

                if (parts.length == 5) {
                    LocalDate date = LocalDate.parse(parts[0].trim(), DATE_FORMATTER);
                    LocalTime time = LocalTime.parse(parts[1].trim(), TIME_FORMATTER);
                    String description = parts[2].trim();
                    String vendor = parts[3].trim();
                    double amount = Double.parseDouble(parts[4]);
                    transactions.add(new Transaction(date, time, description, vendor, amount));
                }
            }
            bufferedReader.close();

        } catch (Exception e) {
            System.out.println("Error loading inventory: " + e.getMessage());
        }
    }

    // Add a deposit to the list of transactions
    private static void addDeposit(Scanner scanner) {
        System.out.println("Add Deposit");

        System.out.println("Enter the date (format: yyyy-MM-dd): ");
        LocalDate date = LocalDate.parse(scanner.nextLine().trim(), DATE_FORMATTER);

        System.out.println("Enter the time (format: HH:mm:ss): ");
        LocalTime time = LocalTime.parse(scanner.nextLine().trim(), TIME_FORMATTER);

        System.out.println("Enter the description: ");
        String description = scanner.nextLine().trim();

        System.out.println("Enter the vendor: ");
        String vendor = scanner.nextLine().trim();

        System.out.println("Enter the amount: ");
        double amount = Double.parseDouble(scanner.nextLine().trim());

        if (amount <= 0) {
            System.out.println("Invalid amount. Please enter a positive number.");
            return;
        }
        transactions.add(new Transaction(date, time, description, vendor, amount));
        System.out.println("Deposit added successfully");

        try { // Write deposit details to file
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true));
            writer.write(date + "|" + time + "|" + description + "|" + vendor + "|" + amount);
            writer.newLine();
            writer.close();
            System.out.println("Deposit added successfully to transaction.csv");

        } catch (IOException e) {
            System.err.println(("Error writing to transaction.csv: " + e.getMessage()));
        }
    }

    // Add a payment to the list of transactions
    private static void addPayment(Scanner scanner) {
        System.out.println("Make Payment (Debit)");

        System.out.println("Enter the date (format: yyyy-MM-dd): ");
        LocalDate date = LocalDate.parse(scanner.nextLine().trim(), DATE_FORMATTER);

        System.out.println("Enter the time (format: HH:mm:ss): ");
        LocalTime time = LocalTime.parse(scanner.nextLine().trim(), TIME_FORMATTER);

        System.out.println("Enter the description: ");
        String description = scanner.nextLine().trim();

        System.out.println("Enter the vendor: ");
        String vendor = scanner.nextLine().trim();

        System.out.println("Enter the amount: ");
        double amount = Double.parseDouble(scanner.nextLine().trim());

        if (amount <= 0) {
            System.out.println("Invalid amount. Please enter a positive number.");
            return;
        }
        amount *= -1;

        // Create Transaction object and add to transactions ArrayList
        transactions.add(new Transaction(date, time, description, vendor, amount));
        System.out.println("Payment added successfully");

        try { // Write deposit details to file
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true));
            writer.write(date + "|" + time + "|" + description + "|" + vendor + "|" + amount);
            writer.newLine();
            writer.close();
            System.out.println("Payment added successfully to transaction.csv");

        } catch (IOException e) {
            System.err.println(("Error writing to transaction.csv: " + e.getMessage()));
        }
    }

    // Display the ledger menu and handle user options
    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;

        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A":
                    displayLedger();
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    reportsMenu(scanner);
                    break;
                case "H":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }


    // Display all transactions in the ledger
    private static void displayLedger() {
        System.out.println("Ledger: ");
        System.out.printf("%-15s %-15s %-30s %-25s %-15s\n", "Date", "Time", "Description", "Vendor", "Amount");
        for (Transaction transaction : transactions) {
            System.out.printf("%-15s %-15s %-30s %-25s $%-15.2f%n", transaction.getDate(), transaction.getTime(), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
        }
    }

    // Display all deposits in the ledger
    private static void displayDeposits() {
        System.out.println("All deposits: ");
        System.out.printf("%-15s %-15s %-30s %-25s %-15s\n", "Date", "Time", "Description", "Vendor", "Amount");
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                System.out.printf("%-15s %-15s %-30s %-25s $%-15.2f%n", transaction.getDate(), transaction.getTime(), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
            }
        }
    }

    // Display all payments in the ledger
    private static void displayPayments() {
        System.out.println("All payments: ");
        System.out.printf("%-15s %-15s %-30s %-25s %-15s\n", "Date", "Time", "Description", "Vendor", "Amount");
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                System.out.printf("%-15s %-15s %-30s %-25s $%-15.2f%n", transaction.getDate(), transaction.getTime(), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
            }
        }
    }

    // Display the reports menu and handle user options
    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("6) Custom Search");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    filterTransactionsByDate(LocalDate.now().withDayOfMonth(1), LocalDate.now());
                    break;

                case "2":
                    LocalDate firstDayOfPreviousMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1);
                    LocalDate lastDayOfPreviousMonth = LocalDate.now().minusMonths(1).withDayOfMonth(LocalDate.now().minusMonths(1).lengthOfMonth());

                    filterTransactionsByDate(firstDayOfPreviousMonth, lastDayOfPreviousMonth);
                    break;
                case "3":
                    filterTransactionsByDate(LocalDate.now().withDayOfYear(1), LocalDate.now());
                    break;
                case "4":
                    LocalDate firstDayOfPreviousYear = LocalDate.now().minusYears(1).withDayOfYear(1);
                    LocalDate lastDayOfPreviousYear = LocalDate.now().minusYears(1).withDayOfYear(LocalDate.now().minusYears(1).lengthOfYear());

                    filterTransactionsByDate(firstDayOfPreviousYear, lastDayOfPreviousYear);
                    break;
                case "5":
                    System.out.println("Enter the vendor: ");
                    String vendor = scanner.nextLine().trim();

                    filterTransactionsByVendor(vendor);
                    break;
                case "6":
                    customSearch(scanner);
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    // Method for performing custom search based on user-provided criteria
    private static void customSearch(Scanner scanner) {
        System.out.println("Custom Search: ");
        System.out.println("Enter start date (format: yyyy-MM-dd): ");
        LocalDate startDate = null;
        String startDateInput = scanner.nextLine().trim();

        if (!startDateInput.isEmpty()) {

            try {
                startDate = LocalDate.parse(startDateInput, DATE_FORMATTER);

            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Using current date instead.");
                startDate = LocalDate.now();
            }
        }

        System.out.println("Enter end date (format: yyyy-MM-dd): ");
        LocalDate endDate = null;
        String endDateInput = scanner.nextLine().trim();

        if (!endDateInput.isEmpty()) {

            try {
                endDate = LocalDate.parse(endDateInput, DATE_FORMATTER);

            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Using current date instead.");
                endDate = LocalDate.now();

            }

        }

        System.out.println("Enter description: ");
        String description = scanner.nextLine().trim();

        System.out.println("Enter vendor: ");
        String vendor = scanner.nextLine().trim();

        double amount = 0.0;
        System.out.println("Enter amount: ");
        String amountInput = scanner.nextLine().trim();

        if (!amountInput.isEmpty()) {

            try {
                amount = Double.parseDouble(amountInput);

            } catch (NumberFormatException e) {

                System.out.println("Invalid amount format. Using default value instead.");
                amount = 0.0;
            }
        }

        filterTransactionsByDate(startDate, endDate);

    }

    // Filter transactions by date and print the report
    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        System.out.println("Transactions between " + startDate + " and " + endDate + ":");
        for (Transaction transaction : transactions) {
            LocalDate transactionDate = transaction.getDate();
            if (transactionDate.compareTo(startDate) >= 0)
                if (transactionDate.compareTo(endDate) <= 0) {
                    System.out.println(transaction);
                }
        }
    }

    // Filter transactions by vendor and print the report
    private static void filterTransactionsByVendor(String vendor) {
        for (Transaction transaction : transactions) {
            if (transaction.getVendor().equals(vendor)) {
                System.out.println(transaction);
            }
        }
    }
}

