package score;

import java.io.Serializable;

import game.Game;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import user.User;

@Entity
public class Score implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int scoreID;

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "gameID", nullable = false)
    private Game game;

    private int score;

    //Constructors

    public Score() {
    }

    public Score(User user, Game game, int score) {
        this.user = user;
        this.game = game;
        this.score = score;
    }

    //Getter

    public int getScoreID() {
        return this.scoreID;
    }

    public User getUser() {
        return this.user;
    }

    public Game getGame() {
        return this.game;
    }

    public int getScore() {
        return this.score;
    }

    //Setter

    public void setUser(User user) {
        this.user = user;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setScore(int score) {
        this.score = score;
    }
}