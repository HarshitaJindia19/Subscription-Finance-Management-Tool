import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * Menu.java - handles the main menu loop and user interface
 * @author Ramsha Oad
 * @date Feb 24 2026
 * @tutorial T01
 *
 */
public class Menu {
    private static final Scanner input = new Scanner(System.in);
    //Arraylist stores all menu options - index 0=exit, index 1=add subscription, etc.
    public static final ArrayList<String> options = new ArrayList<>();

    //static runs once when class is first loaded - used here because options is a static variable
    static {
        options.add("Exit");
        options.add("Add subscription");
        options.add("Update subscription");
        options.add("View all subscriptions");
        options.add("View active subscriptions");
        options.add("Total monthly spend");
        options.add("Total annual spend");
        options.add("Upcoming bills");
        options.add("Paused subscriptions");
        options.add("Budget % by category");
        options.add("High-expense bills");
        options.add("Budget alert");
        options.add("Search subscriptions");
        options.add("Add category budget");
    }

    /**
     * main loop for program, keeps running until user exits
     * shows menu, reads user input, calls the corresponding method
     */
    public static void runMenu() {
        Data.loadTestData();
        boolean active = true; //loop keeps going
        while (active) {
            System.out.println("\n====SUBSCRIPTION MANAGER MENU OPTIONS====");

            //loop through menu options ArrayList and print each option with number in front
            for (int i = 0; i < options.size(); i++) {
                System.out.println(i + ". " + options.get(i));
            }
            System.out.println("Please select an option: ");
            String choice = input.nextLine().trim();

            if (choice.isEmpty()) {
                continue;
            }


            switch (choice) { //switch to guide user to correct action based on choice
                case "1":
                    // Harshita
                    addNewSubscription();
                    break;
                case "2":
                    updateProcess();//update subscription
                    break;
                case "3":
                    //View all subscriptions
                    showAllSubscriptions();
                    break;
                case "4":
                    // View only ACTIVE status subscriptions
                    showActiveSubscriptions();
                    break;
                case "5":
                    // Harshita
                    showMonthlySpend();
                    break;
                case "6":
                    // Harshita
                    showYearlySpend();
                    break;
                case "7":
                    upcomingBills();
                    break;
                case "8":
                    // Paused subscriptions
                    showPausedSubscriptions();
                    break;
                case "9":
                    // Budget % used by category
                    showCategoryBudgetUsage();
                    break;
                case "10":
                    // High-expense bills
                    showHighExpenseSubscriptions();
                    break;
                case "11":
                    // Budget alert
                    showBudgetAlerts();
                    break;
                case "12":
                    //Search subscriptions by name or category
                    showSearchResults();
                    break;
                case "13":
                    addCategoryBudget();
                    break;
                case "0":
                    active = false;//now active is false to exit while loop
                    System.out.println("Exited.");
                    break;
                default:
                    System.out.println("Invalid choice, please enter a number from the menu.");
            }
            //press enter to continue
            if (!choice.equals("0")) {
                System.out.println("\nPress enter to continue.");
                input.nextLine();
            }
        }
        input.close();//close scanner when program ends
    }

    //==================================MENU FUNCTIONS==================================//

    /**
     * Adds/stores new subscription with information
     */
    private static void addNewSubscription() {
        System.out.println("\n==== ADD NEW SUBSCRIPTION ====");

        // Get the name first to check if it already exists
        String name = getName();

        if (Data.subscriptionExists(name)) {
            System.out.println("ERROR: A subscription with the name '" + name + "' already exists.");
            System.out.println("Please use the 'Update' option if you wish to change it.");
            return;
        }

        // if subscription is new prompt user for other information
        String cost = getCost();
        String category = getCategory();
        String renewal = getRenewal();
        String startDate = getStartDate();
        String endDate = getEndDate();
        String status = getStatus();

        boolean success = Data.storeNewSubscription(name, cost, category, renewal, startDate, endDate, status);

        if (success) {
            System.out.println("\n✅Subscription added successfully!");
        }
    }

