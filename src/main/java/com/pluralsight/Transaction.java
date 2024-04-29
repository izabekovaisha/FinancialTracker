package com.pluralsight;

public class Transaction {
    // Attributes
    private String date;
    private String time;
    private String type;
    private String vendor;
    private double amount;

    // Constructor with all attributes
    public Transaction(String date, String time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.type = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    public boolean isCredit() {
        return amount > 0;
    }

    public boolean isDebit() {
        return amount < 0;
    }
        // Getters and setters
    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public String getVendor() {
        return vendor;
    }

    public double getAmount() {
        return amount;
    }
}
}






