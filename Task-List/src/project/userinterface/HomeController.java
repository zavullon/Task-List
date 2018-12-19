package project.userinterface;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import javafx.util.StringConverter;
import project.data.SQLNotificationsDAO;
import project.data.SQLRelationsDAO;
import project.data.SQLTaskDAO;
import project.data.SQLUserDAO;
import project.logic.*;
import project.logic.DTO.TaskDTO;
import project.userinterface.entities.UITask;
import project.util.Date;
import project.util.Request;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class HomeController
{
    private MainWindow mainApp;

    private UserManager userManager = new SimpleUserManager(new SQLNotificationsDAO() , new SQLRelationsDAO() , new SQLTaskDAO() , new SQLUserDAO());
    private TaskManager taskManager = new SimpleTaskManager(new SQLNotificationsDAO() , new SQLRelationsDAO() , new SQLTaskDAO() , new SQLUserDAO());
    private NotificationsManager notificationsManager = new SimpleNotificationsManager(new SQLNotificationsDAO() , new SQLRelationsDAO() , new SQLTaskDAO() , new SQLUserDAO());

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm");

    private List<Pair<Integer, UITask>> committedChanges = new ArrayList<>();
    private List<Integer> newRows = new LinkedList<>();
    private Request request;

    @FXML
    private Button addButton;
    @FXML
    private Button applyButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button notificationsButton;
    @FXML
    private TableView<UITask> taskTable;
    @FXML
    private TableColumn<UITask, String> Subject;
    @FXML
    private TableColumn<UITask, String> Date;
    @FXML
    private TableColumn<UITask, String> Type;
    @FXML
    private TableColumn<UITask, String> Access;
    @FXML
    private TableColumn<UITask, String> Creator;
    @FXML
    private DatePicker fromDatePicker;
    @FXML
    private DatePicker toDatePicker;
    @FXML
    private DatePicker deleteDatePicker;
    @FXML
    private ProgressBar progressBar;

    @FXML
    private void initialize()
    {
        tableInit();
        setButtons();
        setDatePickers();
    }

    private void tableInit()
    {
        taskTable.setEditable(true);
        contextMenuInit();
        setCellValueFactories();
        setSubjectCellFactory();
        setDateCellFactory();
        setTypeCellFactory();
        setAccessCellFactory();
    }

    public void setMainApp(MainWindow mainApp)
    {
        this.mainApp = mainApp;
        load();
    }

    public void load()
    {
        setRequest();
        new Thread()
        {
            public void run()
            {
                changeStatus(true);
                List<UITask> tasksList = taskManager.getTasksByUser(mainApp.getUser().toDTO() , request)
                        .stream()
                        .map(UITask::new)
                        .collect(Collectors.toList());
                taskTable.setItems(FXCollections.observableArrayList(tasksList));
                progressBar.setProgress(1);
                try
                {
                    sleep(1000);
                }
                catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
                changeStatus(false);
                progressBar.setDisable(true);
            }
        }.start();
    }

    private void changeStatus(boolean status)
    {
        this.notificationsButton.setDisable(status);
        this.applyButton.setDisable(status);
        this.deleteButton.setDisable(status);
        this.addButton.setDisable(status);
        this.fromDatePicker.setDisable(status);
        this.toDatePicker.setDisable(status);
        this.deleteDatePicker.setDisable(status);
    }

    private boolean isSet(UITask task)
    {
        return task.getDate().toString() != "" && task.getNote() != null && task.getType() != "" && task.getAccess() != null && task.getCreatorLogin() != "";
    }

    private void contextMenuInit()
    {
        taskTable.setRowFactory((row) ->
        {
            TableRow<UITask> taskTableRow = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem addUsers = new MenuItem("add users");
            MenuItem deleteUsers = new MenuItem("delete users");
            MenuItem deleteTask = new MenuItem("delete task");
            deleteTask.setOnAction((action) ->
            {
                taskTable.getItems().remove(taskTableRow.getItem());
                if(!newRows.contains(taskTableRow.getIndex()))
                {
                    taskManager.deleteTask(this.mainApp.getUser().toDTO() , taskTableRow.getItem().toDTO());
                }
            });
            addUsers.setOnAction((action) ->
            {
                try
                {
                    Stage stage = new Stage();
                    this.mainApp.getPage().disableProperty().bind(stage.showingProperty());
                    (new UsersManipulation(taskTableRow.getItem() , this)).startAddUsers(stage);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            });
            deleteUsers.setOnAction((action) ->
            {
                try
                {
                    Stage stage = new Stage();
                    this.mainApp.getPage().disableProperty().bind(stage.showingProperty());
                    (new UsersManipulation(taskTableRow.getItem() , this)).startDeleteUsers(stage);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            });
            contextMenu.getItems().addAll(addUsers , deleteUsers , deleteTask);
            taskTableRow.contextMenuProperty().bind(Bindings.when(taskTableRow.emptyProperty()).then((ContextMenu) null).otherwise(contextMenu));
            return taskTableRow;
        });
    }

    private void setCellValueFactories()
    {
        Subject.setCellValueFactory((data) -> new SimpleStringProperty(data.getValue().getNote()));
        Date.setCellValueFactory((data) -> new SimpleStringProperty(data.getValue().getDate().toString()));
        Type.setCellValueFactory((data) -> new SimpleStringProperty(data.getValue().getType()));
        Access.setCellValueFactory((data) -> new SimpleStringProperty(data.getValue().getAccess()));
        Creator.setCellValueFactory((data) -> new SimpleStringProperty(data.getValue().getCreatorLogin()));
    }

    private void setSubjectCellFactory()
    {
        Subject.setCellFactory((column) ->
        {
            return new TableCell<UITask, String>()
            {
                @Override
                protected void updateItem(String item , boolean empty)
                {
                    super.updateItem(item , empty);
                    if(empty)
                    {
                        setGraphic(null);
                        setText(null);
                    }
                    else if(getTableView().getItems().get(getIndex()).getCreatorLogin().equals(mainApp.getUser().getLogin()))
                    {
                        applyButton.setDisable(false);
                        TextField textField = new TextField();
                        textField.setText(item);
                        textField.setOnAction((event ->
                        {
                            if(!hasKey(getIndex()))
                            {
                                committedChanges.add(new Pair<>(getIndex() , new UITask(getTableView().getItems().get(getIndex()))));
                            }
                            getTableView().getItems().get(getIndex()).setNote(textField.getText());
                        }));
                        setGraphic(textField);
                    }
                    else
                    {
                        applyButton.setDisable(false);
                        setText(item);
                    }
                }
            };
        });
    }

    private void setDateCellFactory()
    {
        Date.setCellFactory((column) ->
        {
            return new TableCell<UITask, String>()
            {
                @Override
                protected void updateItem(String item , boolean empty)
                {
                    super.updateItem(item , empty);
                    if(empty)
                    {
                        setGraphic(null);
                        setText(null);
                    }
                    else if(getTableView().getItems().get(getIndex()).getCreatorLogin().equals(mainApp.getUser().getLogin()))
                    {
                        applyButton.setDisable(false);
                        TextField textField = new TextField();
                        textField.setText(project.util.Date.checkDate(item) ? item : null);
                        textField.setOnAction((event ->
                        {
                            if(!hasKey(getIndex()))
                            {
                                committedChanges.add(new Pair<>(getIndex() , new UITask(getTableView().getItems().get(getIndex()))));
                            }
                            getTableView().getItems().get(getIndex()).setDate(project.util.Date.checkDate(item) ? new Date(item) : null);
                        }));
                        setGraphic(textField);
                    }
                    else
                    {
                        setText(project.util.Date.checkDate(item) ? item : null);
                    }
                }
            };
        });
    }

    private void setTypeCellFactory()
    {
        Type.setCellFactory((column) ->
        {
            return new TableCell<UITask, String>()
            {
                @Override
                protected void updateItem(String item , boolean empty)
                {
                    super.updateItem(item , empty);
                    if(empty)
                    {
                        setGraphic(null);
                        setText(null);
                    }
                    else if(getTableView().getItems().get(getIndex()).getCreatorLogin().equals(mainApp.getUser().getLogin()))
                    {
                        applyButton.setDisable(false);
                        ComboBox<String> comboBox = new ComboBox<>();
                        comboBox.setItems(FXCollections.observableArrayList("Work" , "Personal"));
                        comboBox.getSelectionModel().select(item);
                        comboBox.setPrefWidth(150);
                        comboBox.setOnAction((event ->
                        {
                            if(!hasKey(getIndex()))
                            {
                                committedChanges.add(new Pair<>(getIndex() , new UITask(getTableView().getItems().get(getIndex()))));
                            }
                            getTableView().getItems().get(getIndex()).setType(comboBox.getSelectionModel().getSelectedItem());
                        }));
                        setGraphic(comboBox);
                    }
                    else
                    {
                        applyButton.setDisable(false);
                        setText(item);
                    }
                }
            };
        });
    }

    private void setAccessCellFactory()
    {
        Access.setCellFactory((column) ->
        {
            return new TableCell<UITask, String>()
            {
                @Override
                protected void updateItem(String item , boolean empty)
                {
                    super.updateItem(item , empty);
                    if(empty)
                    {
                        setGraphic(null);
                        setText(null);
                    }
                    else if(getTableView().getItems().get(getIndex()).getCreatorLogin().equals(mainApp.getUser().getLogin()))
                    {
                        applyButton.setDisable(false);
                        ComboBox<String> comboBox = new ComboBox<>();
                        comboBox.setItems(FXCollections.observableArrayList("Individual" , "Team"));
                        comboBox.getSelectionModel().select(item);
                        comboBox.setPrefWidth(150);
                        comboBox.setOnAction((event ->
                        {
                            if(!hasKey(getIndex()))
                            {
                                committedChanges.add(new Pair<>(getIndex() , new UITask(getTableView().getItems().get(getIndex()))));
                            }
                            getTableView().getItems().get(getIndex()).setAccess(comboBox.getSelectionModel().getSelectedItem());
                        }));
                        setGraphic(comboBox);
                    }
                    else
                    {
                        applyButton.setDisable(false);
                        setText(item);
                    }
                }
            };
        });
    }

    private void setButtons()
    {
        applyButton.setDisable(true);

        applyButton.setOnAction((event) ->
        {
            if(committedChanges.size() > 0)
            {
                committedChanges.forEach((p) ->
                {
                    if(!newRows.contains(p.getKey()) && isSet(taskTable.getItems().get(p.getKey())))
                    {
                        taskManager.updateTask(p.getValue().toDTO() , taskTable.getItems().get(p.getKey()).toDTO());
                    }
                });
                newRows.forEach((i) ->
                {
                    if(isSet(taskTable.getItems().get(i)))
                    {
                        taskManager.addTask(taskTable.getItems().get(i).toDTO());
                    }
                });
            }
            else
            {
                try
                {
                    new Error("ERROR").start(new Stage());
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        addButton.setOnAction((action) ->
        {
            taskTable.getItems().add(new UITask(this.mainApp.getUser()));
            newRows.add(taskTable.getItems().size() - 1);
        });

        notificationsButton.setOnAction((action) ->
        {
            try
            {
                this.mainApp.notificationsStart(this.mainApp.getPrimaryStage());
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        });

        deleteButton.setOnAction((action) ->
        {
            if(deleteDatePicker.getValue() != null)
            {
                taskManager.deleteUntil(this.mainApp.getUser().toDTO() , new Date(deleteDatePicker.getValue() + " 00:00"));
            }
            load();
        });
    }

    private void setDatePickers()
    {
        fromDatePicker.setOnAction((action) ->
        {
            if(toDatePicker.getValue() != null)
            {
                taskTable.setItems(FXCollections.observableArrayList(taskManager
                        .getTasksBetween(this.mainApp.getUser().toDTO() ,
                                new Date(fromDatePicker.getValue() + " 00:00") ,
                                new Date(toDatePicker.getValue() + " 00:00"))
                        .stream()
                        .map((t) -> new UITask(t))
                        .collect(Collectors.toList())));
            }
        });

        toDatePicker.setOnAction((action) ->
        {
            if(toDatePicker.getValue() != null)
            {
                taskTable.setItems(FXCollections.observableArrayList(taskManager
                        .getTasksBetween(this.mainApp.getUser().toDTO() ,
                                new Date(fromDatePicker.getValue() + " 00:00") ,
                                new Date(toDatePicker.getValue() + " 00:00"))
                        .stream()
                        .map((t) -> new UITask(t))
                        .collect(Collectors.toList())));
            }
        });
    }

    public Request getRequest()
    {
        return this.request;
    }

    private boolean hasKey(Integer key)
    {
        for(Pair<Integer, UITask> i : committedChanges)
        {
            if(i.getKey() == key)
            {
                return true;
            }
        }
        return false;
    }

    private void setRequest()
    {
        progressBar.setDisable(false);
        progressBar.setProgress(0);
        this.request = new Request("added" , "done");
        request.subscribe("added" , () ->
        {
            this.progressBar.setProgress(0.4);
        });
        request.subscribe("done" , () ->
        {
            this.progressBar.setProgress(0.7);
            this.request.unsubscribe("added");
        });
    }
}
