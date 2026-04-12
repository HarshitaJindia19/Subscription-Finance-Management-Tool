package ca.ucalgary.cpsc233w26project1.GUI;

import ca.ucalgary.cpsc233w26project1.SubscriptionManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddBudgetController {

    @FXML private TextField categoryField;
    @FXML private TextField limitField;
    @FXML private Label messageLabel;
    @FXML private Button closeButton;

    private SubscriptionManager manager;

    public void setManager(SubscriptionManager manager) {
        this.manager = manager;
    }

    @FXML
    private void addBudget() {
        String category = categoryField.getText().trim();
        String limitText = limitField.getText().trim();

        if (category.isEmpty()) {
            showError("Category cannot be empty.");
            return;
        }
        if (!SubscriptionManager.isOnlyText(category)) {
            showError("Category must contain only letters.");
            return;
        }
        if (!SubscriptionManager.isValidCost(limitText)) {
            showError("Limit must be a valid positive number.");
            return;
        }
        double limit = Double.parseDouble(limitText);
        manager.setCategoryBudget(category, limit);
        showSuccess("Budget saved for " + category + "!");

        categoryField.clear();
        limitField.clear();
    }

    @FXML
    private void close() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    private void showError(String msg) {
        messageLabel.setText("⛔ " + msg);
        messageLabel.setStyle("-fx-text-fill: red;");
    }
    private void showSuccess(String msg) {
        messageLabel.setText("✅ " + msg);
        messageLabel.setStyle("-fx-text-fill: green;");
    }
}