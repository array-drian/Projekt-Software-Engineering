package category;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

@Named
@RequestScoped 
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
        if(this.entityManager.isOpen()) {
            this.entityManager.close();
        }
    }

    //Get the Entity with categoryID = categoryID
    public Category getCategoryAtIndex(int categoryID) {
        try {
            return entityManager.createQuery(
                "SELECT c FROM Category c " +
                "WHERE c.categoryID = :categoryID", Category.class)
                .setParameter("categoryID", categoryID)
                .getSingleResult();
        }catch(NoResultException e) {
            return null;
        }
    }

    //Get all Entities from Category
    public List<Category> getAllCategorys() {
        return entityManager.createQuery(
            "SELECT c FROM Category c", Category.class)
            .getResultList();
    }

    //Create a transaction
    public EntityTransaction beginTransaction() {
        if(!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }
        return entityManager.getTransaction();
    }

    //Merge an Entity
    public Category merge(Category category) {
        EntityTransaction tx = beginTransaction();
        try {
            Category managedCategory = entityManager.merge(category);
            tx.commit();
            return managedCategory;
        }catch(RuntimeException e) {
            if(tx.isActive()) tx.rollback();
            throw e;
        }
    }

    //Persist an Entity
    public void persist(Category category) {
        EntityTransaction tx = beginTransaction();
        try {
            entityManager.persist(category);
            tx.commit();
        }catch(RuntimeException e) {
            if(tx.isActive()) tx.rollback();
            throw e;
        }
    }

    //Remove an Entity
    public void remove(Category category) {
        EntityTransaction tx = beginTransaction();
        try {
            entityManager.remove(category);
            tx.commit();
        }catch(RuntimeException e) {
            if(tx.isActive()) tx.rollback();
            throw e;
        }
    }
}