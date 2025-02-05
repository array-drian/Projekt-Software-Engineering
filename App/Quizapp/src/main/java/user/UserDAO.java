package user;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

@Named
@ApplicationScoped
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
        if (this.entityManager.isOpen()) {
            this.entityManager.close();
        }
    }

    public User getUserAtIndex(int userId) {
        return entityManager.createQuery("SELECT u FROM User u WHERE u.userID = :userId", User.class)
                        .setParameter("userId", userId)
                        .getSingleResult();
    }

    public List<User> getAllUsers() {
        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    private EntityTransaction beginTransaction() {
        EntityTransaction tran = entityManager.getTransaction();
        tran.begin();
        return tran;
    }

    public User getUserByNameAndPassword(String userName, String userPass) {
        try {
            return entityManager.createQuery(
                "SELECT u FROM User u WHERE u.userName = :userName AND u.userPass = :userPass", User.class)
                .setParameter("userName", userName)
                .setParameter("userPass", userPass)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void merge(User user) {
        EntityTransaction tx = beginTransaction();
        try {
            entityManager.merge(user);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public void persist(User user) {
        EntityTransaction tx = beginTransaction();
        try {
            entityManager.persist(user);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public void remove(User user) {
        EntityTransaction tx = beginTransaction();
        try {
            entityManager.remove(user);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public void saveUser(User user) {
        persist(user);
    }
}