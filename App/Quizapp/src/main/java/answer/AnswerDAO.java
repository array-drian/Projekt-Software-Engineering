package answer;
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
public class AnswerDAO {

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

    public Answer getAnswerAtIndex(int answerID) {
        return entityManager.createQuery("SELECT a FROM Answer a WHERE a.answerID = :answerID", Answer.class)
                        .setParameter("answerID", answerID)
                        .getSingleResult();
    }

    public EntityTransaction beginTransaction() {
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }
        return entityManager.getTransaction();
    }

    public Answer merge(Answer answer) {
        EntityTransaction tx = beginTransaction();
        try {
            Answer managedAnswer = entityManager.merge(answer); // Store the managed instance
            tx.commit();
            return managedAnswer; // Return the managed entity
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public void persist(Answer answer) {
        EntityTransaction tx = beginTransaction();
        try {
            entityManager.persist(answer);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public void remove(Answer answer) {
        EntityTransaction tx = beginTransaction();
        try {
            entityManager.remove(answer);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }
}