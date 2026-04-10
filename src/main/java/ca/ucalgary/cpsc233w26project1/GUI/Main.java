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
        //Arguments to program
        List<String> args = getParameters().getRaw();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("SubscriptionMain.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        SubscriptionMainController controller = fxmlLoader.getController();
        controller.initData(args);
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}
