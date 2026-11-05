
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Transaction {

    private LocalDate date;
    private LocalTime time;
    private String description;
    private String customer;
    private String vendor;
    private double amount;

    // Constructor
    public Transaction(LocalDate date, LocalTime time, String description, String customer, String vendor, double amount) {

        this.date = date;
        this.time = time;
        this.description = description;
        this.customer = customer;
        this.vendor = vendor;
        this.amount = amount;
    }

    // Getters
    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getCustomer() {
        return customer;
    }

    public String getVendor() {
        return vendor;
    }

    public double getAmount() {
        return amount;
    }

    // Convert the transaction into CSV text
    public String toCsv() {

        DateTimeFormatter transactionTimeFormat =
                DateTimeFormatter.ofPattern("HH:mm:ss");

        return date + "|" +
                time.format(transactionTimeFormat) + "|" +
                description + "|" +
                customer + "|" +
                vendor + "|" +
                amount;
    }

    public void displayTransaction() {

        DateTimeFormatter transactionTimeFormat =
                DateTimeFormatter.ofPattern("HH:mm:ss");

        System.out.printf(
                "%-12s %-10s %-25.25s %-20.20s %-20.20s $%10.2f%n",
                date,
                time.format(transactionTimeFormat),
                description,
                customer,
                vendor,
                amount
        );
    }
}
