package project.userinterface;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Registration extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        primaryStage.setTitle("registration");
        FXMLLoader loader = new FXMLLoader(Registration.class.getResource("/resources/registration.fxml"));
        AnchorPane page = loader.load();
        RegistrationController controller = loader.getController();
        controller.setMainApp(this);
        Scene scene = new Scene(page , 270 , 150);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
