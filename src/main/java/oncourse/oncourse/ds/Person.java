package oncourse.oncourse.ds;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
public class Person extends User implements Serializable {

    private String firstName;
    private String lastName;

    public Person() {

    }

    public Person(String username, String password, String firstName, String lastName) {
        super(username, password);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}
