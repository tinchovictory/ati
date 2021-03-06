package ar.edu.itba.ati.GUI.Windows;

import ar.edu.itba.ati.GUI.MainWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class OpenRawImage {

    @FXML
    Button openImage;

    @FXML
    TextField widthField,heightField;

    private MainWindow mainWindow;

    private Stage stage;

    private File image;

    public OpenRawImage() {
    }

    public void initData(MainWindow mainWindow, Stage stage){
        FileChooser chooser = new FileChooser();

        image =  chooser.showOpenDialog(stage);
        if(image != null){
            this.mainWindow = mainWindow;
            this.stage = stage;
            stage.setTitle("Open Raw Image");
            stage.show();
        }else{
            stage.close();
        }

    }

    @FXML
    public void openImageClicked(ActionEvent e){
        int width = Integer.parseInt(widthField.getCharacters().toString());
        int height = Integer.parseInt(heightField.getCharacters().toString());
        mainWindow.openRawImage(width,height,image);
        stage.close();
    }
}
