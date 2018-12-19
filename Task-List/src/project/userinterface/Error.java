package project.userinterface;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Error extends Application
{
    private String errorText;

    public Error(String errorText)
    {
        this.errorText = errorText;
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        primaryStage.setTitle("ERROR");
        FXMLLoader loader = new FXMLLoader(Error.class.getResource("/resources/errorWindow.fxml"));
        AnchorPane page = loader.load();
        ErrorController controller = loader.getController();
        controller.setMainApp(this);
        Scene scene = new Scene(page , 200 , 100);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public String getErrorText()
    {
        return this.errorText;
    }
}
