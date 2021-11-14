package oncourse.oncourse.ds;

import java.io.Serializable;
import java.util.ArrayList;

public class CourseManagementSystem implements Serializable {

    private int id;
    private String version;
    private ArrayList<User> allUsers;
    private ArrayList<Course> allCourses;

    public CourseManagementSystem(int id, String version) {
        this.id = id;
        this.version = version;
        allUsers = new ArrayList<>();
        allCourses = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ArrayList<User> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(ArrayList<User> allUsers) {
        this.allUsers = allUsers;
    }

    public ArrayList<Course> getAllCourses() {
        return allCourses;
    }

    public void setAllCourses(ArrayList<Course> allCourses) {
        this.allCourses = allCourses;
    }

}
