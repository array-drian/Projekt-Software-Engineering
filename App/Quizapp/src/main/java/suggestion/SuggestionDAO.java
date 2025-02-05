package suggestion;
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

    private EntityTransaction beginTransaction() {
        EntityTransaction tran = entityManager.getTransaction();
        tran.begin();
        return tran;
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