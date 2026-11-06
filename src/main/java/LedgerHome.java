


import java.util.Scanner;
public class LedgerHome {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        if (!Login.checkPassword(scanner)) {
            scanner.close();
            return;
        }

        boolean isAppRunning = true;
        while (isAppRunning) {

            System.out.println("\n Would you like to...");
            System.out.println("D) Add Deposit (Food Sale)");
            System.out.println("P) Make Payment (Kitchen Expense)");
            System.out.println("L) View Ledger");
            System.out.println("X) Exit");

            System.out.print("\nChoose an option: ");
            String userChoice = scanner.nextLine().trim().toUpperCase();

            switch (userChoice) {

                case "D":
                    TransactionEntry.addTransaction(scanner, true);
                    break;

                case "P":
                    TransactionEntry.addTransaction(scanner, false);
                    break;

                case "L":
                    LedgerMenu.open(scanner);
                    break;

                case "X":
                    isAppRunning = false;
                    System.out.println("\nSystem closed");
                    System.out.println("Keep cooking up success!");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }
}
