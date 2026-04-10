package ca.ucalgary.cpsc233w26project1;

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

    /**
     * Loads Subscription and Budget information from a file
     *
     * @param manager  to store loaded subscription and budget data
     * @param filename to read from
     */

    public static void loadFromCSV(SubscriptionManager manager, String filename) {
        //Create File object using provided filename
        File file = new File(filename);

        try (Scanner scanner = new Scanner(file)) {

            //Clear the existing data before loading the new data from file
            manager.getAllSubscriptions().clear();
            manager.getAllBudgets().clear();

            //Track the section of file we are reading
            boolean readingSubscriptions = false;
            boolean readingBudgets = false;

            //Read file line by line
            while (scanner.hasNextLine()) {

                String line = scanner.nextLine();
                line = line.trim(); //removes any extra space

                //Skip empty lines
                if (line.isEmpty()) {
                    continue;
                }

                //Check if we reached the SUBSCRIPTIONS section
                if (line.equals("SUBSCRIPTIONS")) {
                    readingSubscriptions = true;
                    readingBudgets = false;

                    //Skip the header line after section title
                    if (scanner.hasNextLine()) {
                        scanner.nextLine();
                    }
                    continue;
                }

                //Check if reached BUDGETS section
                if (line.equals("BUDGETS")) {
                    readingSubscriptions = false;
                    readingBudgets = true;

                    //Skip header line after the section title
                    if (scanner.hasNextLine()) {
                        scanner.nextLine();
                    }
                    continue;
                }

                //Split line into parts
                String[] parts = line.split(",");

                //Skip the invalid lines with missing data
                if (readingSubscriptions) {
                    if (parts.length < 7) {
                        continue;
                    }

                    //Extract sub fields
                    String name = parts[0];
                    double cost = Double.parseDouble(parts[1]);
                    String category = parts[2];
                    String renewal = parts[3];

                    LocalDate startDate = LocalDate.parse(parts[4], formatter);

                    //Handle Optional end date
                    LocalDate endDate;
                    if (parts[5].equals("N/A")) {
                        endDate = null;
                    } else {
                        endDate = LocalDate.parse(parts[5], formatter);
                    }

                    //Covert status test back into enum
                    String statusText = parts[6];
                    Status status = Status.valueOf(statusText.toUpperCase());

                    //Store subs in manager
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
                //Skip invalid line
                if (readingBudgets) {
                    if (parts.length < 2) {
                        continue;
                    }

                    //Extract budget fields
                    String category = parts[0];
                    double limit = Double.parseDouble(parts[1]);
                    //Add budget to manager
                    manager.getAllBudgets().add(new Budget(category, limit));
                }
            }
            //Print message after loading completes
            System.out.println("Data loaded successfully from: " + filename);

        } catch (IOException e) {
            //Handles file reading errors
            System.out.println("ERROR loading file: " + filename);
            System.out.println(e.getMessage());
        }
    }
}