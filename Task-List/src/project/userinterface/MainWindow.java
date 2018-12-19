package project.userinterface;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import project.data.SQLNotificationsDAO;
import project.data.SQLRelationsDAO;
import project.data.SQLTaskDAO;
import project.data.SQLUserDAO;
import project.logic.*;
import project.userinterface.entities.UITask;
import project.userinterface.entities.UIUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class MainWindow extends Application
{
    private UIUser user;

    private UserManager userManager = new SimpleUserManager(new SQLNotificationsDAO() , new SQLRelationsDAO() , new SQLTaskDAO() , new SQLUserDAO());
    private TaskManager taskManager = new SimpleTaskManager(new SQLNotificationsDAO() , new SQLRelationsDAO() , new SQLTaskDAO() , new SQLUserDAO());
    private NotificationsManager notificationsManager = new SimpleNotificationsManager(new SQLNotificationsDAO() , new SQLRelationsDAO() , new SQLTaskDAO() , new SQLUserDAO());

    private AnchorPane page;

    private Stage primaryStage;

    private FXMLLoader loader;

    private HomeController controller;

    private MainWindow mainWindow;

    MainWindow(UIUser logger)
    {
        this.user = logger;
    }

    public UIUser getUser()
    {
        return this.user;
    }

    @Override
    public void start(Stage primaryStage) throws IOException
    {
        this.primaryStage = primaryStage;
        homeStart(primaryStage);
    }

    public void homeStart(Stage primaryStage) throws IOException
    {
        primaryStage.setTitle("MainWindow");
        this.loader = new FXMLLoader(MainWindow.class.getResource("/resources/home.fxml"));
        this.page = loader.load();
        this.controller = loader.getController();
        controller.setMainApp(this);
        Scene scene = new Scene(page , 800 , 600);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void notificationsStart(Stage primaryStage) throws IOException
    {
        primaryStage.setTitle("Notifications");
        this.loader = new FXMLLoader(MainWindow.class.getResource("/resources/notifications.fxml"));
        this.page = loader.load();
        NotificationsController controller = loader.getController();
        this.mainWindow = this;
        controller.setMainApp(mainWindow);
        Scene scene = new Scene(page , 800 , 600);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public Stage getPrimaryStage()
    {
        return this.primaryStage;
    }

    public AnchorPane getPage()
    {
        return this.page;
    }

    public ObservableList<UITask> getList()
    {
        return FXCollections.observableArrayList(taskManager.getTasksByUser(user.toDTO() , controller.getRequest())
                .stream()
                .map(UITask::new)
                .collect(Collectors.toList()));
    }
}
