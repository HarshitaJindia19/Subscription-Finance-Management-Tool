import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class FileHandler {

    /**
     * Standard formatter for startDate and endDate
     */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    /**
     * Saves Subscription and Budget Information into a file
     *
     * @param manager  to access all methods with subscription data
     * @param filename to save to
     */
    public static void saveToCSV(SubscriptionManager manager, String filename) {
        File file = new File(filename);

        // Write lines to file
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) { // create/open file for writing

            // Write SUBSCRIPTIONS
            writer.println("SUBSCRIPTIONS");
            writer.println("name,cost,category,renewal,startDate,endDate,status");

            // Loop through current logged subscriptions
            for (Subscription sub : manager.getAllSubscriptions()) {

                // Handle null endDate - N/A if skipped
                String endDate = (sub.getEndDate() != null) ? sub.getEndDate().format(formatter) : "N/A";

                // Add subscription information into a CSV line
                // name,cost,category,renewal,startDate,endDate,status
                writer.println(
                        sub.getName() + "," +
                        sub.getCost() + "," +
                        sub.getCategory() + "," +
                        sub.getRenewal() + "," +
                        sub.getStartDate().format(formatter) + "," +
                        endDate + "," +
                        sub.getStatus().toString()
                );
            }

            // Write BUDGETS
            writer.println("BUDGETS");
            writer.println("category,limit");

            // Loop through current logged budgets
            for (Budget budget : manager.getAllBudgets()) {

                // Add budget information to a CSV line
                // category,limit
                writer.println(
                        budget.getCategory() + "," +
                        budget.getLimit()
                );
            }

            // Output message for user for successful save to file
            System.out.println("Data saved successfully to: " + filename);
            System.out.println("Subscriptions Saved: " + manager.getAllSubscriptions().size());
            System.out.println("Budgets Saved: " + manager.getAllBudgets().size());

        } catch (IOException e) {
            System.out.println("ERROR saving to file: " + filename);
            System.out.println("Details: " + e);
        }
    }

    public static void loadFromCSV(SubscriptionManager manager, String filename) {
        File file = new File(filename);

        try (Scanner scanner = new Scanner(file)) {

            manager.getAllSubscriptions().clear();
            manager.getAllBudgets().clear();

            boolean readingSubscriptions = false;
            boolean readingBudgets = false;

            while (scanner.hasNextLine()) {

                String line = scanner.nextLine();
                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                if (line.equals("SUBSCRIPTIONS")) {
                    readingSubscriptions = true;
                    readingBudgets = false;

                    if (scanner.hasNextLine()) {
                        scanner.nextLine();
                    }
                    continue;
                }

                if (line.equals("BUDGETS")) {
                    readingSubscriptions = false;
                    readingBudgets = true;

                    if (scanner.hasNextLine()) {
                        scanner.nextLine();
                    }
                    continue;
                }

                String[] parts = line.split(",");

                if (readingSubscriptions) {
                    if (parts.length < 7) {
                        continue;
                    }

                    String name = parts[0];
                    double cost = Double.parseDouble(parts[1]);
                    String category = parts[2];
                    String renewal = parts[3];

                    LocalDate startDate = LocalDate.parse(parts[4], formatter);

                    LocalDate endDate;
                    if (parts[5].equals("N/A")) {
                        endDate = null;
                    } else {
                        endDate = LocalDate.parse(parts[5], formatter);
                    }

                    String statusText = parts[6];
                    Status status = Status.valueOf(statusText.toUpperCase());

                    manager.storeNewSubscription(
                            name,
                            cost,
                            category,
                            renewal,
                            startDate,
                            endDate,
                            status
                    );
                }

                if (readingBudgets) {
                    if (parts.length < 2) {
                        continue;
                    }

                    String category = parts[0];
                    double limit = Double.parseDouble(parts[1]);
                    manager.getAllBudgets().add(new Budget(category, limit));
                }
            }
            System.out.println("Data loaded successfully from: " + filename);

        } catch (IOException e) {
            System.out.println("ERROR loading file: " + filename);
            System.out.println(e.getMessage());
        }
    }
}