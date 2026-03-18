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

    public LocalDate getEndDate() {
        return endDate;
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
