import java.time.LocalDate;

/**
 * MonthlySubscription.java - subscriptions that is billed monthly
 * @author Ramsha
 * @date March 18 2026
 * @tutorial T01
 */
public class MonthlySubscription extends Subscription {

    //constructor calls parent Subscription constructor
    public MonthlySubscription(String name, double cost, String category,LocalDate startDate, LocalDate endDate, Status status){
        super(name, cost, category, "Monthly", startDate, endDate, status);
    }
    /**
     * Returns monthly cost - for monthly subscriptions this is just the cost directly
     * @return monthly cost as double
     */
    @Override
    public double getMonthlyCost() {
        return getCost();
    }
    /**
     * Returns yearly cost - monthly cost multiplied by 12
     * @return yearly cost as double
     */
    @Override
    public double getYearlyCost() {
        return getCost()*12;
    }
    /**
     * checks if this subscription renews within the next 7 days
     * @return true if upcoming in 7 days
     */
    public boolean isUpcoming() {
        LocalDate today = LocalDate.now();
        LocalDate sevenDaysLater=today.plusDays(7);

        //replace year and month with current to check this months renewal
        LocalDate nextRenewal = getStartDate()
                .withMonth(today.getMonthValue())
                .withYear(today.getYear());
        //if already passed this month, check next month
        if (nextRenewal.isBefore(today)) {
            nextRenewal = nextRenewal.plusMonths(1);
        }
        return !nextRenewal.isBefore(today)&& !nextRenewal.isAfter(sevenDaysLater);

    }
    @Override
    public String toString() {
        return "Monthly -> "+super.toString();
    }
}