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
    private TableView<FileDetails> fileTableView;

    @FXML
    private TableColumn<FileDetails, Integer> fileIdColumn;

    @FXML
    private TableColumn<FileDetails, Long> fileSizeColumn;

    @FXML
    private TableColumn<FileDetails, String> fileNameColumn;

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
        this.observableFileList = server.getObservableFileList();
        fileTableView.setItems(this.observableFileList);
    }

    public void deleteFileMenuButtonAction(ActionEvent event) {
    }
}
