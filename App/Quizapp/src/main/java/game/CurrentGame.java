package game;
import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@Named
@SessionScoped
public class CurrentGame implements Serializable {

    private Game game = null;

    //Getter

    public Game getGame() {
        return this.game;
    }

    //Setter

    public void setGame(Game game) {
        this.game = game;
    }

    //Other

    public boolean isValid() {
        return this.game != null;
    }

    public void reset() {
        this.game= null;
    }
}