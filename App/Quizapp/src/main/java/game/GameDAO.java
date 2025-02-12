package game;
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
public class GameDAO {

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

    public Game getGameAtIndex(int gameID) {
        return entityManager.createQuery("SELECT m FROM Game m WHERE m.gameID = :gameID", Game.class)
                        .setParameter("gameID", gameID)
                        .getSingleResult();
    }

    public EntityTransaction beginTransaction() {
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }
        return entityManager.getTransaction();
    }

    public Game merge(Game game) {
        EntityTransaction tx = beginTransaction();
        try {
            Game managedGame = entityManager.merge(game); // Store the managed instance
            tx.commit();
            return managedGame; // Return the managed entity
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public void persist(Game game) {
        EntityTransaction tx = beginTransaction();
        try {
            entityManager.persist(game);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public void remove(Game game) {
        EntityTransaction tx = beginTransaction();
        try {
            entityManager.remove(game);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }
}