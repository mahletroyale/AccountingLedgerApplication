
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

            System.out.print("Choose a report: ");
            String reportChoice = scanner.nextLine().trim();

            LocalDate currentDate = LocalDate.now();

            int currentYear = currentDate.getYear();
            int currentMonth = currentDate.getMonthValue();

            switch (reportChoice) {

                case "1":
                    displayMonthlyReport(
                            currentYear,
                            currentMonth,
                            true
                    );
                    break;

                case "2":
                    int previousMonthNumber = currentMonth - 1;
                    int previousMonthYear = currentYear;

                    if (previousMonthNumber == 0) {
                        previousMonthNumber = 12;
                        previousMonthYear--;
                    }

                    displayMonthlyReport(
                            previousMonthYear,
                            previousMonthNumber,
                            false
                    );
                    break;

                case "3":
                    displayYearlyReport(currentYear, true);
                    break;

                case "4":
                    displayYearlyReport(currentYear - 1, false);
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
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // Display transactions for a selected month
    public static void displayMonthlyReport(
            int reportYear, int reportMonth, boolean isMonthToDate) {

        ArrayList<Transaction> matchingTransactions =
                new ArrayList<Transaction>();

        ArrayList<Transaction> allTransactions =
                TransactionRecords.getAllTransactions();

        LocalDate currentDate = LocalDate.now();

        for (Transaction currentTransaction : allTransactions) {

            LocalDate transactionDate = currentTransaction.getDate();

            if (transactionDate.getYear() == reportYear &&
                    transactionDate.getMonthValue() == reportMonth) {

                if (!isMonthToDate ||
                        convertDateToNumber(transactionDate) <=
                                convertDateToNumber(currentDate)) {

                    matchingTransactions.add(currentTransaction);
                }
            }
        }

        System.out.println(
                "\nTransactions for " + reportMonth + "/" + reportYear
        );

        TransactionRecords.displayTransactions(matchingTransactions);
    }

    // Display transactions for a selected year
    public static void displayYearlyReport(
            int reportYear, boolean isYearToDate) {

        ArrayList<Transaction> matchingTransactions =
                new ArrayList<Transaction>();

        ArrayList<Transaction> allTransactions =
                TransactionRecords.getAllTransactions();

        LocalDate currentDate = LocalDate.now();

        for (Transaction currentTransaction : allTransactions) {

            LocalDate transactionDate = currentTransaction.getDate();

            if (transactionDate.getYear() == reportYear) {

                if (!isYearToDate ||
                        convertDateToNumber(transactionDate) <=
                                convertDateToNumber(currentDate)) {

                    matchingTransactions.add(currentTransaction);
                }
            }
        }

        System.out.println("\nTransactions for " + reportYear);

        TransactionRecords.displayTransactions(matchingTransactions);
    }

    // Search transactions by customer or vendor
    public static void searchByVendor(Scanner scanner) {

        System.out.print("Enter customer or vendor name: ");
        String vendorToSearch =
                scanner.nextLine().trim().toLowerCase();

        ArrayList<Transaction> matchingTransactions =
                new ArrayList<Transaction>();

        ArrayList<Transaction> allTransactions =
                TransactionRecords.getAllTransactions();

        for (Transaction currentTransaction : allTransactions) {

            String transactionVendor =
                    currentTransaction.getVendor().toLowerCase();

            if (transactionVendor.indexOf(vendorToSearch) != -1) {

                matchingTransactions.add(currentTransaction);
            }
        }

        TransactionRecords.displayTransactions(matchingTransactions);
    }

    public static void customSearch(Scanner scanner) {

        System.out.println("\n========== CUSTOM SEARCH ==========");
        System.out.println("Leave any field blank to skip it.");

        System.out.print("Start Date (yyyy-MM-dd): ");
        String startDateInput = scanner.nextLine().trim();

        System.out.print("End Date (yyyy-MM-dd): ");
        String endDateInput = scanner.nextLine().trim();

        System.out.print("Description: ");
        String descriptionToSearch =
                scanner.nextLine().trim().toLowerCase();

        System.out.print("Vendor: ");
        String vendorToSearch =
                scanner.nextLine().trim().toLowerCase();

        System.out.print("Amount (negative for expenses): ");
        String amountInput = scanner.nextLine().trim();

        LocalDate startDate = null;
        LocalDate endDate = null;

        double amountToSearch = 0;
        boolean searchByAmount = false;

        try {

            // Convert the start date
            if (!startDateInput.isEmpty()) {
                startDate = LocalDate.parse(startDateInput);
            }

            // Convert the end date
            if (!endDateInput.isEmpty()) {
                endDate = LocalDate.parse(endDateInput);
            }

            // Convert the amount
            if (!amountInput.isEmpty()) {

                amountToSearch = Double.parseDouble(amountInput);
                searchByAmount = true;

                if (amountToSearch > 999999999.99 ||
                        amountToSearch < -999999999.99 ||
                        amountToSearch != amountToSearch) {

                    System.out.println("Invalid search amount.");
                    return;
                }
            }

        } catch (Exception error) {

            System.out.println(
                    "Enter dates as yyyy-MM-dd and amount as a number."
            );
            return;
        }

        // Checks that the start date is not after the end date
        if (startDate != null && endDate != null &&
                convertDateToNumber(startDate) >
                        convertDateToNumber(endDate)) {

            System.out.println(
                    "Start date cannot be after end date."
            );
            return;
        }

        ArrayList<Transaction> matchingTransactions =
                new ArrayList<Transaction>();

        ArrayList<Transaction> allTransactions =
                TransactionRecords.getAllTransactions();

        // Check each transaction against the search filters
        for (Transaction currentTransaction : allTransactions) {

            LocalDate transactionDate = currentTransaction.getDate();

            // Check the start date
            if (startDate != null &&
                    convertDateToNumber(transactionDate) <
                            convertDateToNumber(startDate)) {

                continue;
            }

            // Checks the end date
            if (endDate != null &&
                    convertDateToNumber(transactionDate) >
                            convertDateToNumber(endDate)) {

                continue;
            }

            // Checks the description
            String transactionDescription =
                    currentTransaction.getDescription().toLowerCase();

            if (!descriptionToSearch.isEmpty() &&
                    transactionDescription.indexOf(descriptionToSearch) == -1) {

                continue;
            }

            // Checks the vendor
            String transactionVendor =
                    currentTransaction.getVendor().toLowerCase();

            if (!vendorToSearch.isEmpty() &&
                    transactionVendor.indexOf(vendorToSearch) == -1) {

                continue;
            }

            // Checks the amount
            if (searchByAmount &&
                    currentTransaction.getAmount() != amountToSearch) {

                continue;
            }

            // Add a transaction if it matches all selected filters
            matchingTransactions.add(currentTransaction);
        }

        TransactionRecords.displayTransactions(matchingTransactions);
    }

    // Convert a date into a number for easier comparison
    public static int convertDateToNumber(LocalDate transactionDate) {

        return transactionDate.getYear() * 10000 +
                transactionDate.getMonthValue() * 100 +
                transactionDate.getDayOfMonth();
    }
}
