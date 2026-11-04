
import java.util.ArrayList;
import java.util.Scanner;

public class LedgerMenu {

    public static void open(Scanner scanner) {

        boolean isLedgerOpen = true;

        while (isLedgerOpen) {

            System.out.println("\n KITCHEN LEDGER \uD83D\uDD2A");
            System.out.println("A) All Transactions");
            System.out.println("D) Deposits / Food Sales");
            System.out.println("P) Payments / Kitchen Expenses");
            System.out.println("S) Financial Summary");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            System.out.print("\nChoose an option: ");
            String userChoice = scanner.nextLine().trim().toUpperCase();

            switch (userChoice) {

                case "A":
                    TransactionRecords.displayTransactions(
                            TransactionRecords.getAllTransactions()
                    );
                    break;

                case "D":
                    displayDepositsOrPayments(true);
                    break;

                case "P":
                    displayDepositsOrPayments(false);
                    break;

                case "S":
                    FinancialSummary.displaySummary();
                    break;

                case "R":
                    ReportsMenu.open(scanner);
                    break;

                case "H":
                    isLedgerOpen = false;
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // Display only deposits or only payments
    public static void displayDepositsOrPayments(
            boolean showDeposits) {

        ArrayList<Transaction> matchingTransactions =
                new ArrayList<>();

        ArrayList<Transaction> allTransactions =
                TransactionRecords.getAllTransactions();

        for (Transaction transaction : allTransactions) {

            if (showDeposits && transaction.getAmount() > 0) {
                matchingTransactions.add(transaction);
            }

            if (!showDeposits && transaction.getAmount() < 0) {
                matchingTransactions.add(transaction);
            }
        }

        TransactionRecords.displayTransactions(matchingTransactions);
    }
}

