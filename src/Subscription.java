import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Subscription {

    private String name, category, renewal, status;

    private double cost;

    private LocalDate startDate, endDate;

    public Subscription(String name, double cost, String category, String renewal, LocalDate startDate, LocalDate endDate, String status) {
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

    public String getStatus() {
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
    public void setStatus(String status) { this.status = status; }


    public double getMonthlyCost() { return getCost(); }

    public double getYearlyCost() { return getCost() * 12; }

    // Subscription with the same name cannot exist, check if equals
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || !(object instanceof Subscription)) return false;

        Subscription other = (Subscription) object;
        return this.name.equalsIgnoreCase(other.name);
    }

    // Hashcode to set name to lowerCase, for equals to match
    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String endDateDisplay = (endDate != null) ? endDate.format(formatter) : "N/A";

        return "Subscription{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", renewal='" + renewal + '\'' +
                ", status='" + status + '\'' +
                ", cost=" + cost +
                ", startDate=" + startDate.format(formatter) +
                ", endDate=" + endDateDisplay +
                '}';
    }
}
