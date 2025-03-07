package question;
import java.util.List;

import answer.Answer;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;

@Named
@RequestScoped 
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

    //Get the Entity with questionID = questionID
    public Question getQuestionAtIndex(int questionID) {
        try {
            return entityManager.createQuery(
                "SELECT q FROM Question q " +
                "WHERE q.questionID = :questionID", Question.class)
                .setParameter("questionID", questionID)
                .getSingleResult();
        }catch(NoResultException e) {
            return null;
        }
    }

    //Get all Question entities
    public List<Question> getAllQuestions() {
        return entityManager.createQuery(
            "SELECT q FROM Question q " +
            "WHERE q.isActive = true", Question.class)
            .getResultList();
    }

    //Get all Answer entities with questionID = questionID
    public List<Answer> getAnswersForQuestion(int questionID) {
        return entityManager.createQuery(
            "SELECT a FROM Answer a " +
            "WHERE a.belongsTo.questionID = :questionID", Answer.class)
            .setParameter("questionID", questionID)
            .getResultList();
    }

    //Get 10 random Question entities with categoryID = categoryID
    public List<Question> getRandomQuestions(int categoryID) {
        return entityManager.createQuery(
            "SELECT q FROM Question q " +
            "WHERE q.category.categoryID = :categoryID "+
            "AND q.isActive = true "+
            "ORDER BY FUNCTION('RAND')", Question.class)
            .setParameter("categoryID", categoryID)
            .setMaxResults(10)
            .getResultList();
    }

    //Check if a Username is already taken
    public Long checkQuestion(Question question) {
        try {
            return entityManager.createQuery(
                "SELECT COUNT(q) FROM Question q " +
                "WHERE q.question = :question " +
                "AND q.isActive = true " +
                "AND q.questionID <> :questionID",
                Long.class)
            .setParameter("question", question.getQuestion())
            .setParameter("questionID", question.getQuestionID())
            .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    

    //Create a transaction
    public EntityTransaction beginTransaction() {
        if(!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }
        return entityManager.getTransaction();
    }

    //Merge an Entity
    public Question merge(Question question) {
        EntityTransaction tx = beginTransaction();
        try {
            Question managedQuestion = entityManager.merge(question); // Store the managed instance
            tx.commit();
            return managedQuestion; // Return the managed entity
        }catch(RuntimeException e) {
            if(tx.isActive()) tx.rollback();
            throw e;
        }
    }    

    // Persist an Entity
    public void persist(Question question) {
        EntityTransaction tx = beginTransaction();
        try {
            // Check if an active question with the same text already exists
            if (question.getIsActive()) {
                if (checkQuestion(question) > 0) {
                    throw new PersistenceException("Duplicate entry: A question with this text is already active!");
                }
            }

            entityManager.persist(question);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    //Remove an Entity
    public void remove(Question question) {
        EntityTransaction tx = beginTransaction();
        try {
            entityManager.remove(question);
            tx.commit();
        }catch(RuntimeException e) {
            if(tx.isActive()) tx.rollback();
            throw e;
        }
    }
}