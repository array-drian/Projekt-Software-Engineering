package quiz;
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
        if(this.entityManager.isOpen()) {
            this.entityManager.close();
        }
    }

    //Get the Entity with quizID = quizID
    public Quiz getQuizAtIndex(int quizID) {
        try {
            return entityManager.createQuery(
                "SELECT q FROM Quiz q " +
                "WHERE q.quizID = :quizID", Quiz.class)
                .setParameter("quizID", quizID)
                .getSingleResult();
        }catch(NoResultException e) {
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
    public Quiz merge(Quiz quiz) {
        EntityTransaction tx = beginTransaction();
        try {
            Quiz managedQuiz = entityManager.merge(quiz);
            tx.commit();
            return managedQuiz;
        }catch(RuntimeException e) {
            if(tx.isActive()) tx.rollback();
            throw e;
        }
    }

    //Persist an Entity
    public void persist(Quiz quiz) {
        EntityTransaction tx = beginTransaction();
        try {
            entityManager.persist(quiz);
            tx.commit();
        }catch(RuntimeException e) {
            if(tx.isActive()) tx.rollback();
            throw e;
        }
    }

    //Remove an Entity
    public void remove(Quiz quiz) {
        EntityTransaction tx = beginTransaction();
        try {
            entityManager.remove(quiz);
            tx.commit();
        }catch(RuntimeException e) {
            if(tx.isActive()) tx.rollback();
            throw e;
        }
    }
}