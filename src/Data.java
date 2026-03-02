import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Data {

    public static final ArrayList<ArrayList<String>> subscriptions = new ArrayList<>();
    public static final HashMap<String, Double> categoryBudgets = new HashMap<>();
    public static final HashSet<String> categories = new HashSet<>();
    public static final HashSet<String> names = new HashSet<>();
    public static final String[] STATUS_OPTIONS = {"ACTIVE", "PAUSED", "CANCELLED"};

    // Store Index for each subscription information

    public static final int INDEX_NAME = 0;
    public static final int INDEX_COST = 1;
    public static final int INDEX_CATEGORY = 2;
    public static final int INDEX_RENEWAL = 3;
    public static final int INDEX_START_DATE = 4;
    public static final int INDEX_END_DATE = 5;
    public static final int INDEX_STATUS = 6;


    /**
     * Checks if subscription is new, and adds it to the subscription ArrayList
     * Adds categories and names to their respective HashSets
     *
     * @author Harshita
     * @param name of subscription - e.g. Netflix
     * @param cost of subscription e.g. 56.99
     * @param category of subscription - e.g. Health
     * @param renewal of subscription - e.g. Monthly
     * @param startDate of subscription - e.g. 05/20/2027
     * @param endDate of subscription - e.g. 05/20/2027 or N/A
     * @param status of subscription - e.g. PAUSED
     * @return boolean - true if subscription was new and was stored into list, false otherwise
     */
    public static boolean storeNewSubscription(String name, String cost, String category, String renewal, String startDate, String endDate, String status) {
        if (!subscriptionExists(name)) {
            ArrayList<String> subscription = new ArrayList<>();
            subscription.add(name.toLowerCase());
            subscription.add(cost);
            subscription.add(formatTitleCase(category));
            subscription.add(renewal);
            subscription.add(startDate);
            subscription.add(endDate);
            subscription.add(status);
            subscriptions.add(subscription);
            names.add(name.toLowerCase());
            categories.add(formatTitleCase(category)); //no duplicates
            return true;
        } else {
            return false;
        }
    }

    /**
     * Formats category string to title case
     * Stores received budget/limit from Menu.java
     *
     * @author Harshita
     * @param category of subscription chosen by user
     * @param limit of the budget set by the user
     */
    public static void setCategoryBudget(String category, double limit) {
        // format to title case "netflix" -> "Netflix"
        String cleanCategory = formatTitleCase(category);

        categoryBudgets.put(cleanCategory, limit);
        categories.add(cleanCategory);
    }

    /**
     * Formats user input into title case for clean data storage
     * @author Harshita
     * @param input from user
     * @return formatted input
     */
    public static String formatTitleCase(String input) {
        if (input.isEmpty()) return "";
        return input.substring(0, 1).toUpperCase() +
                input.substring(1).toLowerCase();
    }

    /**
     * Gets the budget for the specified category if it already exists
     * @author Harshita
     * @param category selected by the user for their subscription
     * @return budget for the category, -1 if not found
     */
    public static double getCategoryBudget(String category) {
        return categoryBudgets.getOrDefault(formatTitleCase(category), -1.0);
    }

    /**
     * Lists all categories and budgets in a clean format
     * @author Harshita
     * @return all the budgets currently stored by category
     */
    public static ArrayList<String> getAllBudgets() {
        ArrayList<String> budgetList = new ArrayList<>();
        for (String category: categoryBudgets.keySet()) {
            double limit = categoryBudgets.get(category);
            budgetList.add(category + ": $" + String.format("%.2f", limit));
        }
        return budgetList;
    }


    /**
     * Takes in all active subscriptions and totals their cost for monthly spend
     * @author Harshita
     * @return total monthly spend of user as double
     */
    public static double calculateMonthlySpend() {
        double total = 0.0;
        double numOfMonths = 12;

        for (ArrayList<String> sub : subscriptions) {

            if (sub.get(INDEX_STATUS).equalsIgnoreCase("ACTIVE")) {
                double cost = Double.parseDouble(sub.get(INDEX_COST));
                String renewal = sub.get(INDEX_RENEWAL);

                if (renewal.equalsIgnoreCase("MONTHLY")) {
                    total += cost;
                } else if (renewal.equalsIgnoreCase("YEARLY")) {
                    total += cost / numOfMonths; // convert yearly to monthly by dividing costs amongst months
                }
            }
        }

        return total;
    }

    /**
     * Takes in all active subscriptions and totals their cost for yearly spend
     * @author Harshita
     * @return total monthly spend of user as double
     */
    public static double calculateYearlySpend() {

        double total = 0.0;
        double numOfMonths = 12;

        for (ArrayList<String> sub : subscriptions) {

            if (sub.get(INDEX_STATUS).equalsIgnoreCase("ACTIVE")) {
                double cost = Double.parseDouble(sub.get(INDEX_COST));
                String renewal = sub.get(INDEX_RENEWAL);

                if (renewal.equalsIgnoreCase("MONTHLY")) {
                    total += cost * numOfMonths; // convert monthly to yearly
                } else if (renewal.equalsIgnoreCase("YEARLY")) {
                    total += cost; // convert yearly to monthly by dividing costs amongst months
                }
            }
        }

        return total;
    }

    /**
     * Finds the index of subscription in outer ArrayList by ensuring the name matches.
     * @author Ramsha
     * @param name the name of subscription to search for
     * @return index of subscription in outer ArrayList or -1 if not found
     */
    public static int findingSubscriptionIndex(String name) {
        //loop through every subscription in list
        for (int i = 0; i < subscriptions.size(); i++) {
            //get(i) - subscription row
            //get(0) - name at index 0
            if (subscriptions.get(i).get(INDEX_NAME).equalsIgnoreCase(name)) {
                return i;//found - return its position
            }
        }
        return -1;//not found
    }
    /**
     * Finds subscriptions with a start date within the next 7 days.
     * @author Ramsha
     * @return ArrayList of subscriptions renewing within 7 days
     */
    public static ArrayList<ArrayList<String>> getUpcomingBills() {
        ArrayList<ArrayList<String>> results = new ArrayList<>();

        //get todays date
        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.LocalDate sevenDaysLater = today.plusDays(7);

        for (int i = 0; i < subscriptions.size(); i++) {
            ArrayList<String> sub = subscriptions.get(i);
            if (sub.size() <= 6) continue;

            String dateStr = sub.get(4); //start date at index 4

            try {
                //parse MM/DD/YYYY format
                java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy");
                java.time.LocalDate subDate = java.time.LocalDate.parse(dateStr, formatter);

                //check if date is within next 7 days
                if (!subDate.isBefore(today) && !subDate.isAfter(sevenDaysLater)) {
                    results.add(sub);
                }
            } catch (Exception e) {
                //skip if date cant be parsed
            }
        }
        return results;
    }

    /**
     * Search Engine to find all the subscriptions under the given name
     * @param name the name of subscription to search for
     * @return ArrayList of ArrayLists, where inner list shows a
     * subscription that matches the name
     */
    public static ArrayList<ArrayList<String>> searchByName(String name) {
        ArrayList<ArrayList<String>> results = new ArrayList<>();
        if (name == null) return results;

        for (int i = 0; i < subscriptions.size(); i++) {
            ArrayList<String> sub= subscriptions.get(i);

            if (sub.size() > 0) {
                String subName= sub.get(INDEX_NAME);
                if (subName == null) continue;

                String subNameLower= subName.toLowerCase();
                String newNameLower= name.toLowerCase();

                if (subNameLower.equals(newNameLower)) {
                    results.add(sub);
                }else {
                }
            }else {
            }
        }
        return results;
    }
    /**
     * Search Engine to find all the subscriptions under the given category
     * @param category is the category of subscription to search for
     * @return ArrayList of ArrayLists, where each inner list shows a
     *subscription that matches the category
     */

    public static ArrayList<ArrayList<String>> searchByCategory(String category) {
        ArrayList<ArrayList<String>> results = new ArrayList<>();
        if (category == null) return results;

        for (int i = 0; i < subscriptions.size(); i++) {
            ArrayList<String> sub= subscriptions.get(i);
            if (sub.size() > 2) {
                String subCategory= sub.get(2);
                if (subCategory == null) continue;

                String subCategoryLower= subCategory.toLowerCase();
                String categoryNewLower= category.toLowerCase();

                if (subCategoryLower.equals(categoryNewLower)) {
                    results.add(sub);
                }else {
                }
            }else {
            }
        }
        return results;
    }

    /**
     * Finds all ACTIVE subscriptions with a cost >= to the given threshold
     *@param threshold the minimum cost to take into account
     * @return ArrayList of ArrayLists, where each of the inner list represents a subscription
     *where the cost will either meets or exceeds the threshold (>=)
     */
    public static ArrayList<ArrayList<String>> getHighExpenseBills(double threshold) {
        ArrayList<ArrayList<String>> results = new ArrayList<>();

        for (ArrayList<String> sub : subscriptions) {
            if (sub.size() > 6) {
                String status = sub.get(INDEX_STATUS);

                if (status.equalsIgnoreCase("ACTIVE")) {
                    String costStr = sub.get(INDEX_COST);

                    if (!costStr.isEmpty()) {
                        try {
                            double cost = Double.parseDouble(costStr);

                            if (cost >= threshold) {
                                results.add(sub);
                            }
                        } catch (RuntimeException e) {
                            System.out.println("Invalid cost for: " + sub.get(INDEX_NAME));
                        }
                    } else {
                        System.out.println("Cost not entered for: " + sub.get(INDEX_NAME));
                    }
                }
            }
        }
        return results;
    }

    /**
     * Checks all subscription categories with their budget limits and prints alerts
     * Used for categories where total spending on ACTIVE subscriptions exceeds the budget
     *
     * For each category, this method sums the costs of all ACTIVE subscriptions in that category
     * If the total spending > budget for category = budget alert
     * is printed to the console showing the category name, amount spent, and budget limit
     *
     * This method doesn't return a value, only printing messages to console
     */
    public static void showBudgetAlerts() {
        ArrayList<String> categories = new ArrayList<>(categoryBudgets.keySet());

        for (int i = 0; i < categories.size(); i++) {
            String category = categories.get(i);

            double limit = categoryBudgets.get(category);
            double total = 0;

            for (int j = 0; j < subscriptions.size(); j++) {
                ArrayList<String> sub = subscriptions.get(j);

                if (sub.size() <= 6) {
                    continue;
                }

                String subName = sub.get(INDEX_NAME);
                String costStr = sub.get(INDEX_COST);
                String subCategory = sub.get(INDEX_CATEGORY);
                String status = sub.get(INDEX_STATUS);

                    if (costStr.equals("") == false) {
                        try {
                            double cost = Double.parseDouble(costStr);
                            total += cost;
                            overBudgetSubs.add(sub);
                        } catch (RuntimeException e) {
                            System.out.println("Could not read the cost for: " + subName);
                        }
                    } else {
                    }
                } else {
                }
            } else {
            }
        }
        if (total > limit) {
            System.out.println("🚨 Budget exceeded for " + category);
            System.out.println("Spent: $" + String.format("%.2f", total) + " / Budget: $" + String.format("%.2f", limit));
            System.out.println("Subscriptions causing alert:");
            Data.printTable(overBudgetSubs);
        } else {
        }
      }
    }

    /**
     * printTable prints a formatted table of subscriptions to the console/ interface
     *Each subscription is printed as one row with aligned columns of:
     * Name, Cost, Category, Billing, Start Date, End Date, and Status
     * If the end date is null/ empty, "-" is printed
     * @param subs is the list of subscriptions to display
     */
    public static void printTable(ArrayList<ArrayList<String>> subs) {
        // Used printf for column alignment (Java standard library formatting)
        System.out.printf("%-15s %-8s %-15s %-10s %-15s %-15s %-10s%n",
                "Name", "Cost", "Category", "Billing", "Start", "End", "Status");

        for (int i = 0; i < subs.size(); i++) {
            ArrayList<String> sub = subs.get(i);

            if (sub.size() <= 6) {
                continue;
            }

            String subName = sub.get(0);
            String costStr = sub.get(1);
            String subCategory = sub.get(2);
            String billing = sub.get(3);
            String start = sub.get(4);
            String end = sub.get(5);
            String status = sub.get(6);

            if (end == null) {
                end = "-";
            } else if (end.isEmpty()) {
                end = "-";
            }

       System.out.printf("%-15s $%-7s %-15s %-10s %-15s %-15s %-10s%n",
       subName, costStr, subCategory, billing, start, end, status);
            }
        }
    /**
     * Calculates the percentage of the budget used for a given category
     * Iterates through all ACTIVE subscriptions in the category,
     * sums their costs, then divides by the budget limit
     * @param category is the name of the category
     * @return percentage of budget used (0-100) and -1 if category not found
     */

    public static double getCategoryBudgetUsage(String category) {
        if (categoryBudgets.containsKey(category.toLowerCase()) == false) {
            return -1;
        }

        double totalSpent = 0;
        for (int i = 0; i < subscriptions.size(); i++) {
            ArrayList<String> sub = subscriptions.get(i);
            if (sub.size() > 6) {
                String subCategory = sub.get(2);
                String status = sub.get(6);

                if (subCategory.equalsIgnoreCase(category) == true && status.equalsIgnoreCase("active") == true) {
                    String costStr = sub.get(1).replace("$", "").trim();
                    if (costStr.isEmpty() == false) {
                        try {
                            totalSpent += Double.parseDouble(costStr);
                        } catch (RuntimeException e) {
                            System.out.println("Invalid cost for subscription: " + sub.get(0));
                        }
                    }
                }
            }
        }
        double budgetLimit = categoryBudgets.get(category.toLowerCase());
        double percentageUsed = (totalSpent / budgetLimit) * 100;

        if (percentageUsed > 100) {
            percentageUsed = 100;
        }

        return percentageUsed;
    }

   /**
   * Updates a specific field of a subscription using .set(index,newValue).
   * @author Ramsha
   * @param subIndex index of subscription in outer ArrayList
   * @param choiceIndex index of field to update in inner ArrayList
   * @param newChoice   new value to set
    */

    public static void changeSubscriptionChoice(int subIndex, int choiceIndex, String newChoice) {
        //get(subIndex) - finds correct subscription row
        //.set(choiceIndex, newChoice) - replaces value at field
        subscriptions.get(subIndex).set(choiceIndex, newChoice);
    }


    //--------------------------------Valid Checks----------------------------------------//

    public static boolean subscriptionExists(String name) {
        return names.contains(name.toLowerCase());
    }

    /**
     * Checks if status string is one of the valid options(ACTIVE, PAUSED, CANCELLED stored in STATUS_OPTIONS)
     * @author Ramsha
     * @param status status string to ensure is right
     * @return true if valid, false if invalid
     */

    public static boolean isValidStatus(String status) {
        for (String validString : STATUS_OPTIONS) {
            if (validString.equals(status)) {
                return true;//matched
            }
        }
        return false;//nothing to match
    }

    /**
     * Checks if date (start/end) are valid
     * @author Harshita
     * @param date (end/start) of the subscription ( user input )
     * @return boolean true if valid, false otherwise
     */
    public static boolean isValidDate(String date) {

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

    /**
     * Checks if cost is valid
     * @author Harshita
     * @param cost for new subscription
     * @return boolean true if cost is double, false otherwise
     */
    public static boolean isValidCost(String cost) {
        try {
            double value = Double.parseDouble(cost);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //======================================== TEST DATA =========================================//

    public static void loadTestData() {
        storeNewSubscription("Netflix", "15.99", "Entertainment", "Monthly", "01/01/2025", null, "ACTIVE");
        storeNewSubscription("GoodEarth", "9.99", "Food", "Monthly", "02/01/2025", null, "ACTIVE");
        storeNewSubscription("GoodLife", "50.00", "Fitness", "Monthly", "03/01/2025", null, "ACTIVE");
        storeNewSubscription("YouTube Premium", "13.99", "Entertainment", "Monthly", "04/01/2025", null, "PAUSED");

        setCategoryBudget("Entertainment", 60.00);
        setCategoryBudget("Fitness", 100.00);
    }
}
