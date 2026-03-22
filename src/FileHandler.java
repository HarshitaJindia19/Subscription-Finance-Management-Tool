import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;

public class FileHandler {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

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
                        sub.getName() + "," +                        //Netflix
                        sub.getCost() + "," +                        //13.99
                        sub.getCategory() + "," +                    //Entertainment
                        sub.getRenewal() + "," +                     //Monthly
                        sub.getStartDate().format(formatter) + "," + //05/06/2025
                        endDate + "," +                              //N/A
                        sub.getStatus().toString()                   //ACTIVE
                );
            }

            //Budgets
            writer.println("BUDGETS");
            writer.println("category,limit");

            //Loop through all budgets and write a line for each


            // Output successful message that data has been saved -> or should that be inside menu method
            // Don't forget -> add a menu option to save to file

        } catch (IOException e) { // NOTE -> add more specific err messages (multiple for better clarity)
            System.out.println("Error saving to file: " + e);
        }
    }
}

// Point to remember -> if the user save to file twice, does previous info repeat in the file?
