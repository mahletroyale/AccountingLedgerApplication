


import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;

public class TransactionManager {

    private static final String FILE_NAME = "transactions.csv";

    // Saves a transaction to the CSV file
    public static boolean saveTransaction(Transaction transaction) {

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(FILE_NAME, true))) {

            writer.write(transaction.toCsv());
            writer.newLine();

            System.out.println("Transaction saved successfully!");
            return true;

        } catch (IOException e) {

            System.out.println(
                    "Error saving transaction: " + e.getMessage()
            );
            return false;
        }
    }

    // Reads all transactions from the CSV file
    public static ArrayList<Transaction> getAllTransactions() {

        ArrayList<Transaction> allTransactions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new FileReader(FILE_NAME))) {

            String line;
            while ((line = reader.readLine()) != null) {

                if (line.isBlank()) {
                    continue;
                }

                // Separating the five CSV transaction parts
                String[] transactionParts = line.split("\\|", -1);

                if (transactionParts.length != 5) {
                    System.out.println("Skipping invalid record: " + line);
                    continue;
                }

                // Skips the CSV header row
                if (transactionParts[0].equalsIgnoreCase("date") &&
                        transactionParts[1].equalsIgnoreCase("time")) {
                    continue;
                }

                //
                try {
                    LocalDate date = LocalDate.parse(transactionParts[0]);
                    LocalTime time = LocalTime.parse(transactionParts[1]);

                    String description = transactionParts[2];
                    String vendor = transactionParts[3];
                    double amount = Double.parseDouble(transactionParts[4]);

                    if (!Double.isFinite(amount)) {
                        continue;
                    }

                    Transaction transaction = new Transaction(
                            date,
                            time,
                            description,
                            vendor,
                            amount
                    );

                    allTransactions.add(transaction);

                } catch (RuntimeException e) {
                    System.out.println("Skipping invalid record: " + line);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Transaction file not found.");

        } catch (IOException e) {
            System.out.println(
                    "Error reading transactions: " + e.getMessage()
            );
        }

        // Sort transactions from newest to oldest
        allTransactions.sort(
                Comparator.comparing(Transaction::getDate)
                        .thenComparing(Transaction::getTime)
                        .reversed()
        );

        return allTransactions;
    }


}

