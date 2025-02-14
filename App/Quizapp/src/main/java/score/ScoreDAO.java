package score;
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
public class ScoreDAO {

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

    public Score getScoreAtIndex(int scoreID) {
        return entityManager.createQuery("SELECT s FROM Score s WHERE s.scoreID = :scoreID", Score.class)
                        .setParameter("scoreID", scoreID)
                        .getSingleResult();
    }

    public EntityTransaction beginTransaction() {
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }
        return entityManager.getTransaction();
    }

    public Score merge(Score score) {
        EntityTransaction tx = beginTransaction();
        try {
            Score managedScore = entityManager.merge(score); // Store the managed instance
            tx.commit();
            return managedScore; // Return the managed entity
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public void persist(Score score) {
        EntityTransaction tx = beginTransaction();
        try {
            entityManager.persist(score);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public void remove(Score score) {
        EntityTransaction tx = beginTransaction();
        try {
            entityManager.remove(score);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }
}