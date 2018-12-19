package project.userinterface;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ErrorController
{
    private Error mainApp;

    @FXML
    private Button okButton;
    @FXML
    private Text errorText;

    @FXML
    public void initialize()
    {
        errorText.setText("ERROR! Check input data");
        okButton.setOnAction((action) ->
        {
            ((Stage) okButton.getScene().getWindow()).close();
        });
    }

    public void setMainApp(Error app)
    {
        this.mainApp = app;
    }
}
