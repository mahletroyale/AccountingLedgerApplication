
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class TransactionRecords {

    private static final String FILE_NAME = "src/main/java/Transaction.csv";

    // Saves a transaction to the CSV file
    public static boolean saveTransaction(Transaction transaction) {

        try {

            FileWriter transactionFileWriter = new FileWriter(FILE_NAME, true);

            BufferedWriter transactionWriter = new BufferedWriter(transactionFileWriter);

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

    // Reads all transactions from the CSV file
    public static ArrayList<Transaction> getAllTransactions() {

        ArrayList<Transaction> allTransactions = new ArrayList<Transaction>();

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

                // Separate the CSV line into six transaction details
                String[] transactionDetails =
                        transactionLine.split("\\|", -1);

                if (transactionDetails.length != 6) {

                    System.out.println(
                            "Skipping invalid record: " + transactionLine
                    );
                    continue;
                }

                // Skip the first row of CSV since it is the header
                if (transactionDetails[0].trim().equalsIgnoreCase("date") &&
                        transactionDetails[1].trim().equalsIgnoreCase("time")) {
                    continue;
                }

                try {

                    // Converts CSV text into the correct data types
                    LocalDate transactionDate =
                            LocalDate.parse(transactionDetails[0].trim());

                    LocalTime transactionTime =
                            LocalTime.parse(transactionDetails[1].trim());

                    String transactionDescription =
                            transactionDetails[2].trim();

                    String transactionCustomer =
                            transactionDetails[3].trim();

                    String transactionVendor =
                            transactionDetails[4].trim();

                    double transactionAmount =
                            Double.parseDouble(transactionDetails[5].trim());

                    // Check for invalid transaction information
                    if (transactionDescription.isEmpty() || (transactionAmount > 0 && transactionCustomer.isEmpty()) ||
                            (transactionAmount < 0 && transactionVendor.isEmpty()) ||
                            transactionAmount == 0 ||
                            transactionAmount > 5000.00 ||
                            transactionAmount < -5000.00 ||
                            transactionAmount != transactionAmount) {

                        System.out.println(
                                "Skipping transaction with invalid information: "
                                        + transactionLine
                        );
                        continue;
                    }

                    // Creates Transaction objects
                    Transaction savedTransaction = new Transaction(
                            transactionDate,
                            transactionTime,
                            transactionDescription,
                            transactionCustomer,
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


        // Sort transactions from newest to oldest
        for (int currentIndex = 0; currentIndex < allTransactions.size(); currentIndex++) {

            for (int comparisonIndex = currentIndex + 1; comparisonIndex < allTransactions.size(); comparisonIndex++) {

                Transaction currentTransaction = allTransactions.get(currentIndex);
                Transaction comparisonTransaction = allTransactions.get(comparisonIndex);

                // Get the dates and times of both transactions
                LocalDate currentDate = currentTransaction.getDate();
                LocalDate comparisonDate = comparisonTransaction.getDate();

                LocalTime currentTime = currentTransaction.getTime();
                LocalTime comparisonTime = comparisonTransaction.getTime();

                // Check if the comparison transaction is newer
                if (comparisonDate.isAfter(currentDate) || (comparisonDate.isEqual(currentDate) && comparisonTime.isAfter(currentTime))) {

                    // Swap the transactions
                    allTransactions.set(currentIndex, comparisonTransaction);
                    allTransactions.set(comparisonIndex, currentTransaction);
                }
            }
        }

        return allTransactions;

    }

    public static void displayTransactions(
            ArrayList<Transaction> transactionsToDisplay) {

        if (transactionsToDisplay.isEmpty()) {

            System.out.println("\nNo transactions found.");
            return;
        }

        System.out.printf(
                "%-12s %-10s %-25s %-20s %-20s %11s%n",
                "DATE",
                "TIME",
                "DESCRIPTION",
                "CUSTOMER",
                "VENDOR",
                "AMOUNT"
        );

        System.out.println(
                "---------------------------------------------------------------------------------------------------------"
        );

        for (Transaction currentTransaction : transactionsToDisplay) {
            currentTransaction.displayTransaction();
        }
    }
}
