import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

/**
 * UpdateSubscriptionController.java - handles the Update Subscription popup
 * @author Ramsha Oad
 * @date April 7 2026
 * @tutorial T01
 */
public class UpdateSubscriptionController {

    @FXML private TextField nameField;
    @FXML private Label statusLabel;
    @FXML private VBox editFields;
    @FXML private TextField newNameField;
    @FXML private TextField newCostField;
    @FXML private TextField newCategoryField;
    @FXML private ComboBox<String> newRenewalField;
    @FXML private TextField newStartDateField;
    @FXML private TextField newEndDateField;
    @FXML private ComboBox<String> newStatusField;

    //the shared data manager which is set from main controller
    private SubscriptionManager manager;

    //stores the index of subscription found
    private int foundIndex = -1;


    /**
     * Called automatically when window opens - sets up ComboBox dropdown options
     */
    @FXML
    public void initialize() {
        //add options to the billing cycle dropdown
        newRenewalField.getItems().addAll("Monthly", "Yearly");

        //add options to the status dropdown (active, paused, cancelled)
        newStatusField.getItems().addAll("ACTIVE", "PAUSED", "CANCELLED");
}


    /**
     * Called by main controller to pass the shared manager
     * @param manager shared SubscriptionManager
     */

    public void setManager(SubscriptionManager manager) {
        this.manager = manager;
}
    /**
     * Called when user clicks "Find Subscription" button
     * Searches for subscription by name and shows edit fields if found
     */
    @FXML
    private void findSubscription() {
        //get what user put in the name field
        String name = nameField.getText().trim();

        //check if name field is empty
        if (name.isEmpty()) {
            statusLabel.setText("Enter a subscription name:");
            statusLabel.setStyle("-fx-text-fill: rgba(255,0,0,0.64);");
            editFields.setVisible(false);
            return;
        }

        //search for subscription in manager
        foundIndex = manager.findingSubscriptionIndex(name);

        //show an error if nothing found
        if (foundIndex == -1) {
            statusLabel.setText("Subscription '" + name + "' not found.");
            statusLabel.setStyle("-fx-text-fill: rgba(255,0,0,0.64);");
            editFields.setVisible(false);
            return;
        }

        //get the subscription object if found
        Subscription sub = manager.getAllSubscriptions().get(foundIndex);

        //pre-fill all the fields with current values
        newNameField.setText(sub.getName());
        newCostField.setText(String.valueOf(sub.getCost()));
        newCategoryField.setText(sub.getCategory());
        newRenewalField.setValue(sub.getRenewal());
        newStartDateField.setText(sub.getStartDate().format(java.time.format.DateTimeFormatter.ofPattern("MM/DD/YYYY")));
        newEndDateField.setText(sub.getEndDate()!= null? sub.getEndDate().format(java.time.format.DateTimeFormatter. ofPattern("MM/DD/YYYY")) : "");
        newStatusField.setValue(sub.getStatus().toString());

        //show the edit fields
        statusLabel.setText("Subscription found! Edit fields below:");
        statusLabel.setStyle("-fx-text-fill: green;");
        editFields.setVisible(true);
    }

    /**
     * Shows a alerting popup with error message
     * @param message error message
     */

    private void showAlert(String message){
        Alert alert= new Alert(Alert. AlertType.ERROR);
        alert.setTitle("Invalid input.");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}