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
    public static final ArrayList<String> options= new ArrayList<>();

    //static runs once when class is first loaded - used here because options is a static variable
    static{
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
                    // mehrins work
                    break;
                case "4":
                    // mehrins work
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
                    // mehrins work
                    break;
                case "9":
                    // mehrins work
                    break;
                case "10":
                    // mehrins work
                    break;
                case "11":
                    // mehrins work
                    break;
                case "12":
                    // mehrins work
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
        boolean success = false;

        do {
            String name = getName();
            String cost = getCost();
            String category = getCategory();
            String renewal = getRenewal();
            String startDate = getStartDate();
            String endDate = getEndDate();
            String status = getStatus();
            success = Data.storeNewSubscription(name, cost, category, renewal, startDate, endDate, status);
        } while(!success);
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
        System.out.println("Choose the category you want to add the subscription for: ");
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
        //loop until valid subscription name is found
        int index = -1;
        while (index==-1) {
            System.out.println("\nEnter name of subscription to update: ");
            String name= input.nextLine().trim();
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

        int choiceIndex= -1;//loop until valid choice is selected
        while (choiceIndex ==-1) {
            System.out.print("YOUR DESIRED CHOICE: ");
            String fieldChoice=input.nextLine().trim();
            choiceIndex=getIndexChoice(fieldChoice);
            //if -1, that means choice is invalid, stop
            if (choiceIndex == -1) {
                System.out.println("ERROR: invalid choice selection.");
            }
        }
        //looping until valid newChoice is entered
        String newChoice="";
        boolean validChoice=false;
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
            }else {
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
     * @author Ramsha
     */
    public static void upcomingBills() {
        ArrayList<ArrayList<String>> upcoming = Data.getUpcomingBills();
        if (upcoming.isEmpty()) {
            System.out.println("No upcoming bills in the next 7 days.");
        } else {
            System.out.println("Upcoming bills in the next 7 days:");
            Data.printTable(upcoming);
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
        for (String budget: budgets) {
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
     * @return name/brand of subscription
     */
    private static String getName() {
        System.out.println("\nEnter the name:");
        String name;
        do {
            name = input.nextLine().trim();
        } while (name.isEmpty());
        return name;
    }

    /**
     * Validates/gets cost from user
     * @return String cost for new subscription (user input)
     */
    private static String getCost() {
        System.out.println("Enter the cost:");
        String cost;

        do {
            cost = input.nextLine().trim().replace("$", "");
            if (!Data.isValidCost(cost)) {
                System.out.println("Invalid cost. Please enter a valid cost (ex. 15.99)");
            }
        } while (!Data.isValidCost(cost));

        return cost;
    }

    /**
     * Validates/gets category from user
     * Displays previous categories for user to choose from or add new one
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
            if (category.isEmpty()) {
                System.out.println("Category cannot be empty, please enter a category: ");
            }
        } while (category.isEmpty());
        return category;
    }

    /**
     * Validates/gets renewal cycle from user (Monthly or Yearly)
     * @return renewal cycle of subscription
     */
    private static String getRenewal() {
        String renewal;
        System.out.println("Enter the billing cycle (Monthly or Yearly):");

        do {
            renewal = input.nextLine().trim();
            if (!renewal.equalsIgnoreCase("monthly") && !renewal.equalsIgnoreCase("yearly")) {
                System.out.println("Please enter a valid billing cycle (Monthly or Yearly)");
            }
        } while(!renewal.equalsIgnoreCase("monthly") && !renewal.equalsIgnoreCase("yearly"));

        return renewal;
    }

    /**
     * Validates/gets start date from user
     * @return start date of subscription (user input)
     */
    private static String getStartDate() {
        String startDate;
        System.out.println("Enter the start date (MM/DD/YYYY): ");

        do{
            startDate = input.nextLine().trim();
            if(!Data.isValidDate(startDate)) {
                System.out.println("Invalid date. Please use the format MM/DD/YYYY (e.g. 03/05/2016).");
            }
        } while(!Data.isValidDate(startDate));

        return startDate;
    }

    /**
     * Validates/gets endDate from user
     * @return endDate for subscription (user input)
     */
    private static String getEndDate() {

        System.out.println("Enter the end date (MM/DD/YYYY) or press Enter to skip:");
        String endDate = input.nextLine().trim();

        if (endDate.isEmpty()) {
            return "N/A";
        }

        while (!Data.isValidDate(endDate)) {
            System.out.println("Invalid date. Please use MM/DD/YYYY or press Enter to skip:");
            endDate = input.nextLine().trim();
            if (endDate.isEmpty()) {
                return "N/A";
            }
        }
        return endDate;
    }

    /**
     * Validates/gets status input from user - compares it to STATUS_OPTIONS
     * @return status of subscription (user input)
     */
    private static String getStatus(){
        String status;
        System.out.println("Choose from " + Arrays.toString(Data.STATUS_OPTIONS));
        System.out.println("Enter current status: ");

        do {
            status = input.nextLine().trim().toUpperCase();
            if(!Data.isValidStatus(status)) {
                System.out.println("Choose from " + Arrays.toString(Data.STATUS_OPTIONS));
                System.out.println("Please enter one of the options provided:");
            }
        } while (!Data.isValidStatus(status));
        return status;
    }

    /**
     * users desired menu choice(1-7) to ArrayList index(0-6)
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
}
