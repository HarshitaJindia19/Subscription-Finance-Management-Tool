import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * HighExpenseBillsController.java - shows active subscriptions above a cost threshold
 * @author Ramsha Oad
 * @date April 8 2026
 * @tutorial T01
 */
public class HighExpenseBillsController {

    //connects to FXML elements
    @FXML private TextField thresholdField;
    @FXML private Label statusLabel;
    @FXML private TableView<Subscription> resultsTable;

    //connects to each column
    @FXML private TableColumn<Subscription, String> nameCol;
    @FXML private TableColumn<Subscription, String> costCol;
    @FXML private TableColumn<Subscription, String> categoryCol;
    @FXML private TableColumn<Subscription, String> renewalCol;
    @FXML private TableColumn<Subscription, String> statusCol;

    private SubscriptionManager manager;


    /**
     * Called by main controller to pass in the shared manager
     * @param manager the shared SubscriptionManager
     */

    public void setManager(SubscriptionManager manager) {
        this.manager =manager;
    }
}