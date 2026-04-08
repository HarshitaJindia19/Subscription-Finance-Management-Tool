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
}