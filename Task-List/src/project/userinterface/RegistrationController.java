package project.userinterface;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import project.data.SQLNotificationsDAO;
import project.data.SQLRelationsDAO;
import project.data.SQLTaskDAO;
import project.data.SQLUserDAO;
import project.logic.*;
import project.logic.DTO.UserDTO;

public class RegistrationController
{
    private UserManager userManager = new SimpleUserManager(new SQLNotificationsDAO() , new SQLRelationsDAO() , new SQLTaskDAO() , new SQLUserDAO());
    private TaskManager taskManager = new SimpleTaskManager(new SQLNotificationsDAO() , new SQLRelationsDAO() , new SQLTaskDAO() , new SQLUserDAO());
    private NotificationsManager notificationsManager = new SimpleNotificationsManager(new SQLNotificationsDAO() , new SQLRelationsDAO() , new SQLTaskDAO() , new SQLUserDAO());

    private Registration mainApp;

    @FXML
    private TextField logField;
    @FXML
    private PasswordField passField;
    @FXML
    private ComboBox<String> accessTypeCBox;
    @FXML
    private Button signUpButton;
    @FXML
    private Text warningText;

    @FXML
    private void initialize()
    {
        accessTypeCBox.getItems().addAll("Free access" , "Requested access");
        signUpButton.setOnAction((event) ->
        {
            if(userManager.addUser(new UserDTO(logField.getText() , passField.getText() , accessTypeCBox.getValue())))
            {
                ((Stage) signUpButton.getScene().getWindow()).close();
            }
            else
            {
                warningText.setText("!");
                warningText.setFill(Color.RED);
                logField.setText("");
                passField.setText("");
            }
        });
    }

    public void setMainApp(Registration mainApp)
    {
        this.mainApp = mainApp;
    }
}
