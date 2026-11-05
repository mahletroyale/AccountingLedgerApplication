
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class ReportsMenu {

    // Display the Reports Menu
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

            switch (reportChoice) {

                case "1":
                    // Current month through today
                    displayDateReport(
                            currentDate.withDayOfMonth(1),
                            currentDate
                    );
                    break;

                case "2":
                    // First and last day of the previous month
                    LocalDate previousMonth = currentDate.minusMonths(1);

                    displayDateReport(
                            previousMonth.withDayOfMonth(1),
                            previousMonth.withDayOfMonth(
                                    previousMonth.lengthOfMonth()
                            )
                    );
                    break;

                case "3":
                    // Start of the current year through today
                    displayDateReport(LocalDate.of(currentDate.getYear(), 1, 1), currentDate);
                    break;

                case "4":
                    // Entire previous year
                    displayDateReport(LocalDate.of(currentDate.getYear() - 1, 1, 1),
                            LocalDate.of(currentDate.getYear() - 1, 12, 31)
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
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // Display transactions within a selected date range
    public static void displayDateReport(
            LocalDate startDate, LocalDate endDate) {

        ArrayList<Transaction> matchingTransactions =
                new ArrayList<>();

        ArrayList<Transaction> allTransactions =
                TransactionRecords.getAllTransactions();

        for (Transaction currentTransaction : allTransactions) {

            LocalDate transactionDate = currentTransaction.getDate();

            // Check whether the date is within the selected range
            if (!transactionDate.isBefore(startDate) && !transactionDate.isAfter(endDate)) {
                matchingTransactions.add(currentTransaction);
            }
        }

        System.out.println("\nTransactions from " + startDate + " to " + endDate);

        TransactionRecords.displayTransactions(matchingTransactions);
    }

    // Search transactions by vendor
    public static void searchByVendor(Scanner scanner) {

        System.out.print("Enter vendor name: ");
        String vendorToSearch =
                scanner.nextLine().trim().toLowerCase();

        if (vendorToSearch.isEmpty()) {
            System.out.println("Please enter a vendor name.");
            return;
        }

        ArrayList<Transaction> matchingTransactions =
                new ArrayList<>();

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

        System.out.print("Customer: ");
        String customerToSearch =
                scanner.nextLine().trim().toLowerCase();

        System.out.print("Vendor: ");
        String vendorToSearch =
                scanner.nextLine().trim().toLowerCase();

        System.out.print("Amount (negative for expenses): ");
        String amountInput = scanner.nextLine().trim();

        LocalDate startDate = null;
        LocalDate endDate = null;
        double amountToSearch = 0;

        try {

            // Convert the start date if entered
            if (!startDateInput.isEmpty()) {
                startDate = LocalDate.parse(startDateInput);
            }

            // Convert the end date if entered
            if (!endDateInput.isEmpty()) {
                endDate = LocalDate.parse(endDateInput);
            }

            // Convert the amount if entered
            if (!amountInput.isEmpty()) {

                amountToSearch = Double.parseDouble(amountInput);

                if (!Double.isFinite(amountToSearch) ||
                        amountToSearch > 5000.00 ||
                        amountToSearch < -5000.00) {

                    System.out.println("Invalid search amount.");
                    return;
                }
            }

        } catch (Exception error) {

            System.out.println(
                    "Invalid date or amount. Please try again."
            );
            return;
        }

        // Check that the start date comes before the end date
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

        // Checks each transaction against the search filters
        for (Transaction currentTransaction : allTransactions) {

            LocalDate transactionDate =
                    currentTransaction.getDate();

            // Checks the start date
            if (startDate != null &&
                    transactionDate.isBefore(startDate)) {

                continue;
            }

            // Check end date
            if (endDate != null &&
                    transactionDate.isAfter(endDate)) {

                continue;
            }

            // Check description
            String transactionDescription =
                    currentTransaction.getDescription().toLowerCase();

            if (!descriptionToSearch.isEmpty() &&
                    transactionDescription.indexOf(descriptionToSearch) == -1) {

                continue;
            }

            // Check customer
            String transactionCustomer = currentTransaction.getCustomer().toLowerCase();

            if (!customerToSearch.isEmpty() && transactionCustomer.indexOf(customerToSearch) == -1) {
                continue;
            }

            // Check vendor
            String transactionVendor = currentTransaction.getVendor().toLowerCase();

            if (!vendorToSearch.isEmpty() && transactionVendor.indexOf(vendorToSearch) == -1) {
                continue;
            }

            // Check amount only if the user entered one
            if (!amountInput.isEmpty() && currentTransaction.getAmount() != amountToSearch) {
                continue;
            }

            // Add the transaction if it matches the filters
            matchingTransactions.add(currentTransaction);
        }

        // Display matching transactions
        TransactionRecords.displayTransactions(matchingTransactions);
    }
}