    /**
     * Add/update a category budget and display status to user
     * Save the information into categoryBudgets<>
     */
    private static void addCategoryBudget() {
        System.out.println("\n====ADD/UPDATE CATEGORY BUDGET====");

        // Show existing budgets (if any)
        displayAllBudgets();

        // Get category from user
        String category = getCategory();

        // Update category budget
        double categoryExisting = Data.getCategoryBudget(category); //

        if (categoryExisting != -1) {
            System.out.println("A budget of $" + String.format("%.2f", categoryExisting) + " already exists for '" + category + "'.");
            System.out.println("Do you want to update it? (Y/N)");

            String confirm;

            do {
                confirm = input.nextLine().trim();
                if (!confirm.equalsIgnoreCase("y") && !confirm.equalsIgnoreCase("n")) {
                    System.out.println("Please enter (Y/N)!");
                }
            } while (!confirm.equalsIgnoreCase("y") && !confirm.equalsIgnoreCase("n"));

            if (confirm.equalsIgnoreCase("n")) {
                System.out.println("Budget not updated.");
                return;
            }
        }

        // Set new category budget and display status
        String budgetInput = getBudgetAmount(category);
        double limit = Double.parseDouble(budgetInput); // Will not run into error - already verified

        Data.setCategoryBudget(category, limit);

        System.out.println("\nBudget set successfully!");
        System.out.println("Category : " + Data.formatTitleCase(category));
        System.out.println("Limit    : $" + String.format("%.2f", limit));

    }

    /**
     * handles update subscription - asks user for subscription name--> finds in Data.java-->
     * then asks which field to update and what new value is
     */

    public static void updateProcess() {
        System.out.println("\n====UPDATE SUBSCRIPTION====");
        //loop until valid subscription name is found
        int index = -1;
        while (index == -1) {
            System.out.println("\nEnter name of subscription to update: ");
            String name = input.nextLine().trim();
            //search for subscription by name or return -1 if not found
            index = Data.findingSubscriptionIndex(name);
            //if returned -1, subscription DNE
            if (index == -1) {
                System.out.println("ERROR: '" + name + "' subscription not found. Please try again.");
            }
        }

        System.out.println("\nWhat field do you want to update?");
        System.out.println("1. Name");
        System.out.println("2. Cost");
        System.out.println("3. Category");
        System.out.println("4. Billing cycle");
        System.out.println("5. Start date");
        System.out.println("6. End date");
        System.out.println("7. Status of subscription (ACTIVE/PAUSED/CANCELLED)");

        int choiceIndex = -1;//loop until valid choice is selected
        while (choiceIndex == -1) {
            System.out.print("YOUR DESIRED CHOICE: ");
            String fieldChoice = input.nextLine().trim();
            choiceIndex = getIndexChoice(fieldChoice);
            //if -1, that means choice is invalid, stop
            if (choiceIndex == -1) {
                System.out.println("ERROR: invalid choice selection.");
            }
        }
        //looping until valid newChoice is entered
        String newChoice = "";
        boolean validChoice = false;
        while (!validChoice) {
            System.out.println("Please enter new choice: ");
            newChoice = input.nextLine().trim();

            if (choiceIndex == 6) {//if (6) updating status, ensure it is one of allowed values(ACTIVE,PAUSED,CANCELLED)
                if (!Data.isValidStatus(newChoice.toUpperCase())) {
                    System.out.println("ERROR: status must be ACTIVE/PAUSED/CANCELLED. Try again.");
                } else {
                    newChoice = newChoice.toUpperCase();
                    validChoice = true;
                }
            } else {
                if (newChoice.isEmpty()) {//all other fields(not emtpy), the valid is valid
                    System.out.println("ERROR: value cannot be empty. Try again.");
                } else {
                    validChoice = true;
                }

            }
        }
        //call Data to update field using .set(index,newValue)
        Data.changeSubscriptionChoice(index, choiceIndex, newChoice);
        System.out.println("Update complete.");
    }

