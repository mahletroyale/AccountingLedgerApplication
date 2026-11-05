
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class TransactionEntry {

    // Add a new food sale or kitchen expense
    public static void addTransaction(
            Scanner scanner, boolean isDeposit) {

        if (isDeposit) {
            System.out.println("\nADD FOOD SALE");
        } else {
            System.out.println("\nADD KITCHEN EXPENSE");
        }

        System.out.print("Description: ");
        String description = scanner.nextLine().trim();

        // Default values
        String customer = "N/A";
        String vendor = "N/A";

        // Ask for the correct name based on the transaction type
        if (isDeposit) {

            System.out.print("Customer name: ");
            customer = scanner.nextLine().trim();

        } else {

            System.out.print("Vendor name: ");
            vendor = scanner.nextLine().trim();
        }

        // Validate the description and the required name
        if (!isValidText(description) ||
                (isDeposit && !isValidText(customer)) ||
                (!isDeposit && !isValidText(vendor))) {

            System.out.println(
                    "Please use only letters, numbers, and spaces."
            );
            return;
        }

        System.out.print("Amount: $");
        String amountInput = scanner.nextLine().trim();

        // Validate the amount
        if (!isValidAmount(amountInput)) {

            System.out.println(
                    "Please enter a valid amount, such as 85.00."
            );
            return;
        }

        double transactionAmount;

        try {

            // Convert the amount from String into double
            transactionAmount = Double.parseDouble(amountInput);

        } catch (NumberFormatException error) {

            System.out.println("Please enter a valid amount.");
            return;
        }

        // Checks the business transaction limit of $5000
        if (transactionAmount <= 0 ||
                transactionAmount > 5000.00) {

            System.out.println(
                    "Amount must be greater than $0 and cannot exceed $5,000."
            );
            return;
        }

        // Converts kitchen expenses into negative amounts since it is money leaving the business
        if (!isDeposit) {
            transactionAmount = -transactionAmount;
        }

        // Create a new Transaction object
        Transaction newTransaction = new Transaction(
                LocalDate.now(),
                LocalTime.now(),
                description,
                customer,
                vendor,
                transactionAmount
        );

        // Save the transaction
        boolean wasSaved = TransactionRecords.saveTransaction(newTransaction);

        if (wasSaved) {

            if (isDeposit) {
                System.out.println("Sale recorded!");
            } else {
                System.out.println("Kitchen expense recorded!");
            }
        }
    }

    // Checks for letters, numbers, and spaces only
    public static boolean isValidText(String userInput) {

        if (userInput.isEmpty()) {
            return false;
        }

        for (int characterIndex = 0; characterIndex < userInput.length(); characterIndex++) {

            char currentCharacter = userInput.charAt(characterIndex);

            boolean isLowercaseLetter = currentCharacter >= 'a' && currentCharacter <= 'z';

            boolean isUppercaseLetter = currentCharacter >= 'A' && currentCharacter <= 'Z';

            boolean isNumber = currentCharacter >= '0' && currentCharacter <= '9';

            boolean isSpace = currentCharacter == ' ';

            if (!isLowercaseLetter && !isUppercaseLetter && !isNumber && !isSpace) {
                return false;
            }
        }

        return true;
    }

    // Checks that the amount has digits and at most one decimal point
    public static boolean isValidAmount(String amountInput) {

        if (amountInput.isEmpty()) {
            return false;
        }

        int decimalPointCount = 0;
        int digitCount = 0;

        for (int characterIndex = 0;
             characterIndex < amountInput.length();
             characterIndex++) {

            char currentCharacter = amountInput.charAt(characterIndex);

            if (currentCharacter == '.') {
                decimalPointCount++;

            } else if (currentCharacter >= '0' && currentCharacter <= '9') {
                digitCount++;

            } else {

                return false;
            }
        }

        return decimalPointCount <= 1 && digitCount > 0;
    }
}
