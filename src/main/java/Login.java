




import java.util.Scanner;

public class Login {

    public static boolean checkPassword(Scanner scanner) {

        String correctUsername = "mahleta";
        String correctPassword = "1234*";
        boolean isLoggedIn = false;
        System.out.println("\n ‼\uFE0F OWNER LOGIN ‼\uFE0F");


        while (!isLoggedIn) {

            System.out.print("Username: ");
            String username = scanner.nextLine().trim();
            System.out.print("Password: ");
            String password = scanner.nextLine();

            if (username.equals(correctUsername) &&
                    password.equals(correctPassword)) {

                System.out.println("Login successful! Welcome back, Mahisha! \uD83E\uDDD1\uD83C\uDFFD\u200D\uD83C\uDF73");
                isLoggedIn = true;

            } else {

                System.out.println("Incorrect username or password. Please try again.\n");
            }
        }

        return isLoggedIn;
    }
}
