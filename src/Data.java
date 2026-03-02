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
            subscription.add(name);
            subscription.add(cost);
            subscription.add(category);
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

    // Check functions
    public static boolean subscriptionExists(String name) {
        return names.contains(name);
    }

    //Store budget for each category
    public static void setCategoryBudget(String category, double limit) {
        //Store in HashMap -> Key = Category Name, Value = Budget Limit
        categoryBudgets.put(category.toLowerCase(), limit);
        categories.add(category);
    }

    //Calculates Monthly spend of the user
    public static double calculateMonthlySpend() {
        return 0;
    }

    //Calculates yearly spend of the user
    public static double calculateYearlySpend() {
        return 0;
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
            if (subscriptions.get(i).get(0).equalsIgnoreCase(name)) {
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
                String subName= sub.get(0);

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
                String subCategory= sub.get(2);

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
//new method here, add javadoc comments later
public static void printTable(ArrayList<ArrayList<String>> subs) {
      //look into how to make the interface later
    for (int i = 0; i < subs.size(); i++) {
        ArrayList<String> sub = subs.get(i);

        if (sub.size() <= 6) {
            continue;
        }

        String subName= sub.get(0);
        String costStr= sub.get(1);
        String subCategory= sub.get(2);
        String billing= sub.get(3);
        String start= sub.get(4);
        String end= sub.get(5);
        String status= sub.get(6);

        if (end == null) {
            end = "-";
        } else if (end.isEmpty()) {
            end = "-";
        }



    /**
     * Finds all ACTIVE subscriptions with a cost >= to the given threshold
     *@param threshold the minimum cost to take into account
     * @return ArrayList of ArrayLists, where each of the inner list represents a subscription
     *where the cost will either meets or exceeds the threshold (>=)
     */
    public static ArrayList<ArrayList<String>> getHighExpenseBills(double threshold) {
        ArrayList<ArrayList<String>> results = new ArrayList<>();

        for (int i=0; i < subscriptions.size(); i++) {
            ArrayList<String> sub= subscriptions.get(i);

        if (sub.size() > 6) {
            String status= sub.get(6);

                if (status.equalsIgnoreCase("ACTIVE")) {
                    String costStr = sub.get(1);

                    if (costStr.equals("") == false) {
                        try {
                            double cost = Double.parseDouble(costStr);

                            if (cost >= threshold) {
                                results.add(sub);
                            }
                        } catch (RuntimeException e) {
                            System.out.println("Invalid cost for: " + sub.get(0));
                        }
                    } else {
                        System.out.println("Cost not entered for: " + sub.get(0));
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

                    String subName = sub.get(0);
                    String costStr = sub.get(1);
                    String subCategory = sub.get(2);
                    String status = sub.get(6);

                    if (subCategory.toLowerCase().equals(category.toLowerCase())) {
                        if (status.toLowerCase().equals("active")) {
                            costStr = costStr.replace("$", "").trim();

                            if (costStr.equals("") == false) {
                                try {
                                    double cost = Double.parseDouble(costStr);
                                    total += cost;
                                } catch (RuntimeException e) {
                                    System.out.println("Could not read the cost for: " + subName);

                                    if (total > limit) {
                                        System.out.println("🚨 Budget exceeded for " + category);
                                        System.out.println("Spent: $" + String.format("%.2f", total) + " / Budget: $" + String.format("%.2f", limit));
                                    }
                                }

                            }
                        }
                    }
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

    /**
     * Checks if status string is one of the valid options(ACTIVE, PAUSED, CANCELLED stored in STATUS_OPTIONS)
     * @author Ramsha
     * @param status status string to ensure is right
     * @return true if valid, false if invalid
     */

    //--------------------------------Valid Checks----------------------------------------//
    //loop through STATUS_OPTIONS string array and check that one matches
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
}
