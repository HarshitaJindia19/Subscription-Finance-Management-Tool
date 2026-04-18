package ca.ucalgary.cpsc233w26project1.GUI;

import ca.ucalgary.cpsc233w26project1.SubscriptionManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * AddBudgetController.java - handles adding new budget categories and limits
 * @date April 14 2026
 * @tutorial T01
 */
public class AddBudgetController {

    //connects to text fields in FXML
    @FXML private TextField categoryField;
    @FXML private TextField limitField;

    //label to display success/error messages
    @FXML private Label messageLabel;

    //button used to close the window
    @FXML private Button closeButton;

    private SubscriptionManager manager;

    /**
     * Called by main controller to pass in the shared manager
     * @param manager shared SubscriptionManager
     */
    public void setManager(SubscriptionManager manager) {
        this.manager = manager;
    }

    /**
     * Called when user clicks "Add Budget" button
     * Validates input and saves budget into manager
     */
    @FXML
    private void addBudget() {

        //get user input from text fields
        String category = categoryField.getText().trim();
        String limitText = limitField.getText().trim();

        //check if category is empty
        if (category.isEmpty()) {
            showError("Category cannot be empty.");
            return;
        }

        //check if category contains only letters
        if (!SubscriptionManager.isOnlyText(category)) {
            showError("Category must contain only letters.");
            return;
        }

        //check if limit is a valid number
        if (!SubscriptionManager.isValidCost(limitText)) {
            showError("Limit must be a valid positive number.");
            return;
        }

        //convert limit to double
        double limit = Double.parseDouble(limitText);

        //Make sure that if setting budget the category is active
        //this will track if we find an active subscription
        boolean hasActive = false;
        //get the list of all subscriptions
        var subs = manager.getAllSubscriptions();
        //loop through the list using an index
        for (int i = 0; i < subs.size(); i++) {
            //get one subscription from the list
            var sub = subs.get(i);

            //check if category matches AND status is ACTIVE
            if (sub.getCategory().equalsIgnoreCase(category)
                    && sub.getStatus().toString().equals("ACTIVE")) {
                //we found one so set to true
                hasActive = true;
                //stop loop since we don't need to keep checking
                break;
            }
        }
        //if we did NOT find any active subscriptions
        if (!hasActive) {
            showError("Cannot set budget. No ACTIVE subscriptions in this category.");
            return;
        }


        //save budget into manager
        manager.setCategoryBudget(category, limit);

        //show success message
        showSuccess("Budget saved for " + category + "!");

        //clear input fields after saving
        categoryField.clear();
        limitField.clear();
    }

    /**
     * Closes the current window when user clicks close button
     */
    @FXML
    private void close() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Displays an error message in red text
     * @param msg error message to show
     */
    private void showError(String msg) {
        messageLabel.setText("⛔ " + msg);
        messageLabel.setStyle("-fx-text-fill: red;");
    }

    /**
     * Displays a success message in green text
     * @param msg success message to show
     */
    private void showSuccess(String msg) {
        messageLabel.setText("✅ " + msg);
        messageLabel.setStyle("-fx-text-fill: green;");
    }
}