package category;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import question.Question;
import quiz.Quiz;

@Entity
public class Category implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryID;

    private String category;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Quiz> quizzes = new ArrayList<>();

    @Column(nullable = false)
    private boolean isActive = true;

    //Constructor

    public Category() {
    }

    public Category(String category) {
        this.category = category;
    }

    //Getter

    public int getCategoryID() {
        return this.categoryID;
    }

    public String getCategory() {
        return this.category;
    }

    public List<Question> getQuestions() {
        return this.questions;
    }

    public List<Quiz> getQuizzes() {
        return this.quizzes;
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    //Setter

    public void setCategory(String category) {
        this.category = category;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}