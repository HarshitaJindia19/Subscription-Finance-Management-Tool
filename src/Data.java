import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Data {

    public static final ArrayList<Object[]> subscriptions = new ArrayList<>();
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
            Object[] subscription = new Object[7];
            subscription[INDEX_NAME] = name;
            subscription[INDEX_COST] = cost;
            subscription[INDEX_CATEGORY] = category;
            subscription[INDEX_RENEWAL] = renewal;
            subscription[INDEX_START_DATE] = startDate;
            subscription[INDEX_END_DATE] = endDate;
            subscription[INDEX_STATUS] = status;
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
}