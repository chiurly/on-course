package oncourse.oncourse.fxcontroller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import oncourse.oncourse.Launcher;
import oncourse.oncourse.ds.User;
import oncourse.oncourse.hibernatecontroller.HibernateUser;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;

public class SignInController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private static int signedInUserId;
    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("on_course");
    private final HibernateUser hibernateUser = new HibernateUser(entityManagerFactory);

    public static int getSignedInUserId() {
        return signedInUserId;
    }

    @FXML
    protected void onSignInButtonClick() throws IOException {
        User user = hibernateUser.get(usernameField.getText());

        if (user == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("User not found");
            alert.setContentText("Could not find a user with username \"" + usernameField.getText() + "\"");
            alert.showAndWait();
            return;
        }

        if (!passwordField.getText().equals(user.getPassword())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Incorrect password");
            alert.setContentText("The entered password is incorrect");
            alert.showAndWait();
            return;
        }

        signedInUserId = user.getId();

        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setTitle("Main");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void onSignUpButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("sign-up-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setTitle("Sign Up");
        stage.setScene(scene);
        stage.show();
    }

}
