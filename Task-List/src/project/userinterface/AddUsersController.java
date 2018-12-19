package project.userinterface;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.stage.Stage;
import project.data.SQLNotificationsDAO;
import project.data.SQLRelationsDAO;
import project.data.SQLTaskDAO;
import project.data.SQLUserDAO;
import project.logic.*;
import project.logic.DTO.NotificationDTO;
import project.userinterface.entities.UIUser;

import java.util.stream.Collectors;

public class AddUsersController
{
    private UsersManipulation mainApp;

    private UserManager userManager = new SimpleUserManager(new SQLNotificationsDAO() , new SQLRelationsDAO() , new SQLTaskDAO() , new SQLUserDAO());
    private TaskManager taskManager = new SimpleTaskManager(new SQLNotificationsDAO() , new SQLRelationsDAO() , new SQLTaskDAO() , new SQLUserDAO());
    private NotificationsManager notificationsManager = new SimpleNotificationsManager(new SQLNotificationsDAO() , new SQLRelationsDAO() , new SQLTaskDAO() , new SQLUserDAO());

    @FXML
    private Button addButton;
    @FXML
    private TableView<UIUser> usersTable;
    @FXML
    private TableColumn<UIUser , String> userColumn;
    @FXML
    private TableColumn<UIUser , Boolean> checkColumn;

    @FXML
    public void initialize()
    {
        usersTable.setEditable(true);
        userColumn.setCellValueFactory((data) -> new SimpleStringProperty(data.getValue().getLogin()));

        checkColumn.setCellFactory((column) ->
        {
            CheckBoxTableCell<UIUser , Boolean> checkBoxTableCell = new CheckBoxTableCell<>();
            return checkBoxTableCell;
        });

        DeleteUsersController.setCheckBoxColumn(checkColumn);

        addButton.setOnAction((action) ->
        {
            usersTable.getItems().forEach((u) ->
            {
                if(u.getChosen())
                {
                    if(u.getAccessType().equals("Free access"))
                    {
                        userManager.addUserToTask(this.mainApp.getTask().toDTO() , u.toDTO());
                    }
                    else
                    {
                        notificationsManager.addNotification(new NotificationDTO(this.mainApp.getTask().toDTO().getId() ,
                                this.mainApp.getTask().toDTO().getCreatorLogin() ,
                                u.toDTO().getLogin() , "Requested"));
                    }
                }
            });
            ((Stage) addButton.getScene().getWindow()).close();
            this.mainApp.getHomeController().load();
        });
    }

    public void setMainApp(UsersManipulation mainApp)
    {
        this.mainApp = mainApp;
        this.usersTable.setItems(FXCollections.observableArrayList(userManager
                .getPotentialUsers(this.mainApp.getTask().toDTO()).stream()
                .map((u) -> new UIUser(u)).collect(Collectors.toList())));
    }
}