    /**
     * displays subscriptions renewing in the next 7 days.
     *
     * @author Ramsha
     */
    public static void upcomingBills() {
        System.out.println("\n==== UPCOMING BILLS ====");
        ArrayList<ArrayList<String>> upcoming = Data.getUpcomingBills();
        if (upcoming.isEmpty()) {
            System.out.println("No upcoming bills in the next 7 days.");
        } else {
            System.out.println("Upcoming bills in the next 7 days:");
            printTable(upcoming);
        }
    }

    private static void showMonthlySpend() {
        double total = Data.calculateMonthlySpend();
        System.out.println("\n--- Total Monthly Spend ---");
        System.out.println("Total: $" + String.format("%.2f", total));
    }

    private static void showYearlySpend() {
        double total = Data.calculateYearlySpend();
        System.out.println("\n--- Total Annual Spend ---");
        System.out.println("Total: $" + String.format("%.2f", total));
    }

    //=====================================DISPLAY MESSAGES===============================//

    /**
     * Displays all current set budgets in a formatted table
     */
    private static void displayAllBudgets() {
        ArrayList<String> budgets = Data.getAllBudgets();
        if (budgets.isEmpty()) {
            System.out.println("No budgets set yet.");
            return;
        }
        System.out.println("\n---Current Budgets---");
        System.out.println("------------------------");
        for (String budget : budgets) {
            System.out.println(" " + budget);
        }
        System.out.println("-------------------------");
    }

    //====================================GETTERS==================================//

    /**
     * Gets budget from user for their chosen category
     * Validates input is a valid cost
     *
     * @param category for the budget (user input)
     * @return String budget specified by user for the chosen category
     */
    private static String getBudgetAmount(String category) {
        System.out.println("Enter the monthly budget limit for '" + category + "' (e.g. 50.00):");
        String budget;
        do {
            budget = input.nextLine().trim().replace("$", "");
            if (!Data.isValidCost(budget)) {
                System.out.println("Invalid amount. Please enter a valid budget (e.g. 50.00):");
            }
        } while (!Data.isValidCost(budget));
        return budget;
    }

    /**
     * Validates/gets name from user
     *
     * @return name/brand of subscription
     */
    private static String getName() {
        String name;

        // Check for input validity
        do {
            System.out.println("\nEnter the name:");
            name = input.nextLine().trim();
            if(!Data.isOnlyText(name)) {
                System.out.println("ERROR: Name must contain only letters. Please try again.");
            }
        } while (name.isEmpty());
        return name;
    }

    /**
     * Validates/gets cost from user
     *
     * @return String cost for new subscription (user input)
     */
    private static String getCost() {
        System.out.println("Enter the cost:");
        String cost;

        // Check for input validity
        do {
            cost = input.nextLine().trim().replace("$", "");
            if (!Data.isValidCost(cost)) {
                System.out.println("Invalid cost. Please enter a valid cost (ex. 15.99):");
            }
        } while (!Data.isValidCost(cost));

        return cost;
    }

    /**
     * Validates/gets category from user
     * Displays previous categories for user to choose from or add new one
     *
     * @return category of the subscription
     */
    private static String getCategory() {
        System.out.println("Enter the category (e.g. Entertainment, Fitness, Education): ");

        //Show existing categories if there are any
        if (!Data.categories.isEmpty()) {
            System.out.println("Existing categories: " + Data.categories);
        }

        //User chooses category/or creates one
        String category;
        do {
            category = input.nextLine().trim();
            if (!Data.isOnlyText(category)) { // check input validity
                System.out.println("ERROR: Category must only contain letters. Please enter the category: ");
            }
        } while (!Data.isOnlyText(category));
        return category;
    }

