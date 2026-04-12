package ca.ucalgary.cpsc233w26project1.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("started");

        stage.setTitle("[Subscription Manager]");
        List<String> args = getParameters().getRaw();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("SubscriptionMain.fxml"));
        Scene scene = new Scene(fxmlLoader.load()); // open up the scene (main controller)


        SubscriptionMainController controller = fxmlLoader.getController();
        System.out.println("Controller: " + controller);
        controller.initData(args); // pass args
        System.out.println("initData called");

        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
