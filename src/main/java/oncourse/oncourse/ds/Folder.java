package oncourse.oncourse.ds;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "folder")
public class Folder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private int id;

    @Column(nullable = false)
    private String name;

    private LocalDate dateCreated;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @OrderBy("id ASC")
    private User creator;

    @OneToOne
    @OrderBy("id ASC")
    private Course rootOfCourse;

    @ManyToOne
    @OrderBy("id ASC")
    private Folder parentFolder;

    @OneToMany(mappedBy = "parentFolder", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    @OrderBy("id ASC")
    private Set<Folder> folders = new HashSet<>();

    @OneToMany(mappedBy = "parentFolder", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    @OrderBy("id ASC")
    private Set<File> files = new HashSet<>();

    public Folder() {

    }

    public Folder(String name) {
        this.name = name;
        dateCreated = LocalDate.now();
    }

    @Override
    public String toString() {
        return "Folder ; " + id + " ; " + name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public User getCreator() {
        return creator;
    }

    public Course getRootOfCourse() {
        return rootOfCourse;
    }

    public Folder getParentFolder() {
        return parentFolder;
    }

    public Set<Folder> getFolders() {
        return folders;
    }

    public Set<File> getFiles() {
        return files;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setCreator(User creator) {
        this.creator = creator;
        if (creator != null) {
            creator.getCreatedFolders().add(this);
        }
    }

    public void setRootOfCourse(Course rootOfCourse) {
        this.rootOfCourse = rootOfCourse;
        if (rootOfCourse != null && rootOfCourse.getRootFolder() != this) {
            rootOfCourse.setRootFolder(this);
        }
    }

    public void setParentFolder(Folder parentFolder) {
        this.parentFolder = parentFolder;
        if (parentFolder != null) {
            parentFolder.getFolders().add(this);
        }
    }

    public void setFolders(Set<Folder> folders) {
        this.folders = folders;
    }

    public void addFolder(Folder folder) {
        folders.add(folder);
        folder.setParentFolder(this);
    }

    public void removeFolder(Folder folder) {
        folders.remove(folder);
        folder.setParentFolder(null);
    }

    public void setFiles(Set<File> files) {
        this.files = files;
    }

    public void addFile(File file) {
        files.add(file);
        file.setParentFolder(this);
    }

    public void removeFile(File file) {
        files.remove(file);
        file.setParentFolder(null);
    }

}
