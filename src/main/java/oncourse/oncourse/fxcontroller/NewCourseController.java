package oncourse.oncourse.fxcontroller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import oncourse.oncourse.ds.Course;
import oncourse.oncourse.ds.Folder;
import oncourse.oncourse.ds.User;
import oncourse.oncourse.hibernatecontroller.HibernateCourse;
import oncourse.oncourse.hibernatecontroller.HibernateFolder;
import oncourse.oncourse.hibernatecontroller.HibernateUser;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class NewCourseController {

    @FXML
    private TextField nameField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private DatePicker endDatePicker;

    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("on_course");
    private final HibernateUser hibernateUser = new HibernateUser(entityManagerFactory);
    private final HibernateCourse hibernateCourse = new HibernateCourse(entityManagerFactory);
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
        Course course = new Course(nameField.getText());

        if (descriptionArea.getText() != null && !descriptionArea.getText().isBlank()) {
            course.setDescription(descriptionArea.getText());
        }

        course.setEndDate(endDatePicker.getValue());
        course.setCreator(creator);
        course.addMember(creator);

        Folder rootFolder = new Folder(nameField.getText());
        rootFolder.setCreator(creator);
        course.setRootFolder(rootFolder);

        hibernateUser.edit(creator);

        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onCancelButtonClick() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

}
