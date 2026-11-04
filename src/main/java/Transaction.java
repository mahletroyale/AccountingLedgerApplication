

import java.time.LocalDate;
import java.time.LocalTime;

public class Transaction {

    private LocalDate date;
    private LocalTime time;
    private String description;
    private String vendor;
    private double amount;


    public Transaction(LocalDate date, LocalTime time,
                       String description, String vendor, double amount) {

        // Constructors
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getVendor() {
        return vendor;
    }

    public double getAmount() {
        return amount;
    }

    // Converts transaction into a CSV line
    public String toCsv() {
        return date + "|" + time.withNano(0) + "|" +
                description + "|" + vendor + "|" + amount;
    }


    public void displayTransaction() {

        System.out.printf(
                "%-12s %-10s %-25.25s %-20.20s $%10.2f%n",
                date,
                time.withNano(0),
                description,
                vendor,
                amount
        );
    }
}

