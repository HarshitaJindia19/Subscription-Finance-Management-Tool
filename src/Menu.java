import java.util.Scanner;

/**
 * Menu.java - handles the main menu loop and user interface
 * @author Ramsha Oad
 * @date Feb 24 2026
 * @tutorial T01
 *
 */
public class Menu {
    private static final Scanner input = new Scanner(System.in);

    public static void runMenu() {
        boolean running = true;
        while (running) {
            System.out.println("====SUBSCRIPTION MANAGER===");
            System.out.println("1. Add Subscription");
            System.out.println("2. Update Subscription");
            System.out.println("3. View all Subscriptions");
            System.out.println("4. Exit");
            System.out.print("Select an option: ");
            String choice = input.nextLine().trim();

            switch (choice) {
                case "1":
                    //harshitas work
                    break;
                case "2":
                    updateProcess();
                    break;
                case "3":
                    //mehrins work
                    break;
                case "4":
                    running = false;
                    System.out.println("Exited.");
                    break;
                default:
                    System.out.println("Invalid choice, please enter numbers 1-4.");

            }
        }
        input.close();
    }

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

    private static String getName() {
        System.out.println("Enter the name:");
        String name;
        do {
            name = input.nextLine().trim();
        } while (name.isEmpty());
        return name;
    }

    /**
     * @return String cost for new subscription (user input)
     */
    private static String getCost() {
        System.out.println("Enter the cost:");
        String cost;

        do {
            cost = input.nextLine().trim().replace("$", "");
            if (!isValidCost(cost)) {
                System.out.println("Invalid cost. Please enter a valid cost (ex. 15.99)");
            }
        } while (!isValidCost(cost));

        return cost;
    }

    /**
     * @param cost for new subscription
     * @return boolean true if cost is double, false otherwise
     */
    private static boolean isValidCost(String cost) {
        try {
            double value = Double.parseDouble(cost);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

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

    private static String getStartDate() {
        String startDate;
        System.out.println("Enter the start date (MM/DD/YYYY): ");

        do{
            startDate = input.nextLine().trim();
            if(!isValidDate(startDate)) {
                System.out.println("Invalid date. Please use the format MM/DD/YYYY (e.g. 03/05/2016).");
            }
        } while(!isValidDate(startDate));



        return startDate;
    }

    /**
     * @return endDate for subscription (user input)
     */
    private static String getEndDate() {
        String endDate;
        System.out.println("Enter the end date (MM/DD/YYYY): ");

        do {
            endDate = input.nextLine().trim();
            if (!isValidDate(endDate)) {
                System.out.println("Invalid date. Please use the format MM/DD/YYYY (e.g. 03/05/2016).");
            }

        } while (!isValidDate(endDate));

        return endDate;
    }

    private static boolean isValidDate(String date) {

        // check the basic format MM/DD/YYYY
        if (date.length() != 10) {
            return false;
        }
        if (date.charAt(2) != '/' || date.charAt(5) != '/') {
            return false;
        }

        // Check bounds of month, day, year
        try {
            int month = Integer.parseInt(date.substring(0, 2));
            int day = Integer.parseInt(date.substring(3, 5));
            int year = Integer.parseInt(date.substring(6, 10));

            if (month < 1 || month > 12) {
                return false;
            }
            if (day < 1 || day > 31) {
                return false;
            }
            if (year < 2000) {
                return false;
            }
            return true;

        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static String getStatus(){
        return "";
    }


    public static void updateProcess() {
        System.out.println("\nEnter name of subscription to update: ");
        String name = input.nextLine().trim();

        int index = Data.findingSubscriptionIndex(name);
        if (index == -1) {
            System.out.println("ERROR: '" + name + "' subscription not found.");
            return;
        }
        System.out.println("\nWhat do you want to update?");
        System.out.println("1. Name");
        System.out.println("2. Cost");
        System.out.println("3. Category");
        System.out.println("4. Billing cycle");
        System.out.println("5. Start date");
        System.out.println("6. End date");
        System.out.println("7. Status of subscription (ACTIVE/PAUSED/CANCELLED)");
        System.out.print("YOUR DESIRED CHOICE: ");
        String fieldChoice = input.nextLine().trim();

        int choiceIndex = getIndexChoice(fieldChoice);
        if (choiceIndex == -1) {
            System.out.println("ERROR: invalid choice selection.");
            return;
        }
        System.out.println("Please enter new choice: ");
        String newChoice = input.nextLine().trim();

        if (choiceIndex == 6) {
            if (!Data.validStatus(newChoice.toUpperCase())) {
                System.out.println("ERROR: status must be ACTIVE/PAUSED/CANCELLED");
                return;
            }
            newChoice = newChoice.toUpperCase();
        }
        Data.changeSubscriptionChoice(index, choiceIndex, newChoice);
        System.out.println("Update done!");
    }

    /**
     * users desired menu choice --> ArrayList index
     *
     * @param choice users input which is 1-7
     * @return the index 0-6.-1 is invalid
     */

    public static int getIndexChoice(String choice) {

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
    }}
