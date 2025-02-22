package user;
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
public class UserDAO {

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

    //Get the Entity with userId = userId
    public User getUserAtIndex(int userId) {
        try {
            return entityManager.createQuery(
                "SELECT u FROM User u " +
                "WHERE u.userID = :userId", User.class)
                .setParameter("userId", userId)
                .getSingleResult();
        }catch(NoResultException e) {
            return null;
        }
    }

    //Get the Entity with userId = userId and userPass = userPass
    public User getUserByNameAndPassword(String userName, String userPass) {
        try {
            return entityManager.createQuery(
                "SELECT u FROM User u " +
                "WHERE u.userName = :userName " +
                "AND u.userPass = :userPass", User.class)
                .setParameter("userName", userName)
                .setParameter("userPass", userPass)
                .getSingleResult();
        }catch(NoResultException e) {
            return null;
        }
    }

    //Get a list of all Users
    public List<User> getAllUsers() {
        return entityManager.createQuery(
            "SELECT u FROM User u", User.class)
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
    public User merge(User user) {
        EntityTransaction tx = beginTransaction();
        try {
            User managedUser = entityManager.merge(user);
            tx.commit();
            return managedUser;
        }catch(RuntimeException e) {
            if(tx.isActive()) tx.rollback();
            throw e;
        }
    }

    //Persist an Entity
    public void persist(User user) {
        EntityTransaction tx = beginTransaction();
        try {
            entityManager.persist(user);
            tx.commit();
        }catch(RuntimeException e) {
            if(tx.isActive()) tx.rollback();
            throw e;
        }
    }

    //Remove an Entity
    public void remove(User user) {
        EntityTransaction tx = beginTransaction();
        try {
            entityManager.remove(user);
            tx.commit();
        }catch(RuntimeException e) {
            if(tx.isActive()) tx.rollback();
            throw e;
        }
    }
}