import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Data {

    public static final ArrayList<Object[]> subscriptions = new ArrayList<>();
    public static final HashMap<String, Double> categoryBudgets = new HashMap<>();
    public static final HashSet<String> categories = new HashSet<>();
    public static final String[] STATUS_OPTIONS = {"ACTIVE", "PAUSED", "CANCELLED"};

    //

    // Create function to add new subscription
    public static boolean storeNewSubscription(String name, String cost, String category, String renewal, String startDate, String endDate, String status) {

        Object[] subscription = new Object[7];
        subscription[0] = name;
        subscription[1] = cost;
        subscription[2] = category;
        subscription[3] = renewal;
        subscription[4] = startDate;
        subscription[5] = endDate;
        subscription[6] = status;

        subscriptions.add(subscription);

        categories.add(category); //adds category to set - no duplicates

        return true;
    }

    //Store budget for each category
    public static void setCategoryBudget(String category, double limit) {
        //Store in HashMap -> Key = Category Name, Value = Budget Limit
        categoryBudgets.put(category.toLowerCase(), limit);
    }

//    //Calculates Monthly spend of the user
//    public static double calculateMonthlySpend() {
//        return 0;
//    }
//
//    //Calculates yearly spend of the user
//    public static double calculateYearlySpend() {
//        return 0;
//    }
}
