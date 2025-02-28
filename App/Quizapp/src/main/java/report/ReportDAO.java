package report;
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
public class ReportDAO {

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

    //Get the Entity with reportID = reportID
    public Report getReportAtIndex(int reportID) {
        try {
            return entityManager.createQuery(
                "SELECT r FROM Report r " +
                "WHERE r.reportID = :reportID", Report.class)
                .setParameter("reportID", reportID)
                .getSingleResult();
        }catch(NoResultException e) {
            return null;
        }
    }
    
    //Get a list of all pending Reports
    public List<Report> getPendingReports() {
        return entityManager.createQuery(
            "SELECT r FROM Report r " +
            "WHERE r.isActive = true", Report.class)
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
    public Report merge(Report report) {
        EntityTransaction tx = beginTransaction();
        try {
            Report managedReport = entityManager.merge(report);
            tx.commit();
            return managedReport;
        }catch(RuntimeException e) {
            if(tx.isActive()) tx.rollback();
            throw e;
        }
    }

    //Persist an Entity
    public void persist(Report report) {
        EntityTransaction tx = beginTransaction();
        try {
            entityManager.persist(report);
            tx.commit();
        }catch(RuntimeException e) {
            if(tx.isActive()) tx.rollback();
            throw e;
        }
    }

    //Remove an Entity
    public void remove(Report report) {
        EntityTransaction tx = beginTransaction();
        try {
            entityManager.remove(report);
            tx.commit();
        }catch(RuntimeException e) {
            if(tx.isActive()) tx.rollback();
            throw e;
        }
    }
}