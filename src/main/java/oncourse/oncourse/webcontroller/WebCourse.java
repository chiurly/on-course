package oncourse.oncourse.webcontroller;

import com.google.gson.Gson;
import oncourse.oncourse.ds.Course;
import oncourse.oncourse.hibernatecontroller.HibernateCourse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Properties;

@Controller
public class WebCourse {

    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("on_course");
    private HibernateCourse hibernateCourse = new HibernateCourse(entityManagerFactory);

    @RequestMapping(value = "/course/all", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAll() {
        return hibernateCourse.getAll().toString();
    }

    @RequestMapping(value = "/course/create", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String create(@RequestBody String request) {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);

        Course course = new Course(properties.getProperty("name"));
        hibernateCourse.create(course);
        return "Course created";
    }

    @RequestMapping(value = "/course/edit/{id}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String edit(@RequestBody String request, @PathVariable(name = "id") int id) {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);

        Course course = hibernateCourse.get(id);
        course.setName(properties.getProperty("name"));

        hibernateCourse.edit(course);
        return "Course edited";
    }

    @RequestMapping(value = "/course/delete/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String delete(@PathVariable(name = "id") int id) {
        hibernateCourse.delete(id);
        return "Course deleted";
    }

}
