import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileHandler {

    public static void saveToCSV(SubscriptionManager manager, String filename) {
        File file = new File(filename);

        // Write lines to file
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) { // create/open file for writing

            //Subscriptions
            writer.println("SUBSCRIPTIONS");
            writer.println("name,cost,category,renewal,startDate,endDate,status");

            // Loop through all subscriptions in manager to add to a line
            // endDate should be either N/A or endDate if provided - add ternary operator perhaps
            // Write a CSV line, with name + , + cost + .... writer.println ( add the line to the file)

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
