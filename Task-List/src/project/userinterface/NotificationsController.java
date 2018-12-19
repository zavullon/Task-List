package project.userinterface;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import project.data.SQLNotificationsDAO;
import project.data.SQLRelationsDAO;
import project.data.SQLTaskDAO;
import project.data.SQLUserDAO;
import project.logic.*;
import project.logic.DTO.NotificationDTO;
import project.userinterface.entities.UINotification;
import project.userinterface.entities.UITask;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class NotificationsController
{
    private MainWindow mainApp;
    private UserManager userManager = new SimpleUserManager(new SQLNotificationsDAO() , new SQLRelationsDAO() , new SQLTaskDAO() , new SQLUserDAO());
    private TaskManager taskManager = new SimpleTaskManager(new SQLNotificationsDAO() , new SQLRelationsDAO() , new SQLTaskDAO() , new SQLUserDAO());
    private NotificationsManager notificationsManager = new SimpleNotificationsManager(new SQLNotificationsDAO() , new SQLRelationsDAO() , new SQLTaskDAO() , new SQLUserDAO());
    private List<Integer> acceptedNotifications;
    private List<Integer> deniedNotifications;
    private List<Integer> okNotifications;

    @FXML
    private TableView<UINotification> notificationTableView;
    @FXML
    private TableColumn<UINotification, String> taskTableColumn;
    @FXML
    private TableColumn<UINotification, String> dateTableColumn;
    @FXML
    private TableColumn<UINotification, String> userTableColumn;
    @FXML
    private TableColumn<UINotification, String> messageColumn;
    @FXML
    private TableColumn<UINotification, String> answerTableColumn;
    @FXML
    private Button backButton;
    @FXML
    private Button applyButton;


    @FXML
    public void initialize()
    {
        acceptedNotifications = new LinkedList<>();
        deniedNotifications = new LinkedList<>();
        okNotifications = new LinkedList<>();

        applyButton.setDisable(true);

        backButton.setOnAction((action) ->
        {
            okNotifications.forEach((i) ->
            {
                notificationsManager.deleteNotification(new NotificationDTO(notificationTableView.getItems().get(i).getTaskID() ,
                        taskManager.getById(notificationTableView.getItems().get(i).getTaskID()).getCreatorLogin() ,
                        notificationTableView.getItems().get(i).getReciever() ,
                        notificationTableView.getItems().get(i).getMessage()));
            });
            try
            {
                this.mainApp.homeStart(this.mainApp.getPrimaryStage());
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        });

        taskTableColumn.setCellValueFactory((data) -> new SimpleStringProperty(new UITask(taskManager.getById(data.getValue().getTaskID())).getNote()));
        dateTableColumn.setCellValueFactory((data) -> new SimpleStringProperty(new UITask(taskManager.getById(data.getValue().getTaskID())).getDate().toString()));
        userTableColumn.setCellValueFactory((data) -> new SimpleStringProperty(new UITask(taskManager.getById(data.getValue().getTaskID())).getCreatorLogin()));
        messageColumn.setCellValueFactory((data) -> new SimpleStringProperty(data.getValue().getMessage()));
        answerTableColumn.setCellValueFactory((data) -> new SimpleStringProperty(data.getValue().getMessage()));

        answerTableColumn.setCellFactory((column) ->
        {
            return new TableCell<UINotification, String>()
            {

                RadioButton accept = new RadioButton("accept");
                RadioButton deny = new RadioButton("deny");
                ToggleGroup toggleGroup = new ToggleGroup();

                @Override
                public void updateItem(String item , boolean empty)
                {
                    super.updateItem(item , empty);
                    if(empty)
                    {
                        setGraphic(null);
                        setText(null);
                    }
                    else if(item.equals("Requested"))
                    {
                        accept.setToggleGroup(toggleGroup);
                        deny.setToggleGroup(toggleGroup);
                        accept.setOnAction((action) ->
                        {
                            applyButton.setDisable(false);
                            deniedNotifications.removeIf((i) -> acceptedNotifications.contains(this.getTableRow().getIndex()));
                            acceptedNotifications.add(this.getTableRow().getIndex());
                        });
                        deny.setOnAction((action) ->
                        {
                            applyButton.setDisable(false);
                            acceptedNotifications.removeIf((i) -> deniedNotifications.contains(this.getTableRow().getIndex()));
                            deniedNotifications.add(this.getTableRow().getIndex());
                        });
                        HBox box = new HBox(accept , deny);
                        box.setSpacing(10);
                        setGraphic(box);
                    }
                    else
                    {
                        setGraphic(null);
                        setText(null);
                        okNotifications.add(this.getTableRow().getIndex());
                    }
                }
            };
        });

        applyButton.setOnAction((action) ->
        {
            acceptedNotifications.forEach((i) ->
            {
                userManager.addUserToTask(taskManager.getById(notificationTableView.getItems().get(i).getTaskID()) ,
                        userManager.getUserByLogin(notificationTableView.getItems().get(i).getReciever()));
                notificationsManager.deleteNotification(new NotificationDTO(notificationTableView.getItems().get(i).getTaskID() ,
                        taskManager.getById(notificationTableView.getItems().get(i).getTaskID()).getCreatorLogin() ,
                        notificationTableView.getItems().get(i).getReciever() ,
                        "Requested"));
                notificationsManager.addNotification(new NotificationDTO(notificationTableView.getItems().get(i).getTaskID() ,
                        notificationTableView.getItems().get(i).getReciever() ,
                        taskManager.getById(notificationTableView.getItems().get(i).getTaskID()).getCreatorLogin() ,
                        "Accepted"));
            });
            deniedNotifications.forEach((i) ->
            {
                notificationsManager.deleteNotification(new NotificationDTO(notificationTableView.getItems().get(i).getTaskID() ,
                        taskManager.getById(notificationTableView.getItems().get(i).getTaskID()).getCreatorLogin() ,
                        notificationTableView.getItems().get(i).getReciever() ,
                        "Requested"));
                notificationsManager.addNotification(new NotificationDTO(notificationTableView.getItems().get(i).getTaskID() ,
                        notificationTableView.getItems().get(i).getReciever() ,
                        taskManager.getById(notificationTableView.getItems().get(i).getTaskID()).getCreatorLogin() ,
                        "Denied"));
            });
            load();
        });
    }

    public void setMainApp(MainWindow mainApp)
    {
        this.mainApp = mainApp;
        load();
    }

    public void load()
    {
        notificationTableView.setItems(FXCollections.observableArrayList(notificationsManager.getUserNotification(this.mainApp.getUser().toDTO())
                .stream().map(UINotification::new).collect(Collectors.toList())));
    }
}
