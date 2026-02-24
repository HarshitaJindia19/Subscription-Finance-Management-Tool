import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Data {

    public static ArrayList<ArrayList<String>> subscriptions = new ArrayList<>();
    public static HashMap<String, Double> categoryBudgets = new HashMap<>();
    public static HashSet<String> categories = new HashSet<>();
    public static String[] status = {"ACTIVE", "PAUSED", "CANCELLED"};
}
