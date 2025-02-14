package question;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import answer.Answer;
import category.Category;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import quiz.Quiz;
import suggestion.Suggestion;

@Entity
public class Question implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int questionID;

    private String question;

    @OneToMany(mappedBy = "belongsTo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "categoryID", nullable = false)
    private Category category;

    @Column(nullable = false)
    private boolean isActive = false;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Suggestion> suggestions = new ArrayList<>();

    @ManyToMany(mappedBy = "questions")
    private List<Quiz> quizzes = new ArrayList<>();

    // Constructors

    public Question() {
    }

    public Question(String question, Category category) {
        this.question = question;
        this.category = category;
    }

    public Question(String question, Category category, boolean isActive) {
        this.question = question;
        this.category = category;
        this.isActive = isActive;
    }

    // Getter methods
    public int getQuestionID() {
        return this.questionID;
    }

    public String getQuestion() {
        return this.question;
    }

    public List<Answer> getAnswers() {
        return this.answers;
    }

    public Category getCategory() {
        return this.category;
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    public List<Quiz> getQuizzes() {
        return this.quizzes;
    }

    // Setter methods
    public void setQuestion(String question) {
        this.question = question;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}