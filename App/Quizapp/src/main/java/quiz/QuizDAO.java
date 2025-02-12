package quiz;
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
public class QuizDAO {

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

    public Quiz getQuizAtIndex(int quizID) {
        return entityManager.createQuery("SELECT q FROM Quiz q WHERE q.quizID = :quizID", Quiz.class)
                        .setParameter("quizID", quizID)
                        .getSingleResult();
    }

    public EntityTransaction beginTransaction() {
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }
        return entityManager.getTransaction();
    }

    public Quiz merge(Quiz quiz) {
        EntityTransaction tx = beginTransaction();
        try {
            Quiz managedQuiz = entityManager.merge(quiz); // Store the managed instance
            tx.commit();
            return managedQuiz; // Return the managed entity
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public void persist(Quiz quiz) {
        EntityTransaction tx = beginTransaction();
        try {
            entityManager.persist(quiz);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public void remove(Quiz quiz) {
        EntityTransaction tx = beginTransaction();
        try {
            entityManager.remove(quiz);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }
}