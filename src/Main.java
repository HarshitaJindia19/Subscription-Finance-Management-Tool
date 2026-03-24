import java.io.File;

public class Main {

    public static void main(String[] args) {
        SubscriptionManager manager = new SubscriptionManager();

        if (args.length > 0) {
            String filename = args[0];

            File file = new File(filename);
            if (file.exists()) {
                System.out.println("Loading data from file: " + filename);
                FileHandler.loadFromCSV(manager, filename);
            } else {
                System.out.println("File not found: " + filename);
            }
        } else {
            System.out.println("No file provided. Starting with empty data.");
        }
        Menu.runMenu(manager);
    }
}