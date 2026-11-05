
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Transaction {

    private LocalDate date;
    private LocalTime time;
    private String description;
    private String vendor;
    private double amount;

    // Constructor
    public Transaction(LocalDate date, LocalTime time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
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
    public String getVendor() {
        return vendor;
    }
    public double getAmount() {
        return amount;
    }

    // Takes the transaction and turns it into the CSV format
    public String toCsv() {

        DateTimeFormatter timeFormat =
                DateTimeFormatter.ofPattern("HH:mm:ss");

        return date + "|" +
                time.format(timeFormat) + "|" +
                description + "|" +
                vendor + "|" +
                amount;
    }


    public void displayTransaction() {

        DateTimeFormatter timeFormat =
                DateTimeFormatter.ofPattern("HH:mm:ss");

        System.out.printf(
                "%-12s %-10s %-25.25s %-20.20s $%10.2f%n",
                date,
                time.format(timeFormat),
                description,
                vendor,
                amount
        );
    }
}
