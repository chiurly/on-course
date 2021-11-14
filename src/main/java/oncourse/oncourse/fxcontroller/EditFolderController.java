package oncourse.oncourse.fxcontroller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import oncourse.oncourse.ds.Folder;
import oncourse.oncourse.hibernatecontroller.HibernateFolder;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.net.URL;
import java.util.ResourceBundle;

public class EditFolderController implements Initializable {

    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private DatePicker dateCreatedPicker;

    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("on_course");
    private final HibernateFolder hibernateFolder = new HibernateFolder(entityManagerFactory);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Folder selectedFolder = hibernateFolder.get(MainController.getSelectedFolderOrFileId());
        idField.setText(String.valueOf(selectedFolder.getId()));
        nameField.setText(selectedFolder.getName());
        dateCreatedPicker.setValue(selectedFolder.getDateCreated());
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

        Folder folder = hibernateFolder.get(MainController.getSelectedFolderOrFileId());
        folder.setName(nameField.getText());
        hibernateFolder.edit(folder);

        closeWindow();
    }

    @FXML
    protected void onDeleteButtonClick() {
        hibernateFolder.remove(MainController.getSelectedFolderOrFileId());
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
