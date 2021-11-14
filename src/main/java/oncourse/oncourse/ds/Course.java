package oncourse.oncourse.ds;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "course")
public class Course implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private int id;

    @Column(nullable = false)
    private String name;

    private String description;
    private LocalDate dateCreated;
    private LocalDate endDate;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @OrderBy("id ASC")
    private User creator;

    @OneToOne(mappedBy = "rootOfCourse", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    @OrderBy("id ASC")
    private Folder rootFolder;

    @ManyToMany(cascade = {CascadeType.PERSIST})
    @LazyCollection(LazyCollectionOption.FALSE)
    @OrderBy("id ASC")
    private Set<User> members = new HashSet<>();

    public Course() {

    }

    public Course(String name) {
        this.name = name;
        dateCreated = LocalDate.now();
    }

    @Override
    public String toString() {
        return "Course ; " + id + " ; " + name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Folder getRootFolder() {
        return rootFolder;
    }

    public User getCreator() {
        return creator;
    }

    public Set<User> getMembers() {
        return members;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setRootFolder(Folder rootFolder) {
        this.rootFolder = rootFolder;
        if (rootFolder != null && rootFolder.getRootOfCourse() != this) {
            rootFolder.setRootOfCourse(this);
        }
    }

    public void setCreator(User creator) {
        this.creator = creator;
        if (creator != null) {
            creator.getCreatedCourses().add(this);
        }
    }

    public void setMembers(Set<User> members) {
        this.members = members;
    }

    public void addMember(User member) {
        members.add(member);
        member.getCourses().add(this);
    }

    public void removeMember(User member) {
        members.remove(member);
        member.getCourses().remove(this);
    }

}
