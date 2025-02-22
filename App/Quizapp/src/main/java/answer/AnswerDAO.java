package answer;
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
        if(this.entityManager.isOpen()) {
            this.entityManager.close();
        }
    }

    //Get the Entity with answerID = answerID
    public Answer getAnswerAtIndex(int answerID) {
        try {
            return entityManager.createQuery(
                "SELECT a FROM Answer a " +
                "WHERE a.answerID = :answerID", Answer.class)
                .setParameter("answerID", answerID)
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
    public Answer merge(Answer answer) {
        EntityTransaction tx = beginTransaction();
        try {
            Answer managedAnswer = entityManager.merge(answer);
            tx.commit();
            return managedAnswer;
        }catch(RuntimeException e) {
            if(tx.isActive()) tx.rollback();
            throw e;
        }
    }

    //Persist an Entity
    public void persist(Answer answer) {
        EntityTransaction tx = beginTransaction();
        try {
            entityManager.persist(answer);
            tx.commit();
        }catch(RuntimeException e) {
            if(tx.isActive()) tx.rollback();
            throw e;
        }
    }

    //Remove an Entity
    public void remove(Answer answer) {
        EntityTransaction tx = beginTransaction();
        try {
            entityManager.remove(answer);
            tx.commit();
        }catch(RuntimeException e) {
            if(tx.isActive()) tx.rollback();
            throw e;
        }
    }
}