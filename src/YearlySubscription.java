import java.time.LocalDate;

/**
 * YearlySubscription.java - subscriptions that are billed yearly
 * @author Ramsha
 * @date March 18 2026
 * @tutorial T01
 */

public class YearlySubscription extends Subscription {
    /**
     * constructor calls parent Subscription constructor
     */
    public YearlySubscription(String name, double cost, String category, LocalDate startDate, LocalDate endDate, Status status) {
        super(name, cost, category, "Yearly", startDate, endDate, status);
    }

    /**
     * returns monthly cost - yearly cost / 12
     *
     * @return monthly cost as a double
     */
    @Override
    public double getMonthlyCost() {
        return getCost() / 12;
    }
    /**
     * returns yearly cost - just the direct cost
     *
     * @return yearly cost as double
     */
    @Override
    public double getYearlyCost() {
        return getCost();
    }

//    @Override
//    public String toString() {
//        return "Yearly -> " + super.toString();
//    }
}