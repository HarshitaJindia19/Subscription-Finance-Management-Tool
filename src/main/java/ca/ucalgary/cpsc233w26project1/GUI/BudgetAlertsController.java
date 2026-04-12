package ca.ucalgary.cpsc233w26project1.GUI;

import ca.ucalgary.cpsc233w26project1.Budget;
import ca.ucalgary.cpsc233w26project1.SubscriptionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class BudgetAlertsController {

    @FXML
    private TableView<Row> budgetTable;

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
    @FXML
    private Label summaryLabel;

    private SubscriptionManager manager;

    public void setManager(SubscriptionManager manager) {
        this.manager = manager;
        loadTable();
    }

    private void loadTable() {

        if (manager == null) {
            summaryLabel.setText("Manager not initialized.");
            return;
        }

        budgetTable.getItems().clear();
        int overBudgetCount = 0;

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

        for (int i = 0; i < manager.getAllBudgets().size(); i++) {
            Budget bgt = manager.getAllBudgets().get(i);
            String category = bgt.getCategory();
            double budget = bgt.getLimit();
            double spent = manager.getSpendByCategory(category);
            double percent = manager.getBudgetPercentage(category);

            String status;
            if (percent == -1) {
                status = "No Budget";
            } else if (percent >= 100) {
                status = "🔴 OVER BUDGET";
                overBudgetCount++;
            } else if (percent >= 80) {
                status = "🟡 WARNING";
            } else {
                status = "🟢 OK";
            }

            budgetTable.getItems().add(
                    new Row(category, budget, spent, percent, status)
            );
        }
        summaryLabel.setText("Over budget categories: " + overBudgetCount);
    }
}
