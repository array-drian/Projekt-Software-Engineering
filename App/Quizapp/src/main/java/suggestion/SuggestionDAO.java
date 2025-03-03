package suggestion;
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
        if(this.entityManager.isOpen()) {
            this.entityManager.close();
        }
    }

    //Get the Entity with suggestionID = suggestionID
    public Suggestion getSuggestionAtIndex(int suggestionID) {
        try {
            return entityManager.createQuery(
                "SELECT s FROM Suggestion s " +
                "WHERE s.suggestionID = :suggestionID", Suggestion.class)
                .setParameter("suggestionID", suggestionID)
                .getSingleResult();
        }catch(NoResultException e) {
            return null;
        }
    }
    
    //Get all pending suggestions
    public List<Suggestion> getPendingSuggestions() {
        return entityManager.createQuery(
            "SELECT s FROM Suggestion s " +
            "WHERE s.isAccepted = false " +
            "AND s.isDenied = false", Suggestion.class)
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
    public Suggestion merge(Suggestion suggestion) {
        EntityTransaction tx = beginTransaction();
        try {
            Suggestion managedSuggestion = entityManager.merge(suggestion);
            tx.commit();
            return managedSuggestion;
        }catch(RuntimeException e) {
            if(tx.isActive()) tx.rollback();
            throw e;
        }
    }

    //Persist an Entity
    public void persist(Suggestion suggestion) {
        EntityTransaction tx = beginTransaction();
        try {
            entityManager.persist(suggestion);
            tx.commit();
        }catch(RuntimeException e) {
            if(tx.isActive()) tx.rollback();
            throw e;
        }
    }

    //Remove an Entity
    public void remove(Suggestion suggestion) {
        EntityTransaction tx = beginTransaction();
        try {
            entityManager.remove(suggestion);
            tx.commit();
        }catch(RuntimeException e) {
            if(tx.isActive()) tx.rollback();
            throw e;
        }
    }
}