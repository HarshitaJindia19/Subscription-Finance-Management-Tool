package ca.ucalgary.cpsc233w26project1.GUI;

import ca.ucalgary.cpsc233w26project1.Budget;
import ca.ucalgary.cpsc233w26project1.SubscriptionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class BudgetPercentByCategoryController {
    @FXML
    private TableView<Row> table;

    @FXML
    private TableColumn<Row, String> categoryCol;
    @FXML
    private TableColumn<Row, String> budgetCol;
    @FXML
    private TableColumn<Row, String> spentCol;
    @FXML
    private TableColumn<Row, String> percentCol;

    @FXML
    private Label summaryLabel;

    private SubscriptionManager manager;

    public void setManager(SubscriptionManager manager) {
        this.manager = manager;
        load();
    }

    private void load() {
        if (manager == null) {
            summaryLabel.setText("Manager not initialized.");
            return;
        }
        table.getItems().clear();
        int overBudgetCount = 0;

        categoryCol.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getCategory()));

        budgetCol.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getBudget()));

        spentCol.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getSpent()));

        percentCol.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getPercent()));

        for (int i = 0; i < manager.getAllBudgets().size(); i++) {
            Budget budg = manager.getAllBudgets().get(i);
            String category = budg.getCategory();

            double budget = budg.getLimit();
            double spent = manager.getSpendByCategory(category);
            double percent = manager.getBudgetPercentage(category);

            if (percent >= 100) {
                overBudgetCount++;
            }
            table.getItems().add(
                    new Row(category, budget, spent, percent)
            );
        }
        summaryLabel.setText("Over budget categories: " + overBudgetCount);
    }
}

