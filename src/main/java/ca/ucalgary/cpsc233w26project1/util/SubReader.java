package ca.ucalgary.cpsc233w26project1.util;


import ca.ucalgary.cpsc233w26project1.Budget;
import ca.ucalgary.cpsc233w26project1.Status;
import ca.ucalgary.cpsc233w26project1.SubscriptionManager;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Reads a file, and loads the information for usage
 */
public class SubReader {

    /**
     * Standard formatter for startDate and endDate
     */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    /**
     * Loads Subscription and Budget Information from a file
     *
     * @param manager to access all methods with subscription data
     * @param file to save to
     */
    public static void loadFromFile(SubscriptionManager manager, File file) {

        try (Scanner scanner = new Scanner(file)) {

            //Clear the existing data before loading the new data from file
            manager.getAllSubscriptions().clear();
            manager.getAllBudgets().clear();

            //Track the section of file we are reading
            boolean readingSubscriptions = false;
            boolean readingBudgets = false;

            //Read file line by line
            while (scanner.hasNextLine()) {

                String line = scanner.nextLine().trim();

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

                    //Extract subfields
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
            System.out.println("Data loaded successfully from: " + file);

        } catch (IOException e) {
            //Handles file reading errors
            System.out.println("ERROR loading file: " + file);
            System.out.println(e.getMessage());
        }
    }
}

