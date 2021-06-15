package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import main.Client;
import File.FileDetails;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class ConnectionController implements Initializable {
    @FXML
    private Button connectionButton;
    @FXML
    private Label connectionMessage;
    @FXML
    private ImageView brandingImageView;
    @FXML
    private ImageView connectionImageView;
    @FXML
    private TextField ipAddressTextField;
    @FXML
    private TextField portTextField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File brandingFile = new File("images/upload-icon.png");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);

        File connectionFile = new File("images/connection-icon.png");
        Image connectionImage = new Image(connectionFile.toURI().toString());
        connectionImageView.setImage(connectionImage);
    }

    @FXML
    public void connectionButtonAction(ActionEvent event) throws IOException, InterruptedException {
//        Stage stage = (Stage) connectionButton.getScene().getWindow();
//        stage.close();
        String ipAddress = ipAddressTextField.getText();
        String port = portTextField.getText();

        if (ipAddress.isBlank() || port.isBlank()) {
            connectionMessage.setText("Please Fill the fields");
            return;
        }

        try {
            Client client = new Client(ipAddress, Integer.parseInt(port));
//            setupConnection(event);
            connectionMessage.setText("Connection Set Up Successful!");


//            Parent clientView = FXMLLoader.load(getClass().getResource("../views/client.fxml"));
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../views/client.fxml"));
            Parent clientView = loader.load();

            Scene clientViewScene = new Scene(clientView);

            // Access the controller
            ClientController clientController = loader.getController();
//            ArrayList<FileDetails> fileList = new ArrayList<FileDetails>();
//            ArrayList<FileDetails> fileList = client.fetchFileListFromServer();
//            client.getFileList();
            clientController.initData(client);

            Stage clientViewStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            clientViewStage.setUserData(client);
            clientViewStage.setScene(clientViewScene);
            clientViewStage.show();
        } catch (Exception exception) {
            exception.printStackTrace();
            connectionMessage.setText("Choose a valid Ip Address and Port");
        }

//        setupConnection(event);
    }

    private void setupConnection(ActionEvent event) throws IOException, InterruptedException {
        connectionMessage.setText("Connection Set Up Successful!");
        loadClientFxml(event);
    }

    private void loadClientFxml(ActionEvent event) throws IOException {

        Parent clientView = FXMLLoader.load(getClass().getResource("../views/client.fxml"));
        Scene clientViewScene = new Scene(clientView);
        Stage clientViewStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        clientViewStage.setScene(clientViewScene);
        clientViewStage.show();
    }
}
