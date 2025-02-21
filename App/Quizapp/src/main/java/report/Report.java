package report;
import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import question.Question;
import user.User;

@Entity
public class Report implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reportID;

    @ManyToOne
    @JoinColumn(name = "questionID", nullable = false)
    private Question question;

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private User byUser;

    private String message;

    @Column(nullable = false)
    private boolean isActive = true;

    //Constructor

    public Report() {
    }

    public Report(Question question, User byUser, String message) {
        this.question = question;
        this.byUser = byUser;
        this.message = message;
    }

    //Getter

    public int getReportID() {
        return this.reportID;
    }

    public Question getQuestion() {
        return this.question;
    }

    public User getByUser() {
        return this.byUser;
    }

    public String getMessage() {
        return this.message;
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    //Setter

    public void setQuestion(Question question) {
        this.question = question;
    }

    public void setByUser(User byUser) {
        this.byUser = byUser;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}