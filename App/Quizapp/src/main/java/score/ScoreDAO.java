package score;

import java.util.List;

import category.Category;
import game.Game;
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
        if(this.entityManager.isOpen()) {
            this.entityManager.close();
        }
    }

    //Get the Entity with scoreID = scoreID
    public Score getScoreAtIndex(int scoreID) {
        try {
            return entityManager.createQuery(
                "SELECT s FROM Score s " +
                "WHERE s.scoreID = :scoreID", Score.class)
                .setParameter("scoreID", scoreID)
                .getSingleResult();
        }catch(NoResultException e) {
            return null;
        }
    }

    //Gets the total number of scores for userID = userID
    public Long getScoreCountForUser(int userID) {
        try {
            return entityManager.createQuery(
                "SELECT COUNT(s) FROM Score s " +
                "WHERE s.user.userID = :userID " +
                "AND s.game.isFinished = true", Long.class)
                .setParameter("userID", userID)
                .getSingleResult();
            }catch(NoResultException e) {
                return null;
            }
    }
    
    //Gets the total number of scored points for userID = userID
    public Long getTotalScoreForUser(int userID) {
        try {
            return entityManager.createQuery(
                "SELECT COALESCE(SUM(s.score), 0) FROM Score s " + 
                "WHERE s.user.userID = :userID " + 
                "AND s.game.isFinished = true", Long.class)
                .setParameter("userID", userID)
                .getSingleResult();
        }catch(NoResultException e) {
            return null;
        }
    }

    //Get top 10 users and there amount of scores
    public List<Object[]> getTop10UsersByScoreCount() {
        return entityManager.createQuery(
            "SELECT s.user.userName, COUNT(s) as scoreCount " +
            "FROM Score s " +
            "WHERE s.game.isFinished = true " +
            "GROUP BY s.user.userName " +
            "ORDER BY scoreCount DESC", Object[].class)
            .setMaxResults(10)
            .getResultList();
    }

    //Get top 10 users and there amount of scored points
    public List<Object[]> getTop10UsersByTotalScore() {
        return entityManager.createQuery(
            "SELECT s.user.userName, COALESCE(SUM(s.score), 0) as totalScore " +
            "FROM Score s " +
            "WHERE s.game.isFinished = true " +
            "GROUP BY s.user.userName " +
            "ORDER BY totalScore DESC", Object[].class)
            .setMaxResults(10)
            .getResultList();
    }
    
    //Gets the most player category for userID = userID
    public Category getMostPlayedCategoryForUser(int userID) {
        try {
            return entityManager.createQuery(
                "SELECT c FROM Category c " +
                "JOIN c.quizzes q " +
                "JOIN Game g ON g.quiz = q " +
                "JOIN g.scores s " +
                "WHERE s.user.userID = :userID " +
                "GROUP BY c " +
                "ORDER BY COUNT(g) DESC",
                Category.class)
                .setParameter("userID", userID)
                .setMaxResults(1)
                .getSingleResult();
        }catch(NoResultException e) {
            return null;
        }
    }

    //Gets all finished Singeleplayer games for userID = userID
    public List<Game> getFinishedSingleplayerGamesForUser(int userID) {
        return entityManager.createQuery(
            "SELECT g FROM Game g " +
            "JOIN g.scores s " +
            "WHERE s.user.userID = :userID " +
            "AND g.isFinished = true " +
            "AND g.isMultiplayer = false", Game.class)
            .setParameter("userID", userID)
            .getResultList();
    }

    //Gets all finished Multiplayer games for userID = userID
    public List<Game> getFinishedMuliplayerGamesForUser(int userID) {
        return entityManager.createQuery(
            "SELECT g FROM Game g " +
            "JOIN g.scores s " +
            "WHERE s.user.userID = :userID " +
            "AND g.isFinished = true " +
            "AND g.isMultiplayer = true", Game.class)
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
    public Score merge(Score score) {
        EntityTransaction tx = beginTransaction();
        try {
            Score managedScore = entityManager.merge(score);
            tx.commit();
            return managedScore;
        }catch(RuntimeException e) {
            if(tx.isActive()) tx.rollback();
            throw e;
        }
    }

    //Persist an Entity
    public void persist(Score score) {
        EntityTransaction tx = beginTransaction();
        try {
            entityManager.persist(score);
            tx.commit();
        }catch(RuntimeException e) {
            if(tx.isActive()) tx.rollback();
            throw e;
        }
    }

    //Remove an Entity
    public void remove(Score score) {
        EntityTransaction tx = beginTransaction();
        try {
            entityManager.remove(score);
            tx.commit();
        }catch(RuntimeException e) {
            if(tx.isActive()) tx.rollback();
            throw e;
        }
    }
}