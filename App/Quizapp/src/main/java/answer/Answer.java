package answer;
import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import question.Question;

@Entity
public class Answer implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int answerID;

    private String answer;

    @ManyToOne
    @JoinColumn(name = "questionID", nullable = false)
    private Question belongsTo;

    @Column(nullable = false)
    private boolean isCorrect = false;

    //Constructor

    public Answer() {
    }

    public Answer(String answer, Question question) {
        this.answer = answer;
        this.belongsTo = question;
    }

    public Answer(String answer, Question question, boolean isCorrect) {
        this.answer = answer;
        this.belongsTo = question;
        this.isCorrect = isCorrect;
    }

    //Getter

    public int getAnswerID() {
        return this.answerID;
    }

    public String getAnswer() {
        return this.answer;
    }

    public Question getBelongsTo() {
        return this.belongsTo;
    }

    public boolean getIsCorrect() {
        return this.isCorrect;
    }

    //Setter

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setBelongsTo(Question question) {
        this.belongsTo = question;
    }

    public void setIsCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }
}