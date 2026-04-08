import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * UpcomingBillsController.java - shows subscriptions renewing in next 7 days
 * @author Ramsha Oad
 * @date April 8 2026
 * @tutorial T01
 */
public class UpcomingBillsController {

    //connects to the table in FXML
    @FXML private TableView<Subscription> billsTable;

    //connects to each column in FXML
    @FXML private TableColumn<Subscription, String> nameCol;
    @FXML private TableColumn<Subscription, String> costCol;
    @FXML private TableColumn<Subscription, String> categoryCol;
    @FXML private TableColumn<Subscription, String> renewalCol;
    @FXML private TableColumn<Subscription, String> startDateCol;
    @FXML private TableColumn<Subscription, String> statusCol;

    //shows message when no upcoming bills
    @FXML private Label emptyLabel;

    private SubscriptionManager manager;


    /**
     * Called by main controller to pass in the shared manager
     * @param manager shared SubscriptionManager
     */

    public void setManager(SubscriptionManager manager) {
        this.manager = manager;
    }
}