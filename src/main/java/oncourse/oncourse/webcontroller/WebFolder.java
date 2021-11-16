package oncourse.oncourse.webcontroller;

import com.google.gson.Gson;
import oncourse.oncourse.ds.Folder;
import oncourse.oncourse.hibernatecontroller.HibernateFolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Properties;

@Controller
public class WebFolder {

    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("on_course");
    private HibernateFolder hibernateFolder = new HibernateFolder(entityManagerFactory);

    @RequestMapping(value = "/folder/all", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAll() {
        return hibernateFolder.getAll().toString();
    }

    @RequestMapping(value = "/folder/create", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String create(@RequestBody String request) {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);

        Folder folder = new Folder(properties.getProperty("name"));
        hibernateFolder.create(folder);
        return "Folder created";
    }

    @RequestMapping(value = "/folder/edit/{id}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String edit(@RequestBody String request, @PathVariable(name = "id") int id) {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);

        Folder folder = hibernateFolder.get(id);
        folder.setName(properties.getProperty("name"));

        hibernateFolder.edit(folder);
        return "Folder edited";
    }

    @RequestMapping(value = "/folder/delete/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String delete(@PathVariable(name = "id") int id) {
        hibernateFolder.delete(id);
        return "Folder deleted";
    }

}
