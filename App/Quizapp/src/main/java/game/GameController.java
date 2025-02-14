package game;

import java.io.Serializable;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.NavigationHandler;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import user.CurrentUser;

@Named
@ViewScoped
public class GameController implements Serializable {
    private int index = 0;
    private List<Game> pendingGames;
    private Game game;

    @Inject
    private CurrentUser currentUser;

    @Inject
    private GameDAO gameDAO;

    @Inject
    private CurrentGame currentGame;

    @PostConstruct
    public void init() {
        loadGames();
    }

    public void loadGames() {
        this.pendingGames = gameDAO.getPendingGamesForUser(currentUser.getUser().getUserID());
        if (pendingGames != null && !pendingGames.isEmpty()) {
            this.game = pendingGames.get(index);
        } else {
            this.game = null;
        }
    }
    
    public void nextGame() {
        if (this.pendingGames != null && !this.pendingGames.isEmpty()) {
            this.index += 1;
            this.game = this.pendingGames.get(this.index);
        } else {
            this.game = null;
        }
    }

    public void previousGame() {
        if (this.pendingGames != null && !this.pendingGames.isEmpty()) {
            this.index -=1 ;
            this.game = this.pendingGames.get(this.index);
        } else {
            this.game = null;
        }
    }

    public Game getGame() {
        return this.game;
    }

    public int getIndex() {
        return this.index;
    }

    public int getMaxIndex() {
        return this.pendingGames != null ? this.pendingGames.size() - 1 : 0;
    }

    public void checkGame() {
        if(!currentGame.isValid()) {
            FacesContext fc = FacesContext.getCurrentInstance();
            NavigationHandler nh = fc.getApplication().getNavigationHandler();
            nh.handleNavigation(fc, null, "quiz.xhtml?faces-redirect=true");
        }
    }

    public void playGame(Game game) {
        if(game != null) {
            this.currentGame.setGame(game);
            FacesContext fc = FacesContext.getCurrentInstance();
            NavigationHandler nh = fc.getApplication().getNavigationHandler();
            nh.handleNavigation(fc, null, "game.xhtml?faces-redirect=true");
        }
    }
}