import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;

public class FileHandler {

    /**
     * Standard formatter for startDate and endDate
     */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    /**
     * Saves Subscription and Budget Information into a file
     *
     * @param manager to access all methods with subscription data
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
}

