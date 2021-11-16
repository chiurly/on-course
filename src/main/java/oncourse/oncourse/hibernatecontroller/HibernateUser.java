package oncourse.oncourse.hibernatecontroller;

import oncourse.oncourse.ds.Course;
import oncourse.oncourse.ds.File;
import oncourse.oncourse.ds.Folder;
import oncourse.oncourse.ds.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class HibernateUser {

    private EntityManagerFactory entityManagerFactory;
    private HibernateCourse hibernateCourse = new HibernateCourse(entityManagerFactory);

    public HibernateUser(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void create(User user) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(entityManager.merge(user));
            entityManager.getTransaction().commit();

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        entityManager.close();
    }

    public void edit(User user) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.merge(user);
            entityManager.getTransaction().commit();

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        entityManager.close();
    }

    public void delete(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        User user = entityManager.getReference(User.class, id);

        for (Course course : user.getCreatedCourses()) {
            course.setCreator(null);
            for (User member : course.getMembers()) {
                member.removeCourse(course);
            }
        }

        for (Folder folder : user.getCreatedFolders()) {
            folder.setCreator(null);
        }

        for (File file : user.getCreatedFiles()) {
            file.setCreator(null);
        }

        try {
            entityManager.getTransaction().begin();
            entityManager.remove(user);
            entityManager.getTransaction().commit();

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        entityManager.close();
    }

    public User get(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        User user = entityManager.find(User.class, id);
        entityManager.close();
        return user;
    }

    public User get(String username) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        User user = null;

        try {
            Query query = entityManager.createQuery("select u from user u where u.username = :username", User.class);
            query.setParameter("username", username);
            user = (User) query.getSingleResult();

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        entityManager.close();
        return user;
    }

    public List<User> get(int firstResult, int maxResults) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<User> users = null;

        try {
            CriteriaQuery criteriaQuery = entityManager.getCriteriaBuilder().createQuery();
            criteriaQuery.select(criteriaQuery.from(User.class));
            Query query = entityManager.createQuery(criteriaQuery);
            query.setFirstResult(firstResult);
            query.setMaxResults(maxResults);
            users = query.getResultList();

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        entityManager.close();
        return users;
    }

    public List<User> getAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<User> users = null;

        try {
            CriteriaQuery criteriaQuery = entityManager.getCriteriaBuilder().createQuery();
            criteriaQuery.select(criteriaQuery.from(User.class));
            Query query = entityManager.createQuery(criteriaQuery);
            users = query.getResultList();

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        entityManager.close();
        return users;
    }

}
