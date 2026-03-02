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

    private static final int INDEX_NAME = 0;
    private static final int INDEX_COST = 1;
    private static final int INDEX_CATEGORY = 2;
    private static final int INDEX_RENEWAL = 3;
    private static final int INDEX_START_DATE = 4;
    private static final int INDEX_END_DATE = 5;
    private static final int INDEX_STATUS = 6;


    // Create function to add new subscription
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
            names.add(name);
            categories.add(category); //no duplicates
            return true;
        } else {
            return false;
        }
    }

    /**
     * Formats category string to title case
     * Stores received budget/limit from Menu.java
     */
    public static void setCategoryBudget(String category, double limit) {
        // Format to title case: "netflix" -> "Netflix"
        String cleanCategory = formatTitleCase(category);

        categoryBudgets.put(cleanCategory, limit);
        categories.add(cleanCategory);
    }

    /**
     * Formats user input into title case for clean data storage
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
     * @param category selected by the user for their subscription
     * @return budget for the category, -1 if not found
     */
    public static double getCategoryBudget(String category) {
        return categoryBudgets.getOrDefault(formatTitleCase(category), -1.0);
    }

    /**
     * Lists all categories and budgets in a clean format
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

    //Calculates Monthly spend of the user
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

    //Calculates yearly spend of the user
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

        for (int i = 0; i < subscriptions.size(); i++) {
            ArrayList<String> sub= subscriptions.get(i);
            if (sub.size() > 2) {
                String subCategory= sub.get(INDEX_CATEGORY);

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

    //add Javadoc comments later
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

            }
        }
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
