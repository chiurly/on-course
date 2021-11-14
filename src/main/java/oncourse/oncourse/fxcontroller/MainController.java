package oncourse.oncourse.fxcontroller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import oncourse.oncourse.Launcher;
import oncourse.oncourse.ds.*;
import oncourse.oncourse.hibernatecontroller.HibernateCourse;
import oncourse.oncourse.hibernatecontroller.HibernateFile;
import oncourse.oncourse.hibernatecontroller.HibernateFolder;
import oncourse.oncourse.hibernatecontroller.HibernateUser;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class MainController implements Initializable {

    @FXML
    private TabPane tabPane;
    @FXML
    private Tab coursesTab;
    @FXML
    private Tab openedCourseTab;
    @FXML
    private Tab myAccountTab;

    @FXML
    private ListView courseList;
    @FXML
    private CheckBox showOnlyJoinedCheckBox;
    @FXML
    private Button openCourseButton;
    @FXML
    private Button joinCourseButton;
    @FXML
    private Button leaveCourseButton;
    @FXML
    private Button editCourseButton;

    @FXML
    private TreeView folderAndFileTree;
    @FXML
    private Button editFolderOrFileButton;


    @FXML
    private TextField idField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField emailField;
    @FXML
    private DatePicker dateCreatedPicker;

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

    private static int openedCourseId;
    private static int selectedCourseId;

    private static String selectedFolderOrFileType;
    private static int selectedFolderOrFileId;

    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("on_course");
    private final HibernateUser hibernateUser = new HibernateUser(entityManagerFactory);
    private final HibernateCourse hibernateCourse = new HibernateCourse(entityManagerFactory);
    private final HibernateFolder hibernateFolder = new HibernateFolder(entityManagerFactory);
    private final HibernateFile hibernateFile = new HibernateFile(entityManagerFactory);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateCourseList();

        User user = hibernateUser.get(SignInController.getSignedInUserId());

        idField.setText(String.valueOf(user.getId()));
        usernameField.setText(user.getUsername());
        passwordField.setText(user.getPassword());
        emailField.setText(user.getEmail());
        dateCreatedPicker.setValue(user.getDateCreated());

        if (user.getClass() == Person.class) {
            personRadioButton.setSelected(true);
            companyRadioButton.setSelected(false);
            personBox.setDisable(false);
            companyBox.setDisable(true);

            firstNameField.setText(((Person) user).getFirstName());
            lastNameField.setText(((Person) user).getLastName());
        } else {
            personRadioButton.setSelected(false);
            companyRadioButton.setSelected(true);
            personBox.setDisable(true);
            companyBox.setDisable(false);

            companyNameField.setText(((Company) user).getCompanyName());

            if (((Company) user).getPhoneNumber() != null) {
                phoneNumberField.setText(((Company) user).getPhoneNumber());
            }
        }
    }

    public static int getOpenedCourseId() {
        return openedCourseId;
    }

    public static int getSelectedCourseId() {
        return selectedCourseId;
    }

    public static String getSelectedFolderOrFileType() {
        return selectedFolderOrFileType;
    }

    public static int getSelectedFolderOrFileId() {
        return selectedFolderOrFileId;
    }

    @FXML
    protected void onCourseListClick() {
        String selectedItem = (String) courseList.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            return;
        }

        selectedCourseId = Integer.parseInt(selectedItem.split(" ; ")[1]);
        Course course = hibernateCourse.get(selectedCourseId);
        boolean alreadyJoined = false;
        boolean editable = course.getCreator().getId() == SignInController.getSignedInUserId();

        for (User user : course.getMembers()) {
            if (user.getId() == SignInController.getSignedInUserId()) {
                alreadyJoined = true;
                break;
            }
        }

        openCourseButton.setDisable(false);
        joinCourseButton.setDisable(alreadyJoined);
        leaveCourseButton.setDisable(!alreadyJoined);
        editCourseButton.setDisable(!editable);
    }

    @FXML
    protected void onShowOnlyJoinedCheckBoxAction() {
        updateCourseList();
    }

    @FXML
    protected void onNewCourseButtonClick() throws IOException {
        Stage stage = new Stage();
        stage.initOwner(tabPane.getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);
        stage.setTitle("New Course");

        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("new-course-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.showAndWait();

        updateCourseList();
    }

    @FXML
    protected void onOpenCourseButtonClick() {
        Course course = hibernateCourse.get(selectedCourseId);
        selectedFolderOrFileId = course.getRootFolder().getId();
        openedCourseId = selectedCourseId;

        updateFolderAndFileTree();
        tabPane.getSelectionModel().select(openedCourseTab);
    }

    @FXML
    protected void onJoinCourseButtonClick() {
        joinCourseButton.setDisable(true);

        Course course = hibernateCourse.get(selectedCourseId);
        User user = hibernateUser.get(SignInController.getSignedInUserId());
        user.addCourse(course);
        hibernateCourse.edit(course);

        leaveCourseButton.setDisable(false);
    }

    @FXML
    protected void onLeaveCourseButtonClick() {
        leaveCourseButton.setDisable(true);

        User user = hibernateUser.get(SignInController.getSignedInUserId());
        Course course = null;

        for (Course c : user.getCourses()) {
            if (c.getId() == selectedCourseId) {
                course = c;
                break;
            }
        }

        user.removeCourse(course);
        hibernateCourse.edit(course);

        joinCourseButton.setDisable(false);
        updateCourseList();
    }

    @FXML
    protected void onEditCourseButtonClick() throws IOException {
        Stage stage = new Stage();
        stage.initOwner(tabPane.getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);
        stage.setTitle("Edit Course");

        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("edit-course-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.showAndWait();

        updateCourseList();
    }

    @FXML
    protected void onFolderAndFileTreeClick() {
        TreeItem<String> selectedItem = (TreeItem<String>) folderAndFileTree.getSelectionModel().getSelectedItem();

        if (selectedCourseId == 0) {
            return;
        }

        if (selectedItem == null) {
            Course selectedCourse = hibernateCourse.get(selectedCourseId);
            if (selectedCourse == null) {
                return;
            }

            Folder folder = selectedCourse.getRootFolder();
            if (folder == null) {
                return;
            }

            selectedFolderOrFileType = "Folder";
            selectedFolderOrFileId = folder.getId();

        } else {
            String[] splits = selectedItem.getValue().split(" ; ");
            selectedFolderOrFileType = splits[0];
            selectedFolderOrFileId = Integer.parseInt(splits[1]);
        }

        boolean editable = false;

        if (selectedFolderOrFileType.equals("Folder")) {
            Folder selectedFolder = hibernateFolder.get(selectedFolderOrFileId);
            editable = selectedFolder.getCreator().getId() == SignInController.getSignedInUserId();

        } else if (selectedFolderOrFileType.equals("File")) {
            File selectedFile = hibernateFile.get(selectedFolderOrFileId);
            editable = selectedFile.getCreator().getId() == SignInController.getSignedInUserId();
        }

        editFolderOrFileButton.setDisable(!editable);
    }

    @FXML
    protected void onNewFolderButtonClick() throws IOException {
        if (!selectedFolderOrFileType.equals("Folder")) {
            return;
        }

        Stage stage = new Stage();
        stage.initOwner(tabPane.getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);
        stage.setTitle("New Folder");

        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("new-folder-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.showAndWait();

        updateFolderAndFileTree();
    }

    @FXML
    protected void onNewFileButtonClick() throws IOException {
        if (!selectedFolderOrFileType.equals("Folder")) {
            return;
        }

        Stage stage = new Stage();
        stage.initOwner(tabPane.getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);
        stage.setTitle("New File");

        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("new-file-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.showAndWait();

        updateFolderAndFileTree();
    }

    @FXML
    protected void onEditFolderOrFileButtonClick() throws IOException {
        Stage stage = new Stage();
        stage.initOwner(tabPane.getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);
        stage.setTitle("Edit " + selectedFolderOrFileType);

        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(
                selectedFolderOrFileType.equals("Folder") ? "edit-folder-view.fxml" : "edit-file-view.fxml")
        );

        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.showAndWait();

        updateFolderAndFileTree();
    }

    @FXML
    protected void onUpdateUserButtonClick() {
        User user = hibernateUser.get(SignInController.getSignedInUserId());
        boolean userIsPerson = user.getClass() == Person.class;

        if (usernameField.getText().isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Required fields cannot be empty");
            alert.setContentText("Field \"Username\" cannot be empty");
            alert.showAndWait();
            return;
        } else if (passwordField.getText().isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Required fields cannot be empty");
            alert.setContentText("Field \"Password\" cannot be empty");
            alert.showAndWait();
            return;
        } else if (userIsPerson) {
            if (firstNameField.getText().isBlank()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Required fields cannot be empty");
                alert.setContentText("Field \"First name\" cannot be empty");
                alert.showAndWait();
                return;
            } else if (lastNameField.getText().isBlank()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Required fields cannot be empty");
                alert.setContentText("Field \"Last name\" cannot be empty");
                alert.showAndWait();
                return;
            }
        } else {
            if (companyNameField.getText().isBlank()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Required fields cannot be empty");
                alert.setContentText("Field \"Company name\" cannot be empty");
                alert.showAndWait();
                return;
            }
        }

        if (!usernameField.getText().equals(user.getUsername()) && hibernateUser.get(usernameField.getText()) != null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Username must be unique");
            alert.setContentText("Username \"" + usernameField.getText() + "\" already exists");
            alert.showAndWait();
            return;
        }

        user.setUsername(usernameField.getText());
        user.setPassword(passwordField.getText());

        if (emailField.getText() != null && !emailField.getText().isBlank()) {
            user.setEmail(emailField.getText());
        }

        if (userIsPerson) {
            ((Person) user).setFirstName(firstNameField.getText());
            ((Person) user).setLastName(lastNameField.getText());
        } else {
            ((Company) user).setCompanyName(companyNameField.getText());
            if (!phoneNumberField.getText().isBlank()) {
                ((Company) user).setPhoneNumber(phoneNumberField.getText());
            }
        }

        hibernateUser.edit(user);
    }

    @FXML
    protected void onDeleteUserButtonClick() throws IOException {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.NO);

        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this user?\n\nUsername: " + usernameField.getText(),
                yes,
                no
        );

        alert.setHeaderText("User deletion");
        alert.showAndWait();

        if (alert.getResult() == yes) {
            hibernateUser.remove(SignInController.getSignedInUserId());
            showSignInView();
        }
    }

    @FXML
    protected void onSignOutButtonClick() throws IOException {
        showSignInView();
    }

    private void updateCourseList() {
        Set<Course> courses;

        if (showOnlyJoinedCheckBox.isSelected()) {
            User user = hibernateUser.get(SignInController.getSignedInUserId());
            courses = user.getCourses();
        } else {
            courses = hibernateCourse.getAll();
        }

        courseList.getItems().clear();
        courses.forEach(course -> courseList.getItems().add(course.toString()));
    }

    private void updateFolderAndFileTree() {
        Course course = hibernateCourse.get(openedCourseId);
        Folder rootFolder = hibernateFolder.get(course.getRootFolder().getId());

        TreeItem<String> rootTreeItem = new TreeItem<>(rootFolder.toString());
        rootTreeItem.setExpanded(true);
        folderAndFileTree.setRoot(rootTreeItem);

        for (Folder folder : rootFolder.getFolders()) {
            addToTreeRecursively(rootTreeItem, folder);
        }

        for (File file : rootFolder.getFiles()) {
            TreeItem<String> fileTreeItem = new TreeItem<>(file.toString());
            rootTreeItem.getChildren().add(fileTreeItem);
        }
    }

    private void addToTreeRecursively(TreeItem parentTreeItem, Folder folder) {
        TreeItem<String> folderTreeItem = new TreeItem<>(folder.toString());
        folderTreeItem.setExpanded(true);
        parentTreeItem.getChildren().add(folderTreeItem);

        for (Folder subFolder : folder.getFolders()) {
            addToTreeRecursively(folderTreeItem, subFolder);
        }

        for (File file : folder.getFiles()) {
            TreeItem<String> fileTreeItem = new TreeItem<>(file.toString());
            folderTreeItem.getChildren().add(fileTreeItem);
        }
    }

    private void showSignInView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("sign-in-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) tabPane.getScene().getWindow();
        stage.setTitle("Sign In");
        stage.setScene(scene);
        stage.show();
    }

}