    /**
     * Validates/gets renewal cycle from user (Monthly or Yearly)
     *
     * @return renewal cycle of subscription
     */
    private static String getRenewal() {
        String renewal;
        System.out.println("Enter the billing cycle (Monthly or Yearly):");

        // check for input validity
        do {
            renewal = input.nextLine().trim();
            if (!renewal.equalsIgnoreCase("monthly") && !renewal.equalsIgnoreCase("yearly")) {
                System.out.println("Please enter a valid billing cycle (Monthly or Yearly)");
            }
        } while (!renewal.equalsIgnoreCase("monthly") && !renewal.equalsIgnoreCase("yearly"));

        return Data.formatTitleCase(renewal); // return formatted version for clean output
    }

    /**
     * Validates/gets start date from user
     *
     * @return start date of subscription (user input)
     */
    private static String getStartDate() {
        String startDate;
        System.out.println("Enter the start date (MM/DD/YYYY): ");

        while (true) {
            startDate = input.nextLine().trim();

            // Checks if it is the valid date format
            if (!Data.isValidDate(startDate)) {
                System.out.println("Invalid format or calendar date! Please use MM/DD/YYYY (e.g., 01/25/2024):");
                continue; // ask again
            }

            // Checks if start is in the future
            if (Data.isFutureDate(startDate)) {
                System.out.println("Start date cannot be in the future! Please enter today's date or a past date:");
                continue; // Ask again
            }

            // break loop, valid inputs verified
            break;
        }
        return startDate;
    }

    /**
     * Validates/gets endDate from user
     *
     * @return endDate for subscription (user input)
     */
    private static String getEndDate() {
        System.out.println("Enter the end date (MM/DD/YYYY) or press Enter to skip:");

        while (true) {
            String endDate = input.nextLine().trim();

            // Allow user to skip
            if (endDate.isEmpty()) {
                return "N/A"; // automatically save as N/A
            }

            // Checks for valid calendar date and format
            if (Data.isValidDate(endDate)) {
                return endDate; // It's a valid date (past or future is fine here)
            } else {
                System.out.println("Invalid format or calendar date. Please use MM/DD/YYYY or press Enter to skip:");
                // Loop continues
            }
        }
    }

    /**
     * Validates/gets status input from user - compares it to STATUS_OPTIONS
     *
     * @return status of subscription (user input)
     */
    private static String getStatus() {
        String status;
        System.out.println("Choose from " + Arrays.toString(Data.STATUS_OPTIONS)); // display options for user clarity
        System.out.println("Enter current status: ");

        // loop until valid input
        do {
            status = input.nextLine().trim().toUpperCase();
            if (!Data.isValidStatus(status)) {
                System.out.println("Choose from " + Arrays.toString(Data.STATUS_OPTIONS));
                System.out.println("Please enter one of the options provided:");
            }
        } while (!Data.isValidStatus(status));
        return status.toUpperCase(); // consistent casing
    }

    /**
     * users desired menu choice(1-7) to ArrayList index(0-6)
     *
     * @param choice users input which is "1"-"7"
     * @return the corresponding ArrayList index 0-6.-1 is invalid
     */

    public static int getIndexChoice(String choice) {
        //each case (e.g. "1") matches to data index
        switch (choice) {
            case "1":
                return 0;
            case "2":
                return 1;
            case "3":
                return 2;
            case "4":
                return 3;
            case "5":
                return 4;
            case "6":
                return 5;
            case "7":
                return 6;
            default:
                return -1;

        }
    }

