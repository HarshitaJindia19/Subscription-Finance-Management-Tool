package ca.ucalgary.cpsc233w26project1.GUI;

import ca.ucalgary.cpsc233w26project1.Budget;
import ca.ucalgary.cpsc233w26project1.SubscriptionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * BudgetPercentByCategoryController.java - shows percent of budget used per category
 * Displays spending vs budget and counts how many are over budget
 * @date April 14 2026
 * @tutorial T01
 */
public class BudgetPercentByCategoryController {

    //connects to table in FXML
    @FXML
    private TableView<Row> table;

    //connects to table columns in FXML
    @FXML
    private TableColumn<Row, String> categoryCol;
    @FXML
    private TableColumn<Row, String> budgetCol;
    @FXML
    private TableColumn<Row, String> spentCol;
    @FXML
    private TableColumn<Row, String> percentCol;

    //label to display summary (number of over-budget categories)
    @FXML
    private Label summaryLabel;

    private SubscriptionManager manager;

    /**
     * Called by main controller to pass shared manager
     * Automatically loads table after setting manager
     * @param manager shared SubscriptionManager
     */
    public void setManager(SubscriptionManager manager) {
        this.manager = manager;
        load();
    }

    /**
     * Loads all budget percentage data into the table
     * Calculates spending and percentage for each category
     */
    private void load() {

        //check if manager is initialized
        if (manager == null) {
            summaryLabel.setText("Manager not initialized.");
            return;
        }

        //clear previous table data
        table.getItems().clear();

        int overBudgetCount = 0;

        //set up column mappings from Row object
        categoryCol.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getCategory()));

        budgetCol.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getBudget()));

        spentCol.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getSpent()));

        percentCol.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getPercent()));

        //loop through all budgets
        for (int i = 0; i < manager.getAllBudgets().size(); i++) {

            Budget budg = manager.getAllBudgets().get(i);
            String category = budg.getCategory();

            //keep track if we find an active subscription
            boolean hasActive = false;
            //go through each subscription using index
            var subs = manager.getAllSubscriptions();
            for (int j = 0; j < subs.size(); j++) {
                var sub = subs.get(j);
                //check if category matches and status is ACTIVE
                if (sub.getCategory().equalsIgnoreCase(category)
                        && sub.getStatus().toString().equals("ACTIVE")) {
                    //we found one set to true
                    hasActive = true;
                    break;
                }
            }
            //if no active subscriptions found, skip this loop
            if (!hasActive) {
                continue;
            }

            //get budget limit and spending
            double budget = budg.getLimit();
            double spent = manager.getSpendByCategory(category);

            //calculate percent used
            double percent = manager.getBudgetPercentage(category);

            //cap percent at 100%
            if (percent > 100) {
                percent = 100;
            }

            //count categories that are over budget
            if (percent >= 100) {
                overBudgetCount++;
            }

            //add row to table
            table.getItems().add(
                    new Row(category, budget, spent, percent)
            );
        }

        //update summary label
        summaryLabel.setText("Over budget categories: " + overBudgetCount);
    }

    /**
     * Inner class representing one row in the table
     * Stores category, budget, spending, and percent values
     */
    public static class Row {

        private String category;
        private double budget;
        private double spent;
        private double percent;

        /**
         * Constructor to create a row object
         */
        public Row(String category, double budget,
                   double spent, double percent) {
            this.category = category;
            this.budget = budget;
            this.spent = spent;
            this.percent = percent;
        }

        //getter methods used by TableView
        public String getCategory() {
            return category;
        }

        public String getBudget() {
            return "$" + String.format("%.2f", budget);
        }

        public String getSpent() {
            return "$" + String.format("%.2f", spent);
        }

        public String getPercent() {
            if (percent < 0) return "N/A";
            return String.format("%.1f%%", percent);
        }
    }
}