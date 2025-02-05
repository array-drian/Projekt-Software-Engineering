package suggestion;
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
public class Suggestion implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int suggestionID;

    @ManyToOne
    @JoinColumn(name = "questionID", nullable = false)
    private Question question;

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private User byUser;

    @Column(nullable = false)
    private boolean isAccepted = false;

    //Constructor

    public Suggestion() {
    }

    public Suggestion(Question question, User byUser) {
        this.question = question;
        this.byUser = byUser;
    }

    // Getter

    public int getSuggestionId() {
        return this.suggestionID;
    }

    public Question getQuestion() {
        return this.question;
    }

    public User getByUser() {
        return this.byUser;
    }

    public boolean getIsAccepted() {
        return this.isAccepted;
    }

    //Setter

    public void setQuestion(Question question) {
        this.question = question;
    }

    public void setByUser(User byUser) {
        this.byUser = byUser;
    }

    public void setIsAccepted(boolean isAccepted) {
        this.isAccepted = isAccepted;
    }
}