    /**
     * printTable prints a formatted table of subscriptions to the console/ interface
     * Each subscription is printed as one row with aligned columns of:
     * Name, Cost, Category, Billing, Start Date, End Date, and Status
     * If the end date is null/ empty, "-" is printed
     *
     * @param subs is the list of subscriptions to display
     */
    public static void printTable(ArrayList<ArrayList<String>> subs) {
        // Used printf for column alignment (Java standard library formatting)
        //Print table headers
        System.out.printf("%-15s %-8s %-15s %-10s %-15s %-15s %-10s%n",
                "Name", "Cost", "Category", "Billing", "Start", "End", "Status");

        // Loop through all subs
        for (int i = 0; i < subs.size(); i++) {
            ArrayList<String> sub = subs.get(i);

            //skips invalid sub enteries
            if (sub.size() < 7) {
                continue;
            }

            // Get subscription details using constants
            String subName = sub.get(Data.INDEX_NAME);
            String costStr = sub.get(Data.INDEX_COST);
            String subCategory = sub.get(Data.INDEX_CATEGORY);
            String billing = sub.get(Data.INDEX_RENEWAL);
            String start = sub.get(Data.INDEX_START_DATE);
            String end = sub.get(Data.INDEX_END_DATE);
            String status = sub.get(Data.INDEX_STATUS);

            //if null or empty end dates then replace with "-"
            if (end == null) {
                end = "-";
            } else if (end.isEmpty()) {
                end = "-";
            }

            //Description Row printed
            System.out.printf("%-15s $%-7s %-15s %-10s %-15s %-15s %-10s%n",
                    subName, costStr, subCategory, billing, start, end, status);
        }
    }

    /**
     * Checks all subscription categories with their budget limits and prints alerts
     * Used for categories where total spending on ACTIVE subscriptions exceeds the budget
     *
     * For each category, this method sums the costs of all ACTIVE subscriptions in that category
     * If the total spending > budget for category = budget alert
     * is printed to the console showing the category name, amount spent, and budget limit
     *
     */
    public static void showBudgetAlerts() {
        ArrayList<String> categories = new ArrayList<>(Data.categoryBudgets.keySet());//Gets all categories that have budgets
        //convert into a list
        System.out.println("=== Budget Alert ===");

        //Loop through each category
        for (String category : categories) {// loops through each category
            // Sum of ACTIVE subs costs in this category
            double limit = Data.categoryBudgets.get(category);//Gets the budget limit for that category
            double total = 0; //Initializes total spending for that category

            for (int j = 0; j < Data.subscriptions.size(); j++) {//Loops through subs
                ArrayList<String> sub = Data.subscriptions.get(j);

                if (sub.size() <= 6) { // skip invalid entries
                    continue;
                }

                String subName = sub.get(Data.INDEX_NAME);//gets the sub name
                String costStr = sub.get(Data.INDEX_COST).replace("$", "").trim(); //gets cost
                String subCategory = Data.formatTitleCase(sub.get(Data.INDEX_CATEGORY));// get category and make title case
                String status = sub.get(Data.INDEX_STATUS); //get sub status

                // only count ACTIVE subscriptions in the correct category
                if (subCategory.equalsIgnoreCase(category) && status.equalsIgnoreCase("ACTIVE")) {

                    if (!costStr.isEmpty()) {
                        try {
                            double cost = Double.parseDouble(costStr);
                            total += cost;
                        } catch (NumberFormatException e) {
                            System.out.println("Could not read the cost for: " + subName);
                        }
                    }
                }
            }

            // Alert if budget exceeded
            if (total > limit) {
                System.out.println("🚨 Budget exceeded for " + category);
                System.out.println("Spent: $" + String.format("%.2f", total) + " / Budget: $" + String.format("%.2f", limit));
            }
        }
    }

    /**
     * Finds all ACTIVE subscriptions with a cost >= to the given threshold
     *
     * @param threshold the minimum cost to take into account
     * @return ArrayList of ArrayLists, where each of the inner list represents a subscription
     * where the cost will either meets or exceeds the threshold (>=)
     */
    public static ArrayList<ArrayList<String>> getHighExpenseBills(double threshold) {
        ArrayList<ArrayList<String>> results = new ArrayList<>();//Creates empty list to store expensive subscriptions
        //Returns a list of subscriptions and takes a # (threshold) as minimum cost

        for (ArrayList<String> sub : Data.subscriptions) { //loop through all subs
            if (sub.size() > 6) { // make sure all 7 indexes are there
                String status = sub.get(Data.INDEX_STATUS); //gets sub status

                // Only consider ACTIVE subscriptions
                if (status.equalsIgnoreCase("ACTIVE")) {
                    String costStr = sub.get(Data.INDEX_COST);

                    if (!costStr.isEmpty()) {
                        try {
                            double cost = Double.parseDouble(costStr);//Gets cost as a string and converts to double

                            // Add to expesnive sub to results list if cost >= threshold
                            if (cost >= threshold) {
                                results.add(sub);
                            }
                        } catch (RuntimeException e) {
                            System.out.println("Invalid cost for: " + sub.get(Data.INDEX_NAME));
                        }
                    } else {
                        System.out.println("Cost not entered for: " + sub.get(Data.INDEX_NAME));
                    }
                }
            }
        }
        return results;
    }

