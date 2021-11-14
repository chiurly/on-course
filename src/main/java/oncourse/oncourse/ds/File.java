package oncourse.oncourse.ds;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity(name = "file")
public class File implements Serializable {

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

    @ManyToOne
    @OrderBy("id ASC")
    private Folder parentFolder;

    public File() {

    }

    public File(String name) {
        this.name = name;
        dateCreated = LocalDate.now();
    }

    @Override
    public String toString() {
        return "File ; " + id + " ; " + name;
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

    public Folder getParentFolder() {
        return parentFolder;
    }

    public User getCreator() {
        return creator;
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

    public void setParentFolder(Folder parentFolder) {
        this.parentFolder = parentFolder;
    }

    public void setCreator(User creator) {
        this.creator = creator;
        if (creator != null) {
            creator.getCreatedFiles().add(this);
        }
    }

}
