import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class SubscriptionManager {

    private ArrayList<Subscription> subscriptions;
    private ArrayList<Budget> budgets;

    public SubscriptionManager() {
        subscriptions = new ArrayList<>();
        budgets = new ArrayList<>();
    }

    //============================= Subscription Managements ==============================//

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
    public boolean storeNewSubscription(String name, double cost, String category, String renewal, LocalDate startDate, LocalDate endDate, Status status) {
        String cleanName = formatTitleCase(name);
        String cleanCategory = formatTitleCase(category);
        String cleanRenewal = formatTitleCase(renewal);

        if (subscriptionExists(cleanName)) {
            return false;
        }

        Subscription subscription;
        // create MonthlySubscription or YearlySubscription based on renewal type
        if (cleanRenewal.equalsIgnoreCase("Monthly")) {
            subscription = new MonthlySubscription(cleanName, cost, cleanCategory, startDate, endDate, status);
        } else {
            subscription = new YearlySubscription(cleanName, cost, cleanCategory, startDate, endDate, status);
        }

        subscriptions.add(subscription);
        return true;
    }

    /**
     * Finds the index of subscription in outer ArrayList by ensuring the name matches.
     * @author Ramsha
     * @param name the name of subscription to search for
     * @return index of subscription in outer ArrayList or -1 if not found
     */
    public int findingSubscriptionIndex(String name) {
        //loop through every subscription in list
        for (int i = 0; i < subscriptions.size(); i++) {
            //get(i) - subscription row
            //get(0) - name at index 0
            if (subscriptions.get(i).getName().equalsIgnoreCase(name)) {
                return i;//found - return its position
            }
        }
        return -1;//not found
    }

    public boolean subscriptionExists(String name) {
        return findingSubscriptionIndex(name) != -1;
    }

    public ArrayList<Subscription> getAllSubscriptions() {
        return subscriptions;
    }

    public ArrayList<Subscription> getActiveSubscription() {
        ArrayList<Subscription> results = new ArrayList<>();
        for (Subscription sub : subscriptions) {
            if (sub.getStatus() == Status.ACTIVE) {
                results.add(sub);
            }
        }
        return results;
    }

    public ArrayList<Subscription> getPausedSubscription() {
        ArrayList<Subscription> results = new ArrayList<>();
        for (Subscription sub : subscriptions) {
            if (sub.getStatus() == Status.PAUSED) {
                results.add(sub);
            }
        }
        return results;
    }


    /**
     * Updates a specific field of a subscription using .set(index,newValue).
     *
     * @param subIndex    index of subscription in outer ArrayList
     * @param choiceIndex index of field to update in inner ArrayList
     * @param newChoice   new value to set
     * @author Ramsha
     */

    public void changeSubscriptionChoice(int subIndex, int choiceIndex, String newChoice) {
        Subscription sub=subscriptions.get(subIndex);
        //use setters for users choice
        switch (choiceIndex) {
            case 0: sub.setName(newChoice);
                break;
            case 1: sub.setCost(Double.parseDouble(newChoice));
                break;
            case 2: sub.setCategory(newChoice);
                break;
            case 3: sub.setRenewal(newChoice);
                break;
            case 4: sub.setStartDate(LocalDate.parse(newChoice, DateTimeFormatter.ofPattern("MM/dd/yyyy")));
                break;
            case 5: sub.setEndDate(LocalDate.parse(newChoice, DateTimeFormatter.ofPattern("MM/dd/yyyy")));
                break;
            case 6: sub.setStatus(Status.getStatus(newChoice));
                break;
        }
    }

    //=============================== Budget Management ============================//

    /**
     * Formats category string to title case
     * Stores received budget/limit from Menu.java
     *
     * @author Harshita
     * @param category of subscription chosen by user
     * @param limit of the budget set by the user
     */
    // Set new budget or overwrite budget with new limit
    public void setCategoryBudget(String category, double limit) {
        String cleanCategory = formatTitleCase(category);
        for (Budget budget : budgets) {
            if (budget.getCategory().equalsIgnoreCase(cleanCategory)) {
                budget.setLimit(limit);
                return;
            }
        }
        budgets.add(new Budget(cleanCategory, limit));
    }

    /**
     * Gets the budget for the specified category if it already exists
     * @author Harshita
     * @param category selected by the user for their subscription
     * @return budget for the category, -1 if not found
     */
    public double getCategoryBudget(String category) {

        for (Budget budget: budgets) {
            if (budget.getCategory().equalsIgnoreCase(category)) {
                return budget.getLimit();
            }
        }
        return -1.0;
    }

    /**
     * Lists all categories and budgets in a clean format
     *
     * @return all the budgets currently stored by category
     * @author Harshita
     */
    public ArrayList<Budget> getAllBudgets() {
        return budgets;
    }

    public ArrayList<String> getCategories() {
        ArrayList<String> categories = new ArrayList<>();
        for (Subscription sub : subscriptions) {
            if (!categories.contains(sub.getCategory())) {
                categories.add(sub.getCategory());
            }
        }
        return categories;
    }

    //================================= Calculations ==================================//

    /**
     * Takes in all active subscriptions and totals their cost for monthly spend
     * @author Harshita
     * @return total monthly spend of user as double
     */
    public double calculateMonthlySpend() {
        double total = 0.0;

        for (Subscription sub : subscriptions) {
            if (sub.getStatus() == Status.ACTIVE) {
                total += sub.getMonthlyCost();
            }
        }
        return total;
    }

    /**
     * Takes in all active subscriptions and totals their cost for yearly spend
     *
     * @return total monthly spend of user as double
     * @author Harshita
     */
    public double calculateYearlySpend() {
        double total = 0.0;
        for (Subscription sub : subscriptions) {
            if (sub.getStatus() == Status.ACTIVE) {
                total += sub.getYearlyCost();
            }
        }
        return total;
    }

    // Get the total spend of the category for all active subs
    public double getSpendByCategory(String category) {
        double total = 0.0;

        // Find all subscriptions with the target category and are ACTIVE
        for (Subscription sub: subscriptions) {
            if (sub.getCategory().equalsIgnoreCase(category) && sub.getStatus() == Status.ACTIVE) {
                total += sub.getMonthlyCost();
            }
        }
        return total;
    }

    // Get the total spent of the budget as percentage
    public double getBudgetPercentage(String category) {
        double limit = getCategoryBudget(category);
        double spent = getSpendByCategory(category);

        if (limit == -1.0) return -1.0; // Budget for category doesn't exist
        return (spent/limit) * 100;
    }

    public ArrayList<Subscription> getHighExpenseBills(double threshold) {
        ArrayList<Subscription> highExpenseSubs = new ArrayList<>();
        for (Subscription sub : subscriptions) {
            if (sub.getStatus() == Status.ACTIVE && sub.getMonthlyCost() >= threshold) {
                highExpenseSubs.add(sub);
            }
        }
        return highExpenseSubs;
    }

    /**
     * Finds subscriptions with a start date within the next 7 days.
     * @author Ramsha
     * @return ArrayList of subscriptions renewing within 7 days
     */
    public ArrayList<Subscription> getUpcomingBills() {
        ArrayList<Subscription> results = new ArrayList<>();

        //get today's date
        LocalDate today = LocalDate.now();
        LocalDate sevenDaysLater = today.plusDays(7);

        for (Subscription sub : subscriptions) {
            //use instanceof to check subclass type
            if (sub instanceof MonthlySubscription monthlySub) {
                //use isUpcoming() from MonthlySubscription
                if (monthlySub.isUpcoming()) {
                    results.add(sub);
                }
            } else if (sub instanceof YearlySubscription) {
                //for yearly, check if annual renewal is within next 7 days
                try {
                    //parse MM/DD/YYYY format
                    LocalDate startDate = sub.getStartDate();
                    LocalDate nextRenewal = startDate.withYear(today.getYear());
                    if (nextRenewal.isBefore(today)) {
                        nextRenewal = nextRenewal.plusYears(1);
                    }
                    if (!nextRenewal.isBefore(today) && !nextRenewal.isAfter(sevenDaysLater)) {
                        results.add(sub);
                    }
                } catch (Exception e) {//skip if date is invalid
                }
            }
        }

        return results;
    }

    //================================== Search =======================================//

    /**
     * Search Engine to find all the subscriptions under the given name
     * @param name the name of subscription to search for
     * @return ArrayList of ArrayLists, where inner list shows a
     * subscription that matches the name
     */
    public ArrayList<Subscription> searchByName(String name) {
        ArrayList<Subscription> results = new ArrayList<>(); // Initialize results list

        if (name == null || name.trim().isEmpty()) {// If input is null or isEmpty, return empty list
            return results;
        }

        String keyword = name.trim().toLowerCase(); // Convert search to lowercase

        for (Subscription sub : subscriptions) { // Loop through all subs
            if (sub.getName().toLowerCase().contains(keyword)) {// Check if name contains keyword
                results.add(sub);// Add matching subs to results
            }
        }
        return results; // Return list of matching subs
    }

    /**
     * Search Engine to find all the subscriptions under the given category
     *
     * @param category is the category of subscription to search for
     * @return ArrayList of ArrayLists, where each inner list shows a
     *subscription that matches the category
     */

    public ArrayList<Subscription> searchByCategory(String category) {
        ArrayList<Subscription> results = new ArrayList<>();

        if (category == null || category.trim().isEmpty()) { // Return empty list if input is null
            return results;
        }

        String keyword = category.trim().toLowerCase(); // Convert search to lowercase
        for (Subscription sub : subscriptions) { // Loop through all subs

            if (sub.getCategory().toLowerCase().contains(keyword)) {// Check if category contains keyword
                results.add(sub); // Add matching subs to results
            }

        }
        return results; // Return list of matching subs
    }

    //===================================== Valid Checks =====================================//

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
     * Validates if a String contains only characters and spaces (no special characters and numbers)
     *
     * @param input collected from Menu.java
     * @return True if input String contains only letters or spaces
     */
    public static boolean isOnlyText(String input) {
        if (input == null || input.isEmpty()) return false;
        return input.matches("^[a-zA-Z\\s]+$"); // https://stackoverflow.com/questions/24191040/checking-to-see-if-a-string-is-letters-spaces-only
    }

    /**
     * Checks if date is in the future
     *
     * @param date (end/start) of the subscription ( user input )
     * @return boolean true if Date is in the future, false otherwise
     */
    public static boolean isFutureDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/uuuu");
        LocalDate parsedDate = LocalDate.parse(date, formatter);
        return parsedDate.isAfter(LocalDate.now());
    }

    /**
     * Checks if date (start/end) are valid
     * @author Harshita
     * @param date (end/start) of the subscription ( user input )
     * @return boolean true if valid, false otherwise
     */
    public static boolean isValidDate(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/uuuu").withResolverStyle(ResolverStyle.STRICT);
            LocalDate.parse(date, formatter); // parse the date string
            return true;
        } catch (DateTimeParseException e) {
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
            return value > 0 && value < 100000; // reasonable range for subscription
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
