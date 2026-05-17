import java.util.Scanner;

public class LoginChecker {
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        int attempts = 0;
        int maxAttempts = 3;

        while (attempts < maxAttempts) {

            System.out.print("Enter username: ");
            String username = input.nextLine();

            System.out.print("Enter password: ");
            String password = input.nextLine();

            if (username.equals("admin") && password.equals("1234")) {
                System.out.println("Access Granted");
                break;
            } else {
                attempts++;
                System.out.println("Access Denied");

                if (attempts == maxAttempts) {
                    System.out.println("🚨 ALERT: Possible Brute-Force Attack Detected!"); 
                } else {
                    System.out.println("Attempts left: " + (maxAttempts - attempts));
                }
            }
        }

        input.close(); 
    }
}
