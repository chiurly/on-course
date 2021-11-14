package oncourse.oncourse.fxcontroller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import oncourse.oncourse.ds.File;
import oncourse.oncourse.hibernatecontroller.HibernateFile;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.net.URL;
import java.util.ResourceBundle;

public class EditFileController implements Initializable {

    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private DatePicker dateCreatedPicker;

    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("on_course");
    private final HibernateFile hibernateFile = new HibernateFile(entityManagerFactory);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File selectedFile = hibernateFile.get(MainController.getSelectedFolderOrFileId());
        idField.setText(String.valueOf(selectedFile.getId()));
        nameField.setText(selectedFile.getName());
        dateCreatedPicker.setValue(selectedFile.getDateCreated());
    }

    @FXML
    protected void onUpdateButtonClick() {
        if (nameField.getText().isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Required fields cannot be empty");
            alert.setContentText("Field \"Name\" cannot be empty");
            alert.showAndWait();
            return;
        }

        File file = hibernateFile.get(MainController.getSelectedFolderOrFileId());
        file.setName(nameField.getText());
        hibernateFile.edit(file);

        closeWindow();
    }

    @FXML
    protected void onDeleteButtonClick() {
        hibernateFile.remove(MainController.getSelectedFolderOrFileId());
        closeWindow();
    }

    @FXML
    protected void onCancelButtonClick() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) idField.getScene().getWindow();
        stage.close();
    }

}
