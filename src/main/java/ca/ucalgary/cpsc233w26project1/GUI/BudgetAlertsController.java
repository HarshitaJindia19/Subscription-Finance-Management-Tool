package ca.ucalgary.cpsc233w26project1.GUI;

import ca.ucalgary.cpsc233w26project1.Budget;
import ca.ucalgary.cpsc233w26project1.SubscriptionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * BudgetAlertsController.java - displays budget usage, percent spent, and alerts
 * Shows warning and over-budget categories in a table
 * @date April 14 2026
 * @tutorial T01
 */
public class BudgetAlertsController {

    //connects to table in FXML
    @FXML
    private TableView<Row> budgetTable;

    //connects to table columns in FXML
    @FXML
    private TableColumn<Row, String> categoryCol;
    @FXML
    private TableColumn<Row, String> budgetCol;
    @FXML
    private TableColumn<Row, String> spentCol;
    @FXML
    private TableColumn<Row, String> percentCol;
    @FXML
    private TableColumn<Row, String> statusCol;

    //label to show summary (number of over-budget categories)
    @FXML
    private Label summaryLabel;

    private SubscriptionManager manager;

    /**
     * Called by main controller to pass in shared manager
     * Automatically loads table data after setting manager
     * @param manager shared SubscriptionManager
     */
    public void setManager(SubscriptionManager manager) {
        this.manager = manager;
        loadTable();
    }

    /**
     * Loads all budget data into the table
     * Calculates spending, percentage, and assigns status labels
     */
    private void loadTable() {

        //check if manager is initialized
        if (manager == null) {
            summaryLabel.setText("Manager not initialized.");
            return;
        }

        //clear existing rows before loading new data
        budgetTable.getItems().clear();

        int overBudgetCount = 0;

        //set up each column to pull data from Row object
        categoryCol.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue().getCategory()
                )
        );

        budgetCol.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue().getBudget()
                )
        );

        spentCol.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue().getSpent()
                )
        );

        percentCol.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue().getPercent()
                )
        );

        statusCol.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue().getStatus()
                )
        );

        //loop through all budgets in manager
        for (int i = 0; i < manager.getAllBudgets().size(); i++) {

            Budget bgt = manager.getAllBudgets().get(i);
            String category = bgt.getCategory();
            double budget = bgt.getLimit();

            //keep track if we find an active subscription
            boolean hasActive = false;
            //get all subscriptions
            var subs = manager.getAllSubscriptions();
            //go through each subscription
            for (int j = 0; j < subs.size(); j++) {
                var sub = subs.get(j);
                //check if category matches and status is ACTIVE
                if (sub.getCategory().equalsIgnoreCase(category)
                        && sub.getStatus().toString().equals("ACTIVE")) {
                    //we found one set to true
                    hasActive = true;
                    //stop checking more
                    break;
                }
            }
            //if none were active skip this loop
            if (!hasActive) {
                continue;
            }

            //calculate how much is spent in this category
            double spent = manager.getSpendByCategory(category);

            //calculate percent of budget used
            double percent = manager.getBudgetPercentage(category);

            //cap percentage at 100% (prevents values like 150%)
            percent = Math.min(percent, 100.0);

            //determine status based on percentage
            String status;

            if (percent == -1) {
                status = "No Budget";
            }
            else if (percent >= 100) {
                status = "🔴 OVER BUDGET";
                overBudgetCount++;
            }
            else if (percent >= 80) {
                status = "🟡 WARNING";
            }
            else {
                status = "🟢 OK";
            }

            //add row to table
            budgetTable.getItems().add(
                    new Row(category, budget, spent, percent, status)
            );
        }

        //update summary label
        summaryLabel.setText("Over budget categories: " + overBudgetCount);
    }

    /**
     * Inner class representing one row in the table
     * Stores formatted data for display
     */
    public static class Row {

        private String category;
        private double budget;
        private double spent;
        private double percent;
        private String status;

        /**
         * Constructor to create a row object
         */
        public Row(String category, double budget, double spent,
                   double percent, String status) {
            this.category = category;
            this.budget = budget;
            this.spent = spent;
            this.percent = percent;
            this.status = status;
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

        public String getStatus() {
            return status;
        }
    }
}