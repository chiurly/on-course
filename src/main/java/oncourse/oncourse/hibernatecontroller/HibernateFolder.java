package oncourse.oncourse.hibernatecontroller;

import oncourse.oncourse.ds.File;
import oncourse.oncourse.ds.Folder;
import oncourse.oncourse.ds.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.HashSet;
import java.util.Set;

public class HibernateFolder {

    private EntityManagerFactory entityManagerFactory;

    public HibernateFolder(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void create(Folder folder) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(entityManager.merge(folder));
            entityManager.getTransaction().commit();

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        entityManager.close();
    }

    public void edit(Folder folder) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.merge(folder);
            entityManager.getTransaction().commit();

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        entityManager.close();
    }

    public void remove(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Folder folder = entityManager.find(Folder.class, id);
        removeFolderAssociationsRecursively(folder);

        try {
            entityManager.getTransaction().begin();
            entityManager.remove(folder);
            entityManager.getTransaction().commit();

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        entityManager.close();
    }

    public Folder get(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Folder folder = entityManager.find(Folder.class, id);
        entityManager.close();
        return folder;
    }

    public Set<Folder> get(int firstResult, int maxResults) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Set<Folder> folders = null;

        try {
            CriteriaQuery criteriaQuery = entityManager.getCriteriaBuilder().createQuery();
            criteriaQuery.select(criteriaQuery.from(Folder.class));
            Query query = entityManager.createQuery(criteriaQuery);
            query.setFirstResult(firstResult);
            query.setMaxResults(maxResults);
            folders = new HashSet<Folder>(query.getResultList());

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        entityManager.close();
        return folders;
    }

    public Set<Folder> getAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Set<Folder> folders = null;

        try {
            CriteriaQuery criteriaQuery = entityManager.getCriteriaBuilder().createQuery();
            criteriaQuery.select(criteriaQuery.from(Folder.class));
            Query query = entityManager.createQuery(criteriaQuery);
            folders = new HashSet<Folder>(query.getResultList());

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        entityManager.close();
        return folders;
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
