import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileHandler {

    public static void saveToCSV(SubscriptionManager manager, String filename) {
        File file = new File(filename);
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {

        } catch (IOException e) {
            System.out.println("Error saving to file: " + e);
        }
    }
}
