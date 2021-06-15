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
import File.FileDetails;
import server.Server;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerController implements Initializable {
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

    private Server server;

    private ObservableList<FileDetails> observableFileList = FXCollections.observableArrayList();

    public void initData(Server server) {
        this.server = server;
//        this.fileList = server.getFileList();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        fileNameColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        fileSizeColumn.setCellValueFactory(new PropertyValueFactory<>("fileSize"));
//        this.observableFileList = server.getObservableFileList();
//        fileTableView.setItems(this.observableFileList);
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
        FileDetails fileDetails = fileTableView.getSelectionModel().getSelectedItem();
        System.out.println(fileDetails.getFileName());
//        server.deleteFile(fileDetails.getId());
//        this.observableFileList.removeAll(this.observableFileList);
//        FXCollections.copy(this.observableFileList, server.getObservableFileList());
//        this.observableFileList = client.getObservableFileList();
//        fileTableView.setItems(client.getObservableFileList());
//        fileTableView.refresh();
    }

//    @FXML
//    void downloadFileMenuAction(ActionEvent event) {
////        event.getTarget()
//        FileDetails fileDetails = fileTableView.getSelectionModel().getSelectedItem();
//        System.out.println("FILE ID -->" + fileDetails.getId());
//        server.downloadFile(fileDetails.getId());
////        fileTableView
////        fileTableView.setItems(client.getObservableFileList());
////        fileTableView.refresh();
////        System.out.println(fileDetails.getFileName() + "    " + fileDetails.getId());
//    }

//    @FXML
//    void renameFileMenuAction(ActionEvent event) {
//
//    }

//    @FXML
//    void uploadFileButtonAction(ActionEvent event) {
//        if (fileToUpload == null) {
//            return;
//        }
//        try {
//            System.out.println(fileToUpload.getAbsolutePath());
//            System.out.println(server.getHostIp() +  server.getHostPort());
//            server.uploadFile(fileToUpload);
//
//        } catch (Exception exception) {
//            System.out.println(exception.getMessage());
//        }
//    }
}
