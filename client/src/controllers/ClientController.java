package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.Client;
import File.FileDetails;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    public Button refreshListButton;
    @FXML
    private Button chooseFileButton;

    @FXML
    private TextField chosenFileDirectoryTextField;

    @FXML
    private Button uploadFileButton;

    @FXML
    private TableView<FileDetails> fileTableView;

    @FXML
    private TableColumn<FileDetails, Integer> fileIdColumn;

    @FXML
    private TableColumn<FileDetails, Long> fileSizeColumn;

    @FXML
    private TableColumn<FileDetails, String> fileNameColumn;

    @FXML
    private MenuItem downloadFileMenuButton;

    @FXML
    private MenuItem renameFileMenuButton;

    @FXML
    private MenuItem deleteFileMenuButton;

    private File fileToUpload;

    private Client client;

    private ObservableList<FileDetails> observableFileList = FXCollections.observableArrayList();

    public void initData(Client client) {
        this.client = client;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        fileNameColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        fileSizeColumn.setCellValueFactory(new PropertyValueFactory<>("fileSize"));
        this.refreshList();
    }

    @FXML
    void chooseFileButtonAction(ActionEvent actionEvent) {
        Stage fileChooserStage = new Stage();
        fileChooserStage.setTitle("FileChooser");
        FileChooser fileChooser = new FileChooser();
        fileToUpload = fileChooser.showOpenDialog(fileChooserStage);

        if (fileToUpload != null) {
            chosenFileDirectoryTextField.setText(fileToUpload.getAbsolutePath());
            uploadFileButton.setDisable(false);
        }

    }

    @FXML
    void deleteFileMenuButtonAction(ActionEvent event) {

        if (checkConfirmation("Are you sure you want to delete?")) {
            FileDetails fileDetails = fileTableView.getSelectionModel().getSelectedItem();
            client.deleteFile(fileDetails.getId());
            this.refreshList();
            popInfoNotification("Delete Successful");
        }
    }

    @FXML
    void downloadFileMenuAction(ActionEvent event) {

        if (checkConfirmation("Are you sure you want to download?")) {
            FileDetails fileDetails = fileTableView.getSelectionModel().getSelectedItem();
            System.out.println("FILE ID -->" + fileDetails.getId());
            client.downloadFile(fileDetails);
            popInfoNotification("Download Successful");
        }
    }

    @FXML
    void renameFileMenuAction(ActionEvent event) {

    }

    @FXML
    void uploadFileButtonAction(ActionEvent event) {
        if (fileToUpload == null) {
            return;
        }
        try {
//            System.out.println(fileToUpload.getAbsolutePath());
//            System.out.println(client.getHostIp() + client.getHostPort());
            chosenFileDirectoryTextField.setText("File Upload On Progress...");
            client.uploadFile(fileToUpload);
            this.refreshList();
            popInfoNotification("Upload Successful");


        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    private boolean checkConfirmation(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        Optional<ButtonType> action = alert.showAndWait();

        return action.get() == ButtonType.OK;
    }

    private void popInfoNotification(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.setContentText(message);
        Optional<ButtonType> action = alert.showAndWait();
    }

    public void refreshListButtonAction(ActionEvent event) {

        client.getFileListFromServer();
        this.refreshList();
    }

    private void refreshList() {
        this.observableFileList = client.getObservableFileList();
        fileTableView.setItems(this.observableFileList);
        chosenFileDirectoryTextField.setText("Please choose a file to upload!");
        fileToUpload = null;
        uploadFileButton.setDisable(true);
    }
}
