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

        System.out.print("Customer or vendor: ");
        String vendor = scanner.nextLine().trim();

        // Validate the description and vendor
        if (!isValidText(description) || !isValidText(vendor)) {

            System.out.println(
                    "Please use only letters, numbers, and spaces."
            );
            return;
        }

        System.out.print("Amount: $");
        String amountInput = scanner.nextLine().trim();

        // Checks that the amount contains only valid characters
        if (!isValidAmount(amountInput)) {

            System.out.println(
                    "Please enter a valid amount, such as 85.00."
            );
            return;
        }

        double transactionAmount;

        try {

            // Convert the amount from a String into a double
            transactionAmount = Double.parseDouble(amountInput);

        } catch (NumberFormatException error) {

            System.out.println("Please enter a valid amount.");
            return;
        }

        // Checks that the amount is within the allowed range
        if (transactionAmount <= 0 || transactionAmount > 10000.00) {

            System.out.println(
                    "Amount must be greater than zero and under 10,000."
            );
            return;
        }

        // Convert kitchen expenses into negative amounts
        if (!isDeposit) {
            transactionAmount = -transactionAmount;
        }

        // Create a Transaction object using today's date and time
        Transaction newTransaction = new Transaction(
                LocalDate.now(),
                LocalTime.now(),
                description,
                vendor,
                transactionAmount
        );

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

    // Check that the user enters only letters, numbers, and spaces
    public static boolean isValidText(String userInput) {

        if (userInput.isEmpty()) {
            return false;
        }

        for (int characterIndex = 0; characterIndex < userInput.length(); characterIndex++) {

            char currentCharacter = userInput.charAt(characterIndex);

            boolean isLowercaseLetter =
                    currentCharacter >= 'a' && currentCharacter <= 'z';

            boolean isUppercaseLetter =
                    currentCharacter >= 'A' && currentCharacter <= 'Z';

            boolean isNumber =
                    currentCharacter >= '0' && currentCharacter <= '9';

            boolean isSpace = currentCharacter == ' ';

            // Rejects any special characters
            if (!isLowercaseLetter && !isUppercaseLetter &&
                    !isNumber && !isSpace) {

                return false;
            }
        }

        return true;
    }

    // Checks that the amount contains digits and at most one decimal point
    public static boolean isValidAmount(String amountInput) {

        if (amountInput.isEmpty()) {
            return false;
        }

        int decimalPointCount = 0;
        int digitCount = 0;

        for (int characterIndex = 0; characterIndex < amountInput.length(); characterIndex++) {

            char currentCharacter = amountInput.charAt(characterIndex);

            if (currentCharacter == '.') {

                decimalPointCount++;

            } else if (currentCharacter >= '0' &&
                    currentCharacter <= '9') {

                digitCount++;

            } else {

                return false;
            }
        }

        return decimalPointCount <= 1 && digitCount > 0;
    }
}
