package ca.ucalgary.cpsc233w26project1.util;

import ca.ucalgary.cpsc233w26project1.Budget;
import ca.ucalgary.cpsc233w26project1.Subscription;
import ca.ucalgary.cpsc233w26project1.SubscriptionManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;

/**
 * Takes current logged subscription/budget information and writes it to a file
 */
public class SubWriter {

    /**
     * Standard formatter for startDate and endDate
     */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    /**
     * Saves Subscription and Budget Information into a file
     *
     * @param manager to access all methods with subscription data
     * @param file to save to
     */
    public static void writeToFile(SubscriptionManager manager, File file) {

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

            writer.println("BUDGETS");
            writer.println("category,limit");

            // Loop through current logged budgets
            for (Budget budget : manager.getAllBudgets()) {
                writer.println(budget.getCategory() + "," + budget.getLimit());
            }

            System.out.println("✅ Saved to: " + file.getName());

        } catch (IOException e) {
            System.out.println("Error saving to file: " + file.getName());
        }
    }
}
