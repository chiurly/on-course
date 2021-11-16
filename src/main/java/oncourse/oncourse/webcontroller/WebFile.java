package oncourse.oncourse.webcontroller;

import com.google.gson.Gson;
import oncourse.oncourse.ds.File;
import oncourse.oncourse.hibernatecontroller.HibernateFile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Properties;

@Controller
public class WebFile {

    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("on_course");
    private HibernateFile hibernateFile = new HibernateFile(entityManagerFactory);

    @RequestMapping(value = "/file/all", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAll() {
        return hibernateFile.getAll().toString();
    }

    @RequestMapping(value = "/file/create", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String create(@RequestBody String request) {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);

        File file = new File(properties.getProperty("name"));
        hibernateFile.create(file);
        return "File created";
    }

    @RequestMapping(value = "/file/edit/{id}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String edit(@RequestBody String request, @PathVariable(name = "id") int id) {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);

        File file = hibernateFile.get(id);
        file.setName(properties.getProperty("name"));

        hibernateFile.edit(file);
        return "File edited";
    }

    @RequestMapping(value = "/file/delete/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String delete(@PathVariable(name = "id") int id) {
        hibernateFile.delete(id);
        return "File deleted";
    }

}
