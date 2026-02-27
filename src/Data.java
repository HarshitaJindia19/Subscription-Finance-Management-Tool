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
     * Updates a specific field of a subscription using .set(index,newValue).
     * @author Ramsha
     * @param subIndex    index of subscription in outer ArrayList
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
