import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class LogAnalyzer {
    public static void main(String[] args) {

        HashMap<String, Integer> ipFailures = new HashMap<>();
        int blockThreshold = 5;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try {
            File file = new File("logins.txt"); 
            Scanner reader = new Scanner(file);

            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] parts = line.split(",");

                LocalDateTime time = LocalDateTime.parse(parts[0], formatter);
                String ip = parts[1];
                String username = parts[2];
                String password = parts[3];

                if (!(username.equals("admin") && password.equals("1234"))) {
                    ipFailures.put(ip, ipFailures.getOrDefault(ip, 0) + 1);
                }
            }

            reader.close();

            FileWriter blockWriter = new FileWriter("blocked_ips.txt");

            System.out.println("=== Firewall Auto-Block Simulation ===");

            for (String ip : ipFailures.keySet()) {
                int count = ipFailures.get(ip);

                System.out.println(ip + " → " + count + " failed attempts");

                if (count >= blockThreshold) {
                    System.out.println("🚫 BLOCKED: " + ip + " exceeded failed login threshold.");
                    blockWriter.write(ip + "\n");
                }
            }

            blockWriter.close();

            System.out.println("\nBlocked IP list saved to blocked_ips.txt");

        } catch (Exception e) {
            System.out.println("Error processing logs.");
        }
    }
}



