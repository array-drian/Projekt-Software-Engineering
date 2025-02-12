package quiz;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import category.Category;
import game.Game;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import question.Question;

@Entity
public class Quiz implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int quizID;

    @ManyToMany(cascade = { CascadeType.MERGE })
    @JoinTable(
        name = "quiz_question",
        joinColumns = @JoinColumn(name = "quizID"),
        inverseJoinColumns = @JoinColumn(name = "questionID")
    )
    private List<Question> questions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "categoryID", nullable = false)
    private Category category;

    @OneToOne(mappedBy = "quiz", cascade = CascadeType.ALL)
    private Game match;

    // Constructors

    public Quiz() {
    }

    public Quiz(Category category) {
        this.category = category;
    }

    // Getter methods
    public int getQuizID() {
        return this.quizID;
    }

    public List<Question> getQuestions() {
        return this.questions;
    }

    public Category getCategory() {
        return this.category;
    }

    public Game getMatch() {
        return this.match;
    }

    // Setter methods

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setMatch(Game match) {
        this.match = match;
    }
}