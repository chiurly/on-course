package oncourse.oncourse.ds;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
public class Company extends User implements Serializable {

    private String companyName;
    private String phoneNumber;

    public Company() {

    }

    public Company(String username, String password, String companyName) {
        super(username, password);
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
