import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Subscription implements Comparable<Subscription> {

    private String name, category, renewal;

    private Status status;

    private double cost;

    private LocalDate startDate, endDate;

    public Subscription(String name, double cost, String category, String renewal, LocalDate startDate, LocalDate endDate, Status status) {
        this.name = name;
        this.cost = cost;
        this.category = category;
        this.renewal = renewal;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;

    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getRenewal() {
        return renewal;
    }

    public Status getStatus() {
        return status;
    }

    public double getCost() {
        return cost;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {return endDate;}

    public void setName(String name) { this.name = name; }
    public void setCost(double cost) { this.cost = cost; }
    public void setCategory(String category) { this.category = category; }
    public void setRenewal(String renewal) { this.renewal = renewal; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public void setStatus(Status status) { this.status = status; }


    public double getMonthlyCost() { return getCost(); }

    public double getYearlyCost() { return getCost() * 12; }


    /**
     * Compares two subscription objects to check if they have the same name
     * @param object of subscription to compare name of
     * @return true if name are the same, otherwise false
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Subscription other)) return false;

        return this.name.equalsIgnoreCase(other.name);
    }

    /**
     *
     * @param other the subscription object to be compared.
     * @return int priority order
     */
    @Override
    public int compareTo(Subscription other) {

        //Sort by status priority first
        int priorityThis = getStatusPriority(this.status);
        int priorityOther = getStatusPriority(other.status);

        //
        if (priorityThis != priorityOther) {
            return priorityThis - priorityOther;
        }

        // If same status, sort alphabetically by name
        return this.name.compareToIgnoreCase(other.name);
    }

    /**
     * Helper to assign sort priority to each status (Lower number, higher priority)
     * @param status the Status enum to get priority
     * @return int priority value
     */
    private int getStatusPriority(Status status) {
        return switch (status) {
            case ACTIVE -> 0;
            case PAUSED -> 1;
            case CANCELLED -> 2;
        };
    }

    /**
     * @return Display string for subscription
     */
    @Override
    public String toString() {

        // format startDate and endDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String endDateDisplay = (endDate != null) ? endDate.format(formatter) : "N/A";

        return String.format("%-15s $%-7.2f %-15s %-10s %-15s %-15s %-10s", name, cost, category, renewal, startDate.format(formatter), endDateDisplay, status.toString());
    }
}
