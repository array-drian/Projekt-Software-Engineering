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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

    // Constructors
    public Question() {
        this.answers = new ArrayList<>();
    }

    public Question(String question, Category category) {
        this.question = question;
        this.category = category;
        this.answers = new ArrayList<>();
    }

    public Question(String question, Category category, boolean isActive) {
        this.question = question;
        this.category = category;
        this.isActive = isActive;
        this.answers = new ArrayList<>();
    }

    // Getter methods
    public int getQuestionId() {
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