package oncourse.oncourse.hibernatecontroller;

import oncourse.oncourse.ds.Course;
import oncourse.oncourse.ds.File;
import oncourse.oncourse.ds.Folder;
import oncourse.oncourse.ds.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.HashSet;
import java.util.Set;

public class HibernateCourse {

    private EntityManagerFactory entityManagerFactory;
    private HibernateFolder hibernateFolder = new HibernateFolder(entityManagerFactory);

    public HibernateCourse(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void create(Course course) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(entityManager.merge(course));
            entityManager.getTransaction().commit();

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        entityManager.close();
    }

    public void edit(Course course) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.merge(course);
            entityManager.getTransaction().commit();

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        entityManager.close();
    }

    public void remove(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Course course = entityManager.find(Course.class, id);

        User creator = course.getCreator();
        creator.removeCreatedCourse(course);

        for (User member : course.getMembers()) {
            member.removeCourse(course);
        }

        Folder rootFolder = course.getRootFolder();
        removeFolderAssociationsRecursively(rootFolder);

        try {
            entityManager.getTransaction().begin();
            entityManager.remove(course);
            entityManager.getTransaction().commit();

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        entityManager.close();
    }

    public Course get(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Course course = entityManager.find(Course.class, id);
        entityManager.close();
        return course;
    }

    public Set<Course> get(int firstResult, int maxResults) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Set<Course> courses = null;

        try {
            CriteriaQuery criteriaQuery = entityManager.getCriteriaBuilder().createQuery();
            criteriaQuery.select(criteriaQuery.from(Course.class));
            Query query = entityManager.createQuery(criteriaQuery);
            query.setFirstResult(firstResult);
            query.setMaxResults(maxResults);
            courses = new HashSet<Course>(query.getResultList());

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        entityManager.close();
        return courses;
    }

    public Set<Course> getAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Set<Course> courses = null;

        try {
            CriteriaQuery criteriaQuery = entityManager.getCriteriaBuilder().createQuery();
            criteriaQuery.select(criteriaQuery.from(Course.class));
            Query query = entityManager.createQuery(criteriaQuery);
            courses = new HashSet<Course>(query.getResultList());

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        entityManager.close();
        return courses;
    }

    private void removeFolderAssociationsRecursively(Folder folder) {
        User creator = folder.getCreator();
        if (creator != null) {
            creator.removeCreatedFolder(folder);
        }

        Folder parentFolder = folder.getParentFolder();
        if (parentFolder != null) {
            parentFolder.removeFolder(folder);
        }

        for (Folder subFolder : new HashSet<>(folder.getFolders())) {
            removeFolderAssociationsRecursively(subFolder);
        }

        for (File file : folder.getFiles()) {
            file.setParentFolder(null);
            file.getCreator().removeCreatedFile(file);
        }
    }

}
