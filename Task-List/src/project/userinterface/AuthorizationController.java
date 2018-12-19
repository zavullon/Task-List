package project.userinterface;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import project.data.SQLNotificationsDAO;
import project.data.SQLRelationsDAO;
import project.data.SQLTaskDAO;
import project.data.SQLUserDAO;
import project.logic.*;
import project.logic.DTO.UserDTO;

import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import project.userinterface.entities.UIUser;

public class AuthorizationController
{
    private UserManager userManager = new SimpleUserManager(new SQLNotificationsDAO() , new SQLRelationsDAO() , new SQLTaskDAO() , new SQLUserDAO());
    private TaskManager taskManager = new SimpleTaskManager(new SQLNotificationsDAO() , new SQLRelationsDAO() , new SQLTaskDAO() , new SQLUserDAO());
    private NotificationsManager notificationsManager = new SimpleNotificationsManager(new SQLNotificationsDAO() , new SQLRelationsDAO() , new SQLTaskDAO() , new SQLUserDAO());

    private Authorization mainApp;

    @FXML
    private TextField logField;
    @FXML
    private PasswordField passField;
    @FXML
    private Button logButton;
    @FXML
    private Button signButton;
    @FXML
    private Text warningText;

    @FXML
    private void initialize()
    {
        logButton.setOnAction((event) ->
        {
            UIUser logger = new UIUser(userManager.getUser(new UserDTO(logField.getText() , passField.getText())));
            if(logger.isNull())
            {
                warningText.setText("wrong username or password");
                warningText.setFill(Color.RED);
            } else
            {
                ((Stage) logButton.getScene().getWindow()).close();
                try
                {
                    (new MainWindow(logger)).start(new Stage());
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        signButton.setOnAction((event) ->
        {
            try
            {
                Stage stage = new Stage();
                this.mainApp.getPage().disableProperty().bind(stage.showingProperty());
                (new Registration()).start(stage);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        });
    }

    public void setMainApp(Authorization mainApp)
    {
        this.mainApp = mainApp;
    }
}
