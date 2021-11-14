package oncourse.oncourse.hibernatecontroller;

import oncourse.oncourse.ds.File;
import oncourse.oncourse.ds.Folder;
import oncourse.oncourse.ds.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class HibernateFile {

    private EntityManagerFactory entityManagerFactory;

    public HibernateFile(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void create(File file) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(entityManager.merge(file));
            entityManager.getTransaction().commit();

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        entityManager.close();
    }

    public void edit(File file) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.merge(file);
            entityManager.getTransaction().commit();

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        entityManager.close();
    }

    public void remove(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        File file = entityManager.getReference(File.class, id);

        User creator = file.getCreator();
        creator.removeCreatedFile(file);

        Folder parentFolder = file.getParentFolder();
        parentFolder.removeFile(file);

        try {
            entityManager.getTransaction().begin();
            entityManager.remove(file);
            entityManager.getTransaction().commit();

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        entityManager.close();
    }

    public File get(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        File file = entityManager.find(File.class, id);
        entityManager.close();
        return file;
    }

    public List<File> get(int firstResult, int maxResults) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<File> files = null;

        try {
            CriteriaQuery criteriaQuery = entityManager.getCriteriaBuilder().createQuery();
            criteriaQuery.select(criteriaQuery.from(File.class));
            Query query = entityManager.createQuery(criteriaQuery);
            query.setFirstResult(firstResult);
            query.setMaxResults(maxResults);
            files = query.getResultList();

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        entityManager.close();
        return files;
    }

    public List<File> getAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<File> files = null;

        try {
            CriteriaQuery criteriaQuery = entityManager.getCriteriaBuilder().createQuery();
            criteriaQuery.select(criteriaQuery.from(File.class));
            Query query = entityManager.createQuery(criteriaQuery);
            files = query.getResultList();

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        entityManager.close();
        return files;
    }

}
