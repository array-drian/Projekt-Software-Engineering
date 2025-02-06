package suggestion;
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
public class SuggestionDAO {

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

    public Suggestion getSuggestionAtIndex(int suggestionID) {
        return entityManager.createQuery("SELECT s FROM Suggestion s WHERE s.suggestionID = :suggestionID", Suggestion.class)
                        .setParameter("suggestionID", suggestionID)
                        .getSingleResult();
    }

    public int countPendingSuggestions() {
        return entityManager.createQuery(
            "SELECT COUNT(s) FROM Suggestion s WHERE s.isAccepted = false", Integer.class)
            .getSingleResult();
    }
    
    public List<Suggestion> getPendingSuggestions() {
        return entityManager.createQuery(
            "SELECT s FROM Suggestion s WHERE s.isAccepted = false", Suggestion.class)
            .getResultList();
    }

    public EntityTransaction beginTransaction() {
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }
        return entityManager.getTransaction();
    }

    public void merge(Suggestion suggestion) {
        EntityTransaction tx = beginTransaction();
        try {
            entityManager.merge(suggestion);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public void persist(Suggestion suggestion) {
        EntityTransaction tx = beginTransaction();
        try {
            entityManager.persist(suggestion);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public void remove(Suggestion suggestion) {
        EntityTransaction tx = beginTransaction();
        try {
            entityManager.remove(suggestion);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public void saveAnswer(Suggestion suggestion) {
        persist(suggestion);
    }
}