
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class ReportsMenu {


    public static void open(Scanner scanner) {

        boolean isReportsOpen = true;
        while (isReportsOpen) {

            System.out.println("\n========== FINANCIAL REPORTS ==========");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("6) Custom Search");
            System.out.println("0) Back to Ledger");

            System.out.print("\nChoose a report: ");
            String userChoice = scanner.nextLine().trim();

            LocalDate today = LocalDate.now();

            switch (userChoice) {

                case "1":
                    displayTransactionsByDate(
                            today.withDayOfMonth(1),
                            today
                    );
                    break;

                case "2":
                    LocalDate previousMonth = today.minusMonths(1);

                    displayTransactionsByDate(
                            previousMonth.withDayOfMonth(1),
                            previousMonth.withDayOfMonth(
                                    previousMonth.lengthOfMonth()
                            )
                    );
                    break;

                case "3":
                    displayTransactionsByDate(
                            today.withDayOfYear(1),
                            today
                    );
                    break;

                case "4":
                    LocalDate previousYear = today.minusYears(1);

                    displayTransactionsByDate(
                            previousYear.withDayOfYear(1),
                            previousYear.withDayOfYear(
                                    previousYear.lengthOfYear()
                            )
                    );
                    break;

                case "5":
                    searchByVendor(scanner);
                    break;

                case "6":
                    customSearch(scanner);
                    break;

                case "0":
                    isReportsOpen = false;
                    break;

                default:
                    System.out.println(
                            "Invalid report option. Try again."
                    );
            }
        }
    }

    // Display transactions within a date range
    public static void displayTransactionsByDate(
            LocalDate startDate, LocalDate endDate) {

        ArrayList<Transaction> matchingTransactions =
                new ArrayList<>();

        ArrayList<Transaction> allTransactions =
                TransactionRecords.getAllTransactions();

        for (Transaction transaction : allTransactions) {

            LocalDate transactionDate = transaction.getDate();

            if (!transactionDate.isBefore(startDate) &&
                    !transactionDate.isAfter(endDate)) {

                matchingTransactions.add(transaction);
            }
        }

        System.out.println(
                "\nTransactions from " + startDate + " to " + endDate
        );

        TransactionRecords.displayTransactions(matchingTransactions);
    }

    // Search for transactions by vendor or customer
    public static void searchByVendor(Scanner scanner) {

        System.out.print("\nEnter customer or vendor name: ");
        String vendorName = scanner.nextLine().trim();

        ArrayList<Transaction> matchingTransactions =
                new ArrayList<>();

        ArrayList<Transaction> allTransactions =
                TransactionRecords.getAllTransactions();

        for (Transaction transaction : allTransactions) {

            if (transaction.getVendor().toLowerCase()
                    .contains(vendorName.toLowerCase())) {

                matchingTransactions.add(transaction);
            }
        }

        TransactionRecords.displayTransactions(matchingTransactions);
    }

    // Bonus: Search using different transaction properties
    public static void customSearch(Scanner scanner) {

        System.out.println("\n========== CUSTOM SEARCH ==========");
        System.out.println("Leave any field blank to skip it.");

        System.out.print("Start Date (yyyy-MM-dd): ");
        String startInput = scanner.nextLine().trim();

        System.out.print("End Date (yyyy-MM-dd): ");
        String endInput = scanner.nextLine().trim();

        System.out.print("Description: ");
        String descriptionInput = scanner.nextLine().trim();

        System.out.print("Enter customer or vendor name: ");
        String vendorInput = scanner.nextLine().trim();

        System.out.print("Amount (negative for expenses): ");
        String amountInput = scanner.nextLine().trim();

        try {

            LocalDate startDate = null;
            LocalDate endDate = null;
            Double searchAmount = null;

            // Convert the start date if entered
            if (!startInput.isEmpty()) {
                startDate = LocalDate.parse(startInput);
            }

            // Convert the end date if entered
            if (!endInput.isEmpty()) {
                endDate = LocalDate.parse(endInput);
            }

            // Convert the amount if entered
            if (!amountInput.isEmpty()) {

                searchAmount = Double.parseDouble(amountInput);

                if (!Double.isFinite(searchAmount)) {

                    System.out.println(
                            "Please enter a valid amount."
                    );

                    return;
                }
            }

            // Check that the date range is valid
            if (startDate != null && endDate != null &&
                    startDate.isAfter(endDate)) {

                System.out.println(
                        "Start date cannot be after end date."
                );

                return;
            }

            ArrayList<Transaction> matchingTransactions =
                    new ArrayList<>();

            ArrayList<Transaction> allTransactions =
                    TransactionRecords.getAllTransactions();

            // Check each transaction against the search filters
            for (Transaction transaction : allTransactions) {

                // Check start date
                if (startDate != null &&
                        transaction.getDate().isBefore(startDate)) {

                    continue;
                }

                // Check end date
                if (endDate != null &&
                        transaction.getDate().isAfter(endDate)) {

                    continue;
                }

                // Check description
                if (!descriptionInput.isEmpty() &&
                        !transaction.getDescription().toLowerCase()
                                .contains(descriptionInput.toLowerCase())) {

                    continue;
                }

                // Check vendor
                if (!vendorInput.isEmpty() &&
                        !transaction.getVendor().toLowerCase()
                                .contains(vendorInput.toLowerCase())) {

                    continue;
                }

                // Check amount
                if (searchAmount != null &&
                        Double.compare(
                                transaction.getAmount(),
                                searchAmount
                        ) != 0) {

                    continue;
                }

                // Save the matching transaction
                matchingTransactions.add(transaction);
            }


            TransactionRecords.displayTransactions(matchingTransactions);

        } catch (RuntimeException e) {

            System.out.println(
                    "Invalid search information. Please try again."
            );
        }
    }
}
