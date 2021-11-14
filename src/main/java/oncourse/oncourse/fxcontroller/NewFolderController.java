package oncourse.oncourse.fxcontroller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import oncourse.oncourse.ds.Folder;
import oncourse.oncourse.ds.User;
import oncourse.oncourse.hibernatecontroller.HibernateFile;
import oncourse.oncourse.hibernatecontroller.HibernateFolder;
import oncourse.oncourse.hibernatecontroller.HibernateUser;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class NewFolderController {

    @FXML
    private TextField nameField;

    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("on_course");
    private final HibernateUser hibernateUser = new HibernateUser(entityManagerFactory);
    private final HibernateFolder hibernateFolder = new HibernateFolder(entityManagerFactory);

    @FXML
    protected void onCreateButtonClick() {
        if (nameField.getText().isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Required fields cannot be empty");
            alert.setContentText("Field \"Name\" cannot be empty");
            alert.showAndWait();
            return;
        }

        User creator = hibernateUser.get(SignInController.getSignedInUserId());
        Folder parentFolder = hibernateFolder.get(MainController.getSelectedFolderOrFileId());

        Folder folder = new Folder(nameField.getText());
        creator.addCreatedFolder(folder);
        parentFolder.addFolder(folder);
        hibernateFolder.create(folder);

        closeWindow();
    }

    @FXML
    protected void onCancelButtonClick() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

}
