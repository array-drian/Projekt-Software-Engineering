package game;
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
        if(this.entityManager.isOpen()) {
            this.entityManager.close();
        }
    }

    //Get the Entity with gameID = gameID
    public Game getGameAtIndex(int gameID) {
        try {
            return entityManager.createQuery(
                "SELECT m FROM Game m " +
                "WHERE m.gameID = :gameID", Game.class)
                .setParameter("gameID", gameID)
                .getSingleResult();
        }catch(NoResultException e) {
            return null;
        }
    }

    //Get all singleplayer Games with userID = userID
    public List<Game> getPendingSingleplayerGamesForUser(int userID) {
        return entityManager.createQuery(
            "SELECT g FROM Game g JOIN g.users u " +
            "WHERE u.userID = :userID " +
            "AND g.isFinished = false " +
            "AND g.isMultiplayer = false", Game.class)
            .setParameter("userID", userID)
            .getResultList();
    }

    //Get all multiplayer Games with userID = userID
    public List<Game> getPendingMultiplayerGamesForUser(int userID) {
        return entityManager.createQuery(
            "SELECT g FROM Game g " +
            "JOIN g.users u " +
            "WHERE u.userID = :userID " +
            "AND g.isFinished = false " +
            "AND g.isMultiplayer = true " +
            "AND SIZE(g.users) = g.maxPlayers " +
            "AND NOT EXISTS ( " +
            "   SELECT s FROM Score s " +
            "   WHERE s.game = g AND s.user.userID = :userID" +
            ")", Game.class)
            .setParameter("userID", userID)
            .getResultList();
    }    

    //Get all multiplayer Games with userID = userID that arent full
    public List<Game> getWaitingMultiplayerGamesForUser(int userID) {
        return entityManager.createQuery(
            "SELECT g FROM Game g JOIN g.users u " +
            "WHERE u.userID = :userID " +
            "AND g.isFinished = false " +
            "AND g.isMultiplayer = true " +
            "AND SIZE(g.users) <> g.maxPlayers", Game.class)
            .setParameter("userID", userID)
            .getResultList();
    }

    //Get all multiplayer Games with userID != userID that arent full
    public List<Game> getWaitingMultiplayerGamesWithoutUser(int userID) {
        return entityManager.createQuery(
            "SELECT g FROM Game g " +
            "WHERE g.isFinished = false " +
            "AND g.isMultiplayer = true " +
            "AND SIZE(g.users) <> g.maxPlayers " +
            "AND :userID NOT IN (SELECT u.userID FROM g.users u)", Game.class)
            .setParameter("userID", userID)
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
    public Game merge(Game game) {
        EntityTransaction tx = beginTransaction();
        try {
            Game managedGame = entityManager.merge(game);
            tx.commit();
            return managedGame;
        }catch(RuntimeException e) {
            if(tx.isActive()) tx.rollback();
            throw e;
        }
    }

    //Persist an Entity
    public void persist(Game game) {
        EntityTransaction tx = beginTransaction();
        try {
            entityManager.persist(game);
            tx.commit();
        }catch(RuntimeException e) {
            if(tx.isActive()) tx.rollback();
            throw e;
        }
    }

    //Remove an Entity
    public void remove(Game game) {
        EntityTransaction tx = beginTransaction();
        try {
            entityManager.remove(game);
            tx.commit();
        }catch(RuntimeException e) {
            if(tx.isActive()) tx.rollback();
            throw e;
        }
    }
}