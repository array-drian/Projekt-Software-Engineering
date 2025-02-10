package category;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

@Named
@ApplicationScoped
public class CategoryDAO {

    @Inject
    private EntityManagerFactory emf;

    private EntityManager entityManager;

    @PostConstruct
    public void init() {
        this.entityManager = emf.createEntityManager();
    }

    @PreDestroy
    public void cleanup() {
        if (this.entityManager.isOpen()) {
            this.entityManager.close();
        }
    }

    public Category getCategoryAtIndex(int categoryID) {
        return entityManager.createQuery("SELECT c FROM Category c WHERE c.categoryID = :categoryID", Category.class)
                        .setParameter("categoryID", categoryID)
                        .getSingleResult();
    }

    public List<Category> getAllCategorys() {
        return entityManager.createQuery("SELECT c FROM Category c", Category.class).getResultList();
    }

    public EntityTransaction beginTransaction() {
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }
        return entityManager.getTransaction();
    }
    

    public void merge(Category category) {
        EntityTransaction tx = beginTransaction();
        try {
            entityManager.merge(category);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public void persist(Category category) {
        EntityTransaction tx = beginTransaction();
        try {
            entityManager.persist(category);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public void remove(Category category) {
        EntityTransaction tx = beginTransaction();
        try {
            entityManager.remove(category);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public void saveUser(Category category) {
        persist(category);
    }
}