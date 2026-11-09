

import java.util.ArrayList;

public class FinancialSummary {

    public static void displaySummary() {

        double totalIncome = 0;
        double totalExpenses = 0;

        ArrayList<Transaction> allTransactions = TransactionRecords.getAllTransactions();

        for (Transaction transaction : allTransactions) {
            if (transaction.getAmount() > 0) {

                totalIncome += transaction.getAmount();

            } else if (transaction.getAmount() < 0) {

                totalExpenses += Math.abs(transaction.getAmount());
            }
        }

        double netBalance = totalIncome - totalExpenses;

        System.out.println("\n====================================");
        System.out.println("           Kitchen Summary            ");
        System.out.println("======================================");
        System.out.printf(
                "Total Food Sales: $%,.2f%n", totalIncome
        );

        System.out.printf(
                "Total Expenses:   $%,.2f%n", totalExpenses
        );

        System.out.println("------------------------------------");

        System.out.printf(
                "Net Balance:      $%,.2f%n", netBalance
        );

        if (netBalance > 0) {

            System.out.println(
                    "\nKeep it up! \uD83D\uDCB0"
            );

        } else if (netBalance < 0) {

            System.out.println(
                    "\nLet's review those kitchen expenses \uD83D\uDE2C"
            );

        } else {

            System.out.println(
                    "\nYour income and expenses are balanced."
            );
        }
    }
}

