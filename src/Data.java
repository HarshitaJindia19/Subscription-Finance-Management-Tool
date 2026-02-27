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
     * @param name the name of subscription to search for
     * @return index of subscription or -1 if index is invalid
     * @author Ramsha
     * finds the index of subcription in outer ArrayList by ensuring the name matches
     *
     */
    public static int findingSubscriptionIndex(String name) {
        for (int i = 0; i < subscriptions.size(); i++) {
            if (subscriptions.get(i).get(0).equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }

    /**
     *
     * @param subIndex    index of subscriptions in outer ArrayList
     * @param choiceIndex index of choice to update
     * @param newChoice   new choice to set
     */
    public static void changeSubscriptionChoice(int subIndex, int choiceIndex, String newChoice) {
        subscriptions.get(subIndex).set(choiceIndex, newChoice);
    }

    /**
     * check if status string is valid options
     *
     * @param status status to validate
     * @return true if valid, false if invalid
     */

    public static boolean validStatus(String status) {
        for (String validString : STATUS_OPTIONS) {
            if (validString.equals(status)) {
                return true;
            }
        }
        return false;
    }
}
