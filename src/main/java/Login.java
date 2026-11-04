


import java.util.Scanner;

public class Login {

    public static boolean checkPassword(Scanner scanner) {

        String accessCode = "1234*";

        System.out.println("\n OWNER LOGIN \uD83D\uDC69\uD83C\uDFFD\u200D\uD83C\uDF73");
        System.out.print("Enter password: ");
        String enteredPassword = scanner.nextLine();

        if (enteredPassword.equals(accessCode)) {

            System.out.println("\nLogin successful!");
            System.out.println("Welcome back, Chef Mahisha!");

            return true;

        } else {

            System.out.println("Incorrect password. Access denied.");

            return false;
        }
    }
}
