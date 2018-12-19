package project.userinterface;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Authorization extends Application
{
    private AnchorPane page;

    @Override
    public void start(Stage primaryStage) throws IOException
    {
        primaryStage.setTitle("Welcome");
        FXMLLoader loader = new FXMLLoader(Authorization.class.getResource("/resources/authorization.fxml"));
        this.page = loader.load();
        AuthorizationController controller = loader.getController();
        controller.setMainApp(this);
        Scene scene = new Scene(page , 600 , 450);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public AnchorPane getPage()
    {
        return this.page;
    }

    public static void main(String[] args)
    {
        //for(Integer i = 0 ; i < 1000 ; i++)
        //{
            //new SQLManager().addTask(new TaskDTO(i , new Date("2018-12-09 00:00") , i.toString() , "Work" , "Team" , "admin"));
        //}
        launch(args);
        //(new SQLRelationsDAO()).getTaskIDsByUser(new UserDTO("admin" , "Free access") , new Request<>("added" , "done")).forEach(System.out::println);
    }
}
