package game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import quiz.Quiz;
import score.Score;
import user.User;

@Entity
public class Game implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int gameID;

    @Column(nullable = false)
    private boolean isMultiplayer = false;

    @ManyToMany(cascade = { CascadeType.MERGE })
    @JoinTable(
        name = "game_user",
        joinColumns = @JoinColumn(name = "gameID"),
        inverseJoinColumns = @JoinColumn(name = "userID")
    )
    private List<User> users = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)  
    @JoinColumn(name = "quizID", unique = true, nullable = false)  
    private Quiz quiz;

    @Column(nullable = false)
    private boolean isFinished = false;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Score> scores = new ArrayList<>();

    // Constructors

    public Game() {
    }

    public Game(boolean isMultiplayer, Quiz quiz) {
        this.isMultiplayer = isMultiplayer;
        this.quiz = quiz;
    }

    // Getter methods
    public int getGameID() {
        return this.gameID;
    }

    public boolean isMultiplayer() {
        return this.isMultiplayer;
    }

    public List<User> getUsers() {
        return this.users;
    }

    public boolean isFinished() {
        return this.isFinished;
    }

    public Quiz getQuiz() {
        return this.quiz;
    }

    public List<Score> getScores() {
        return this.scores;
    }

    // Setter methods

    public void setIsMultiplayer(boolean isMultiplayer) {
        this.isMultiplayer = isMultiplayer;
    }

    public void setIsFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }
}