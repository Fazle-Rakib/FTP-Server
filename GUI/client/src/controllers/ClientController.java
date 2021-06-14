package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    @FXML
    private Button chooseFileButton;

    @FXML
    private TextField chosenFileDirectoryTextField;

    @FXML
    private Button uploadFileButton;

    @FXML
    private TableView<?> fileTableView;

    @FXML
    private MenuItem downloadFileMenuButton;

    @FXML
    private MenuItem renameFileMenuButton;

    @FXML
    private MenuItem deleteFileMenuButton;

    private File selectedFile;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void chooseFileButtonAction(ActionEvent actionEvent) {
        Stage fileChooserStage = new Stage();
        fileChooserStage.setTitle("FileChooser");
        FileChooser fileChooser = new FileChooser();
        selectedFile = fileChooser.showOpenDialog(fileChooserStage);

        if (selectedFile != null) {
            chosenFileDirectoryTextField.setText(selectedFile.getAbsolutePath());
            uploadFileButton.setDisable(false);
        }

    }

    @FXML
    void deleteFileMenuButtonAction(ActionEvent event) {

    }

    @FXML
    void downloadFileMenuAction(ActionEvent event) {

    }

    @FXML
    void renameFileMenuAction(ActionEvent event) {

    }

    @FXML
    void uploadFileButtonAction(ActionEvent event) {
        if(selectedFile == null) {
            return;
        }
        System.out.println(selectedFile.getAbsolutePath());
    }
}
