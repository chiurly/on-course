package oncourse.oncourse.ds;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "user")
public abstract class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private int id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String email;
    private LocalDate dateCreated;

    @OneToMany(mappedBy = "creator", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    @OrderBy("id ASC")
    private Set<Course> createdCourses = new HashSet<>();

    @OneToMany(mappedBy = "creator", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    @OrderBy("id ASC")
    private Set<Folder> createdFolders = new HashSet<>();

    @OneToMany(mappedBy = "creator", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    @OrderBy("id ASC")
    private Set<File> createdFiles = new HashSet<>();

    @ManyToMany(mappedBy = "members", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @LazyCollection(LazyCollectionOption.FALSE)
    @OrderBy("id ASC")
    private Set<Course> courses = new HashSet<>();

    public User() {

    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        dateCreated = LocalDate.now();
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public Set<Course> getCreatedCourses() {
        return createdCourses;
    }

    public Set<Folder> getCreatedFolders() {
        return createdFolders;
    }

    public Set<File> getCreatedFiles() {
        return createdFiles;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setCreatedCourses(Set<Course> createdCourses) {
        this.createdCourses = createdCourses;
    }

    public void addCreatedCourse(Course course) {
        createdCourses.add(course);
        course.setCreator(this);
    }

    public void removeCreatedCourse(Course course) {
        createdCourses.remove(course);
        course.setCreator(null);
    }

    public void setCreatedFolders(Set<Folder> createdFolders) {
        this.createdFolders = createdFolders;
    }

    public void addCreatedFolder(Folder folder) {
        createdFolders.add(folder);
        folder.setCreator(this);
    }

    public void removeCreatedFolder(Folder folder) {
        createdFolders.remove(folder);
        folder.setCreator(null);
    }

    public void setCreatedFiles(Set<File> createdFiles) {
        this.createdFiles = createdFiles;
    }

    public void addCreatedFile(File file) {
        createdFiles.add(file);
        file.setCreator(this);
    }

    public void removeCreatedFile(File file) {
        createdFiles.remove(file);
        file.setCreator(null);
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public void addCourse(Course course) {
        courses.add(course);
        course.getMembers().add(this);
    }

    public void removeCourse(Course course) {
        courses.remove(course);
        course.getMembers().remove(this);
    }

}
