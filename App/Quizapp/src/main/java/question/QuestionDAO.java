package question;
import java.util.List;

import answer.Answer;
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
public class QuestionDAO {

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

    public Question getQuestionAtIndex(int questionID) {
        return entityManager.createQuery("SELECT q FROM Question q WHERE q.questionID = :questionID", Question.class)
                        .setParameter("questionID", questionID)
                        .getSingleResult();
    }

    public List<Answer> getAnswersForQuestion(int questionID) {
        return entityManager.createQuery("SELECT a FROM Answer a WHERE a.belongsTo.questionID = :questionID", Answer.class)
                        .setParameter("questionID", questionID)
                        .getResultList();
    }

    public EntityTransaction beginTransaction() {
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }
        return entityManager.getTransaction();
    }

    public void merge(Question question) {
        EntityTransaction tx = beginTransaction();
        try {
            entityManager.merge(question);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public void persist(Question question) {
        EntityTransaction tx = beginTransaction();
        try {
            entityManager.persist(question);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public void remove(Question question) {
        EntityTransaction tx = beginTransaction();
        try {
            entityManager.remove(question);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public void saveAnswer(Question question) {
        persist(question);
    }
}