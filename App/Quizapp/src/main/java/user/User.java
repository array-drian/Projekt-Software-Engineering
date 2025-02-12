package user;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import game.Game;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import suggestion.Suggestion;

@Entity
public class User implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userID;

    private String userName;

    private String userPass;

    @Column(nullable = false)
    private boolean isMod = false;

    @OneToMany(mappedBy = "byUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Suggestion> suggestions = new ArrayList<>();

    @ManyToMany(mappedBy = "users")
    private List<Game> matches = new ArrayList<>();

    //Constructor

    public User() {
    }

    public User(String userName, String userPass) {
        this.userName = userName;
        this.userPass = userPass;
    }

    public User(String userName, String userPass, boolean isMod) {
        this.userName = userName;
        this.userPass = userPass;
        this.isMod = isMod;
    }

    //Getter

    public int getUserId() {
        return this.userID;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getUserPass() {
        return this.userPass;
    }

    public boolean getIsMod() {
        return this.isMod;
    }

    public List<Game> getMatches() {
        return this.matches;
    }

    //Setter

    public void setName(String userName) {
        this.userName = userName;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public void setIsMod(boolean isMod) {
        this.isMod = isMod;
    }
}