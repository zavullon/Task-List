package project.userinterface;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.stage.Stage;
import project.data.SQLNotificationsDAO;
import project.data.SQLRelationsDAO;
import project.data.SQLTaskDAO;
import project.data.SQLUserDAO;
import project.logic.*;
import project.logic.DTO.UserDTO;
import project.userinterface.entities.UIUser;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class DeleteUsersController
{
    private UsersManipulation mainApp;

    private UserManager userManager = new SimpleUserManager(new SQLNotificationsDAO() , new SQLRelationsDAO() , new SQLTaskDAO() , new SQLUserDAO());
    private TaskManager taskManager = new SimpleTaskManager(new SQLNotificationsDAO() , new SQLRelationsDAO() , new SQLTaskDAO() , new SQLUserDAO());
    private NotificationsManager notificationsManager = new SimpleNotificationsManager(new SQLNotificationsDAO() , new SQLRelationsDAO() , new SQLTaskDAO() , new SQLUserDAO());

    @FXML
    private Button deleteButton;
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

        setCheckBoxColumn(checkColumn);

        deleteButton.setOnAction((action) ->
        {
            usersTable.getItems().forEach((u) ->
            {
                if(u.getChosen())
                {
                    userManager.deleteUserFromTask(this.mainApp.getTask().toDTO() , u.toDTO());
                }
            });
            ((Stage) deleteButton.getScene().getWindow()).close();
            this.mainApp.getHomeController().load();
        });
    }

    static void setCheckBoxColumn(TableColumn<UIUser, Boolean> checkColumn)
    {
        checkColumn.setCellValueFactory((data) ->
        {
            UIUser cellValue = data.getValue();
            BooleanProperty property =  new SimpleBooleanProperty(data.getValue().getChosen());
            property.addListener((observable , newValue , oldValue) -> cellValue.setChosen(!cellValue.getChosen()));
            return property;
        });
    }

    public void setMainApp(UsersManipulation mainApp)
    {
        this.mainApp = mainApp;
        this.usersTable.setItems(FXCollections.observableArrayList(userManager.getUsersByTask(this.mainApp.getTask().toDTO())
                .stream().map((u) -> new UIUser(u)).collect(Collectors.toList())));
    }
}
