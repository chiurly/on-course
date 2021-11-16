package oncourse.oncourse.webcontroller;

import com.google.gson.Gson;
import oncourse.oncourse.ds.Company;
import oncourse.oncourse.ds.Person;
import oncourse.oncourse.hibernatecontroller.HibernateUser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Properties;

@Controller
public class WebUser {

    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("on_course");
    private HibernateUser hibernateUser = new HibernateUser(entityManagerFactory);

    @RequestMapping(value = "/user/all", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAll() {
        return hibernateUser.getAll().toString();
    }

    @RequestMapping(value = "/user/createperson", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String createPerson(@RequestBody String request) {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);

        Person person = new Person(
                properties.getProperty("username"),
                properties.getProperty("password"),
                properties.getProperty("firstname"),
                properties.getProperty("lastname"));

        hibernateUser.create(person);
        return "Person created";
    }

    @RequestMapping(value = "/user/createcompany", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String createCompany(@RequestBody String request) {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);

        Company company = new Company(
                properties.getProperty("username"),
                properties.getProperty("password"),
                properties.getProperty("companyname"));

        hibernateUser.create(company);
        return "Company created";
    }

    @RequestMapping(value = "/user/editperson/{id}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String editPerson(@RequestBody String request, @PathVariable(name = "id") int id) {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);

        Person person = (Person) hibernateUser.get(id);
        person.setUsername(properties.getProperty("username"));
        person.setPassword(properties.getProperty("password"));
        person.setFirstName(properties.getProperty("firstname"));
        person.setFirstName(properties.getProperty("lastname"));
        person.setEmail(properties.getProperty("email"));

        hibernateUser.edit(person);
        return "Person edited";
    }

    @RequestMapping(value = "/user/editcompany/{id}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String editCompany(@RequestBody String request, @PathVariable(name = "id") int id) {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);

        Company company = (Company) hibernateUser.get(id);
        company.setUsername(properties.getProperty("username"));
        company.setPassword(properties.getProperty("password"));
        company.setCompanyName(properties.getProperty("companyname"));
        company.setEmail(properties.getProperty("email"));

        hibernateUser.edit(company);
        return "Company edited";
    }

    @RequestMapping(value = "/user/delete/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String delete(@PathVariable(name = "id") int id) {
        hibernateUser.delete(id);
        return "User deleted";
    }

}
