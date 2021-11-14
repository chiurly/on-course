package oncourse.oncourse.fxcontroller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import oncourse.oncourse.Launcher;
import oncourse.oncourse.ds.Company;
import oncourse.oncourse.ds.Person;
import oncourse.oncourse.ds.User;
import oncourse.oncourse.hibernatecontroller.HibernateUser;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;

public class SignUpController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField emailField;

    @FXML
    private RadioButton personRadioButton;
    @FXML
    private VBox personBox;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;

    @FXML
    private RadioButton companyRadioButton;
    @FXML
    private VBox companyBox;
    @FXML
    private TextField companyNameField;
    @FXML
    private TextField phoneNumberField;

    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("on_course");
    private final HibernateUser hibernateUser = new HibernateUser(entityManagerFactory);

    @FXML
    protected void onPersonRadioButtonClick() {
        personRadioButton.setSelected(true);
        companyRadioButton.setSelected(false);
        personBox.setDisable(false);
        companyBox.setDisable(true);
    }

    @FXML
    protected void onCompanyRadioButtonClick() {
        personRadioButton.setSelected(false);
        companyRadioButton.setSelected(true);
        personBox.setDisable(true);
        companyBox.setDisable(false);
    }

    @FXML
    protected void onSignUpButtonClick() throws IOException {
        if (usernameField.getText().isBlank()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("Required fields cannot be empty");
            alert.setContentText("Field \"Username\" cannot be empty");
            alert.showAndWait();
            return;
        } else if (passwordField.getText().isBlank()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("Required fields cannot be empty");
            alert.setContentText("Field \"Password\" cannot be empty");
            alert.showAndWait();
            return;
        } else if (personRadioButton.isSelected()) {
            if (firstNameField.getText().isBlank()) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setHeaderText("Required fields cannot be empty");
                alert.setContentText("Field \"First name\" cannot be empty");
                alert.showAndWait();
                return;
            } else if (lastNameField.getText().isBlank()) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setHeaderText("Required fields cannot be empty");
                alert.setContentText("Field \"Last name\" cannot be empty");
                alert.showAndWait();
                return;
            }
        } else if (companyRadioButton.isSelected()) {
            if (companyNameField.getText().isBlank()) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setHeaderText("Required fields cannot be empty");
                alert.setContentText("Field \"Company name\" cannot be empty");
                alert.showAndWait();
                return;
            }
        }

        if (hibernateUser.get(usernameField.getText()) != null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("Username must be unique");
            alert.setContentText("Username \"" + usernameField.getText() + "\" already exists");
            alert.showAndWait();
            return;
        }

        User user;

        if (personRadioButton.isSelected()) {
            user = new Person(usernameField.getText(), passwordField.getText(), firstNameField.getText(), lastNameField.getText());
        } else {
            Company company = new Company(usernameField.getText(), passwordField.getText(), companyNameField.getText());
            if (!phoneNumberField.getText().isBlank()) {
                company.setPhoneNumber(phoneNumberField.getText());
            }
            user = company;
        }

        if (!emailField.getText().isBlank()) {
            user.setEmail(emailField.getText());
        }

        hibernateUser.create(user);

        showSignInView();
    }

    @FXML
    protected void onCancelButtonClick() throws IOException {
        showSignInView();
    }

    private void showSignInView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("sign-in-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setTitle("Sign In");
        stage.setScene(scene);
        stage.show();
    }

}
