import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class TransactionEntry {

    public static void addTransaction(
            Scanner scanner, boolean isDeposit) {

        if (isDeposit) {
            System.out.println("\n ADD FOOD SALE ");
        } else {
            System.out.println("\n ADD KITCHEN EXPENSE ");
        }

        System.out.print("Description: ");
        String description = scanner.nextLine().trim();

        System.out.print("Name of customer or vendor: ");
        String vendor = scanner.nextLine().trim();

        // Makes sure the user does not enter any special characters
        if (!description.matches("[a-zA-Z0-9 ]+") ||
                !vendor.matches("[a-zA-Z0-9 ]+")) {

            System.out.println(
                    "Please do not enter any special characters in your description"
            );

            return;
        }

        System.out.print("Amount: $");
        String amountInput = scanner.nextLine().trim();
        double transactionAmount;

        try {
            transactionAmount = Double.parseDouble(amountInput);

            if (!Double.isFinite(transactionAmount) ||
                    transactionAmount <= 0) {

                System.out.println("Amount must be greater than zero.");
                return;
            }

        } catch (NumberFormatException e) {

            System.out.println("Please enter a valid amount.");
            return;
        }

        // Converts kitchen expenses into negative amounts
        if (!isDeposit) {
            transactionAmount = -transactionAmount;
        }

        // Creates a transaction using today's date and time
        Transaction newTransaction = new Transaction(
                LocalDate.now(),
                LocalTime.now().withNano(0),
                description,
                vendor,
                transactionAmount
        );

        // Saves the transaction
        boolean wasSaved =
                TransactionRecords.saveTransaction(newTransaction);

        if (wasSaved) {

            if (isDeposit) {
                System.out.println("Sale recorded!");
            } else {
                System.out.println("Kitchen expense recorded!");
            }
        }
    }
}