    /**
     * Calculates the percentage of the budget used for a given category
     * Iterates through all ACTIVE subscriptions in the category,
     * sums their costs, then divides by the budget limit
     *
     * @param category is the name of the category
     * @return percentage of budget used (0-100) and -1 if category not found
     */

    public static double getCategoryBudgetUsage(String category) {
        if (category == null) {
            return -1;
        }

        // keep formatting consistent throughout the program
        String key = Data.formatTitleCase(category.trim());

        if (key.isEmpty()) {
            return -1;
        }

        if (!Data.categoryBudgets.containsKey(key)) {
            return -1;
        }
        double budgetLimit = Data.categoryBudgets.get(key);
        if (budgetLimit <= 0){
            return 0.0;
        }

        // Loop through subscriptions
        double totalSpent = 0;
        for (int i = 0; i < Data.subscriptions.size(); i++) {
            ArrayList<String> sub = Data.subscriptions.get(i);

            if (sub.size() > 6) {
                String subCategory = sub.get(Data.INDEX_CATEGORY);
                String status = sub.get(Data.INDEX_STATUS);

                String cleanSubCategory = Data.formatTitleCase(subCategory);

                // Only count ACTIVE subs in the correct category
                if (status != null && cleanSubCategory.trim().equals(key) && status.equalsIgnoreCase("ACTIVE")) {

                    try {
                        String costStr = sub.get(Data.INDEX_COST).replace("$", "").trim();
                        if (!costStr.isEmpty()) {
                            totalSpent += Double.parseDouble(costStr);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid cost for subscription: " + sub.get(Data.INDEX_NAME));
                    }
                }
            }
        }
        // Calculate percentage used and round to 2 decimals
        double percentageUsed = (totalSpent / budgetLimit) * 100;
        percentageUsed = Math.round(percentageUsed * 100.0) / 100.0;

        if (percentageUsed > 100) {
            System.out.println("🚨Budget exceeds limit! Currently exceeding by " + String.format("%.2f", (percentageUsed - 100)) + "%");
        }

        return percentageUsed;
    }

    /**
     * Displays all subscriptions stored in the system
     * Prints a formatted table showing all details for each subscription
     */
    public static void showActiveSubscriptions() {
        System.out.println("=== Active Subscriptions ===");

        ArrayList<ArrayList<String>> activeSubs = new ArrayList<>();

        // Filter for ACTIVE subs
        for (int i = 0; i < Data.subscriptions.size(); i++) {
            ArrayList<String> sub = Data.subscriptions.get(i);

            if (sub.size() > 6) {
                String status = sub.get(Data.INDEX_STATUS);
                if (status.equalsIgnoreCase(Data.STATUS_OPTIONS[0])) { //Active
                    activeSubs.add(sub);
                }
            }
        }

        // Print table or message if none found
            if (activeSubs.isEmpty()) {
                System.out.println("No active subscriptions found.");
            } else {
                printTable(activeSubs);
            }
    }
    /**
     * Displays all subscriptions that are currently paused
     *Loop through all subscriptions in the system and gets
     * "PAUSED" status and prints as a formatted table showing
     *
     * If no paused subscriptions exist, a message is printed instead.
     *
     */
    public static void showPausedSubscriptions() {
        System.out.println("==== PAUSED SUBSCRIPTIONS ====");

        ArrayList<ArrayList<String>> pausedSubs = new ArrayList<>();

        // Filter for PAUSED subs
        for (int i = 0; i < Data.subscriptions.size(); i++) {
            ArrayList<String> sub = Data.subscriptions.get(i);

            if (sub.size() > 6) {
                String status = sub.get(Data.INDEX_STATUS);

                if (status.equalsIgnoreCase(Data.STATUS_OPTIONS[1])) {
                    pausedSubs.add(sub);
                }
            }
        }
        // Print table or message if none found
        if (pausedSubs.isEmpty()) {
            System.out.println("No paused subscriptions.");
        } else {
            printTable(pausedSubs);
        }
    }
    public static void showAllSubscriptions() {
        System.out.println("=== All Subscriptions ===");
        if (Data.subscriptions.isEmpty()) {
            System.out.println("No subscriptions found.");
        } else {
            printTable(Data.subscriptions);
        }
    }
    /**
     * Displays the percentage of the budget used for a category
     *user to enter category name, then calculates ACTIVE sub costs
     * of a % of budget
     *
     * If no budget is set for the entered category, message printed
     * otherwise % is rounded to two decimal places
     */
    public static void showCategoryBudgetUsage() {
        System.out.println("=== Budget % used by Category ===");
        System.out.print("Enter category to check budget usage: ");

        String category = input.nextLine().trim(); // Read user input and remove extra spaces
        double percentage = getCategoryBudgetUsage(category); // Calculate budget usage for the category
        // Check if category has a budget set
        if (percentage < 0) {
            System.out.println("No budget set for this category.");// Print message if no budget exists
        } else {
            // Print the budget usage rounded to two decimal places
            System.out.println(
                    "Category '" + category + "' Budget used: "
                            + String.format("%.2f", percentage) + "%"
            );
        }
    }

    /**
     * Displays all ACTIVE subs with cost >= thershold
     * user enters a threshold amount
     * getHighExpenseBills(double threshold) called
     *
     * Results are printed in a formatted table
     * If no subscriptions meet threshold, a message is displayed
     *
     */
    public static void showHighExpenseSubscriptions() {
        System.out.print("Enter threshold amount: $");
        double threshold = Double.parseDouble(input.nextLine().trim()); // Read and convert input to double

        ArrayList<ArrayList<String>> highExpense = getHighExpenseBills(threshold);// Get subscriptions above threshold
        // Check if any subscriptions meet the threshold
        if (highExpense.isEmpty()) {
            System.out.println("No high-expense bills were found.");// Print message if none found
        } else {
            System.out.println("=== High-Expense Subscriptions ===");
            printTable(highExpense);// Print the subscriptions in a formatted table
        }
    }
    /**
     * user searches subs by name/ category and displays the results
     * The method asks the user to enter a search type:
     * "1" for searching by sub name, or "2" for category
     * then it asks for a keyword to search for and results are displayed in a formatted table
     *
     * Input validation:
     * Only "1" or "2" is accepted for search type
     * The keyword cannot be empty
     *If no matching subs are found, a message is displayed
     */
    public static void showSearchResults() {

        String searchNumber;
        System.out.print("Search by (1) Name or (2) Category: ");

        do {
            searchNumber = input.nextLine().trim();
            if (!searchNumber.equals("1") && !searchNumber.equals("2")) {
                System.out.println("Invalid entry! Please enter (1) or (2): ");
            }
        } while (!searchNumber.equals("1") && !searchNumber.equals("2"));


        System.out.print("Enter search keyword: ");
        String keyword = input.nextLine().trim();

        //validate keyword
        if (keyword.isEmpty()) {
            System.out.println("Search keyword cannot be empty.");
            return;
        }

        ArrayList<ArrayList<String>> results;

        if (searchNumber.equals("1")) {
            results = Data.searchByName(keyword); //search by name
        } else {
            results = Data.searchByCategory(keyword); //search by category
        }
        // Print results or message if none found
        if (results.isEmpty()) {
            System.out.println("No matching subscriptions found.");
        } else {
            System.out.println("=== Search Results ===");
            printTable(results);
        }
    }
}
