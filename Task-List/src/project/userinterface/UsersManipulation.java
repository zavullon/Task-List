package project.userinterface;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import project.userinterface.entities.UITask;

public class UsersManipulation extends Application
{
    private UITask task;
    private HomeController homeController;
    private Stage primaryStage;

    public UsersManipulation(UITask task , HomeController homeController)
    {
        this.task = task;
        this.homeController = homeController;
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        this.primaryStage = primaryStage;
    }

    public void startAddUsers(Stage primaryStage) throws Exception
    {
        primaryStage.setTitle("Add users");
        FXMLLoader loader = new FXMLLoader(UsersManipulation.class.getResource("/resources/addUsers.fxml"));
        AnchorPane page = loader.load();
        AddUsersController controller = loader.getController();
        controller.setMainApp(this);
        Scene scene = new Scene(page , 200 , 400);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void startDeleteUsers(Stage primaryStage) throws Exception
    {
        primaryStage.setTitle("Delete users");
        FXMLLoader loader = new FXMLLoader(UsersManipulation.class.getResource("/resources/deleteUsers.fxml"));
        AnchorPane page = loader.load();
        DeleteUsersController controller = loader.getController();
        controller.setMainApp(this);
        Scene scene = new Scene(page , 200 , 400);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public UITask getTask()
    {
        return this.task;
    }

    public HomeController getHomeController()
    {
        return this.homeController;
    }
}
