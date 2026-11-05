



import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class TransactionRecords {

    private static final String FILE_NAME = "transactions.csv";

    // Saves a transaction to the CSV file
    public static boolean saveTransaction(Transaction transaction) {

        try {

            FileWriter transactionFileWriter =
                    new FileWriter(FILE_NAME, true);

            BufferedWriter transactionWriter =
                    new BufferedWriter(transactionFileWriter);

            transactionWriter.write(transaction.toCsv());
            transactionWriter.newLine();
            transactionWriter.close();

            System.out.println("Transaction saved successfully!");
            return true;

        } catch (IOException error) {

            System.out.println(
                    "Error saving transaction: " + error.getMessage()
            );
            return false;
        }
    }

    // Read transactions from the CSV file
    public static ArrayList<Transaction> getAllTransactions() {

        ArrayList<Transaction> allTransactions =
                new ArrayList<Transaction>();

        try {

            FileReader transactionFileReader =
                    new FileReader(FILE_NAME);

            BufferedReader transactionReader =
                    new BufferedReader(transactionFileReader);

            String transactionLine;

            while ((transactionLine = transactionReader.readLine()) != null) {


                if (transactionLine.trim().isEmpty()) {
                    continue;
                }

                // Separate the CSV line into five transaction details
                String[] transactionDetails =
                        transactionLine.split("\\|", -1);

                if (transactionDetails.length != 5) {
                    System.out.println(
                            "Skipping invalid record: " + transactionLine
                    );
                    continue;
                }

                // Skips the first row in CSV
                if (transactionDetails[0].trim().equalsIgnoreCase("date") &&
                        transactionDetails[1].trim().equalsIgnoreCase("time")) {
                    continue;
                }

                try {

                    // Convert CSV text into the correct data types
                    LocalDate transactionDate =
                            LocalDate.parse(transactionDetails[0].trim());

                    LocalTime transactionTime =
                            LocalTime.parse(transactionDetails[1].trim());

                    String transactionDescription =
                            transactionDetails[2].trim();

                    String transactionVendor =
                            transactionDetails[3].trim();

                    double transactionAmount =
                            Double.parseDouble(transactionDetails[4].trim());

                    // Check for invalid transaction information
                    if (transactionDescription.isEmpty() ||
                            transactionVendor.isEmpty() ||
                            transactionAmount > 10000.00 ||
                            transactionAmount < -10000.00 ||
                            transactionAmount != transactionAmount) {

                        System.out.println(
                                "Invalid information: " + transactionLine
                        );
                        continue;
                    }

                    // Creates a Transaction object
                    Transaction savedTransaction = new Transaction(
                            transactionDate,
                            transactionTime,
                            transactionDescription,
                            transactionVendor,
                            transactionAmount
                    );

                    // Adds the transaction to the ArrayList
                    allTransactions.add(savedTransaction);

                } catch (Exception error) {

                    System.out.println(
                            "Skipping invalid record: " + transactionLine
                    );
                }
            }

            transactionReader.close();

        } catch (FileNotFoundException error) {

            System.out.println("Transaction file not found.");

        } catch (IOException error) {

            System.out.println(
                    "Error reading transactions: " + error.getMessage()
            );
        }

        // Sorts transactions from newest to oldest
        for (int currentIndex = 0; currentIndex < allTransactions.size(); currentIndex++) {

            for (int comparisonIndex = currentIndex + 1;
                 comparisonIndex < allTransactions.size();
                 comparisonIndex++) {

                Transaction currentTransaction =
                        allTransactions.get(currentIndex);

                Transaction comparisonTransaction =
                        allTransactions.get(comparisonIndex);

                // Convert dates into numbers for comparison
                int currentTransactionDate =
                        currentTransaction.getDate().getYear() * 10000 +
                                currentTransaction.getDate().getMonthValue() * 100 +
                                currentTransaction.getDate().getDayOfMonth();

                int comparisonTransactionDate =
                        comparisonTransaction.getDate().getYear() * 10000 +
                                comparisonTransaction.getDate().getMonthValue() * 100 +
                                comparisonTransaction.getDate().getDayOfMonth();

                // Convert times into numbers for comparison
                int currentTransactionTime =
                        currentTransaction.getTime().getHour() * 10000 + currentTransaction.getTime().getMinute() * 100 + currentTransaction.getTime().getSecond();

                int comparisonTransactionTime =
                        comparisonTransaction.getTime().getHour() * 10000 + comparisonTransaction.getTime().getMinute() * 100 + comparisonTransaction.getTime().getSecond();

                // Swap positions when the comparison transaction is newer
                if (comparisonTransactionDate > currentTransactionDate || (comparisonTransactionDate == currentTransactionDate && comparisonTransactionTime > currentTransactionTime)) {

                    allTransactions.set(
                            currentIndex, comparisonTransaction
                    );

                    allTransactions.set(comparisonIndex, currentTransaction);
                }
            }
        }

        return allTransactions;
    }

    // Display transactions in a formatted table
    public static void displayTransactions( ArrayList<Transaction> transactionsToDisplay) {

        if (transactionsToDisplay.isEmpty()) {
            System.out.println("\nNo transactions found.");
            return;
        }

        System.out.printf(
                "%-12s %-10s %-25s %-20s %11s%n",
                "DATE",
                "TIME",
                "DESCRIPTION",
                "VENDOR",
                "AMOUNT"
        );

        // Display each transaction
        for (Transaction currentTransaction : transactionsToDisplay) {
            currentTransaction.displayTransaction();
        }
    }
}
