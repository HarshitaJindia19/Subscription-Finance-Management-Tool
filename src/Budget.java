public class Budget implements Comparable<Budget> {

    private String category;
    private double limit;

    // Constructor
    public Budget(String category, double limit) {
        this.category = category;
        this.limit = limit;
    }

    public String getCategory() {
        return category;
    }

    public double getLimit() {
        return limit;
    }

    public double setLimit(double limit) {
        return this.limit = limit;
    }

    /**
     * Two budgets are equal if they have the same category
     * @param object the reference object with which to compare.
     * @return boolean
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Budget)) return false;

        Budget other = (Budget) object;
        return this.category.equalsIgnoreCase(other.category);
    }

    /**
     * hashCode based on lowercase category
     * @return int
     */
    @Override
    public int hashCode() { return category.toLowerCase().hashCode(); }


    /**
     * Sort budgets alphabetically by category
     * @param other the object to be compared
     * @return int
     */
    @Override
    public int compareTo(Budget other) {
        return this.category.compareToIgnoreCase(other.category);
    }

    /**
     * Displays Budget/Category information
     * @return String output displaying category budget information
     */
    @Override
    public String toString() {
        return category + ": $" + String.format("%.2f", limit);
    }
}
