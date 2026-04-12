package ca.ucalgary.cpsc233w26project1.GUI;

import ca.ucalgary.cpsc233w26project1.SubscriptionManager;
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
    }
}

