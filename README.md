# Mahisha Made It 👩🏽‍🍳

**You Crave It. I Make It.**

A Java accounting ledger for my small catering business in the near future! I built this project around a food business because I wanted the transactions to feel like real day-to-day decisions: getting paid for a catering order, picking up ingredients, or buying takeout containers.

It's a console application, so everything happens through a menu in the terminal. Transactions are kept in a CSV file.

## What you can do

- **Record food sales.** Enter a description, customer name, and amount. Sales are saved as positive numbers.
- **Track kitchen expenses.** Enter a description, vendor name, and amount. Expenses are saved as negative numbers.
- **Look back at transactions.** View the full ledger (newest first), just deposits, or just payments.
- **Check the business Kitchen Summary.** The *Kitchen Summary* adds up income and expenses and shows the net balance.
- **Pull reports.** View month-to-date, previous-month, year-to-date, and previous-year transactions.
- **Find a specific entry.** Search by vendor, or use the custom search to filter by dates, description, customer, vendor, and amount.
- **Binary Chef Welcome Screen.** I wanted the application to have a personal touch, so I created a chef illustration using only 0s and 1s. Instead of using an image file, I stored the artwork in a String array and used a for loop to print each row in the console. The illustration appears alongside my business name and tagline.

Sales and expenses have separate **Customer** and **Vendor** columns. The one that doesn't apply is recorded as `N/A` for new entries. Each entry also gets the current date and time automatically.

## Running it in IntelliJ

1. Open the `AccountingLedgerApplication` folder in IntelliJ IDEA.
2. Set the project JDK to match the Java version configured in `pom.xml` (currently **Java 27**).
3. Open `src/main/java/LedgerHome.java`.
4. Run the `main()` method in `LedgerHome` — **not** the default `org.example.Main` file.
5. Enter the demonstration password: `1234*`.

Make sure the **working directory** in your Run Configuration is the project's root folder (`AccountingLedgerApplication`). The app uses a relative path to find its CSV file.

### Menus at a glance

```
HOME
D  Add Deposit (Food Sale)
P  Make Payment (Kitchen Expense)
L  View Ledger
X  Exit

KITCHEN LEDGER
A  All Transactions
D  Deposits / Food Sales
P  Payments / Kitchen Expenses
S  Financial Summary
R  Reports
H  Home

REPORTS
1  Month To Date
2  Previous Month
3  Year To Date
4  Previous Year
5  Search by Vendor
6  Custom Search
0  Back to Ledger
```

## Where the transactions go

The app currently reads from and appends to:

```
src/main/java/Transaction.csv
```

The format is **pipe-delimited** (the `|` character separates columns):

```
date|time|description|customer|vendor|amount
2026-11-05|14:16:23|Cupcake|Maaike|N/A|30.0
2026-11-05|14:16:40|Cutlery|N/A|Costco|-40.0
```

A positive amount means money coming in; a negative amount means money going out. The `TransactionRecords` class uses `FileWriter` in append mode so adding a new transaction doesn't erase previous ones.

**CSV note:** There are also older files named `transaction.csv` and `transactions.csv` in the project root. Those are *not* the files the current app uses. Some older example rows in `src/main/java/Transaction.csv` still have five fields; the current reader expects six and will skip those rows until they are updated.

## How the code is organized

All of the main application classes are in `src/main/java/`.

| Class | Responsibility |
| --- | --- |
| LedgerHome | Starts the app and handles the main menu. |
| `Login` | Checks the demonstration password before opening the ledger. |
| `ChefArt` | Creates and displays a binary chef illustration using a String array and an enhanced for loop. |
| `Transaction` | Holds one transaction's date, time, description, customer, vendor, and amount. |
| `TransactionEntry` | Collects and checks user input, then creates a transaction. |
| `TransactionRecords` | Saves to CSV, reads records back, sorts them, and prints the ledger table. |
| `LedgerMenu` | Handles the ledger's viewing and filtering options. |
| `FinancialSummary` | Calculates total food sales, expenses, and net balance. |
| `ReportsMenu` | Handles date-based reports and transaction searches. |

## A few decisions behind the project

I used `ArrayList<Transaction>` to work with transactions after reading them from the file, and `LocalDate` / `LocalTime` to store timestamps. The ledger compares dates first and times second so the newest entries show up at the top.

I also added input checks. Descriptions and names must contain letters, numbers, or spaces, and each transaction must be greater than $0 and no more than **$5,000**. These rules help catch mistakes before an entry is written to the CSV.

One of the trickier parts was getting the file location right. The name `Transaction.csv` looks almost identical to the other CSV files in the project, but the folder and capitalization matter. Keeping the file path in one constant in `TransactionRecords` makes that easier to manage.

## Challenges, Risks, and Lessons Learned

Runtime and efficiency

One area I would improve is how transactions are sorted. I used nested for loops to organize transactions from newest to oldest. While this works for a small number of records, the sorting algorithm has a time complexity of O(n²) because each transaction is compared with multiple other transactions. As the number of transactions grows, sorting becomes less efficient.

Reading the CSV file and searching through transactions both take O(n) time because the program goes through the records one at a time. Since this is a small business application, that approach works for now. However, I could make the sorting process more efficient by using Java's built-in sorting methods, which can achieve O(n log n) time complexity. I could also avoid repeatedly reading the entire CSV file whenever the user selects a different menu option.

Data storage and formatting

Another challenge was managing the CSV file. At one point, transactions were being saved to a different file than the one I was viewing in IntelliJ. I learned how important file paths and consistent file names are when working with file input and output.

I also noticed that using N/A for missing customer or vendor information doesn't look the prettiest in the transaction table. Although it makes it clear that a field doesn't apply, the repeated N/A values make the ledger look somewhat cluttered. In the future, I would consider displaying a dash (-) instead while keeping the underlying data organized. I would also like to improve the table's spacing and alignment to make it easier to read.

Risks and future improvements

Because transactions are stored in a CSV file, records could be accidentally edited, deleted, or formatted incorrectly. The application also uses a hardcoded password, which is fine for demonstrating the project but wouldn't be secure for an actual business. If I continued developing this application, I would explore stronger authentication, better error handling, and a database for storing transactions more reliably.

What I learned

My biggest takeaway is that code can be functional without necessarily being the most efficient or user-friendly solution. This project helped me become more comfortable working with classes, methods, ArrayLists, loops, conditionals, and file handling. It also pushed me to think beyond just meeting the project requirements. I started considering how someone else would interact with the application, what could go wrong, and how I could make the overall experience better.

One creative part of this project was building the binary chef illustration for my welcome screen. I originally wanted something that reflected my cooking business without using an actual image. By arranging 0s and 1s inside a String array, I was able to create artwork directly in the console. It was also a good reminder that even something as simple as spacing can make a big difference in how the final output looks. I enjoyed finding a way to apply basic Java concepts to something visual and personal.
