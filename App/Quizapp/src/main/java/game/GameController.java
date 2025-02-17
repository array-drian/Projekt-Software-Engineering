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
    private List<Game> pendingSingleplayerGames;
    private List<Game> pendingMultiplayerGames;
    private List<Game> waitingMultiplayerGamesWithoutUser;
    private List<Game> waitingMultiplayerGamesForUser;

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
        this.pendingSingleplayerGames = gameDAO.getPendingSingleplayerGamesForUser(currentUser.getUser().getUserID());
        this.pendingMultiplayerGames = gameDAO.getPendingMultiplayerGamesForUser(currentUser.getUser().getUserID());
        this.waitingMultiplayerGamesWithoutUser = gameDAO.getWaitingMultiplayerGamesWithoutUser(currentUser.getUser().getUserID());
        this.waitingMultiplayerGamesForUser = gameDAO.getWaitingMultiplayerGamesForUser(currentUser.getUser().getUserID());
    }

    public List<Game> getPendingSingleplayerGames() {
        return this.pendingSingleplayerGames;
    }

    public List<Game> getPendingMultiplayerGames() {
        return this.pendingMultiplayerGames;
    }

    public List<Game> getWaitingMultiplayerGamesWithoutUser() {
        return this.waitingMultiplayerGamesWithoutUser;
    }

    public List<Game> getWaitingMultiplayerGamesForUser() {
        return this.waitingMultiplayerGamesForUser;
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

    public void joinGame(Game game) {
        if(game != null) {
            game.getUsers().add(currentUser.getUser());
            gameDAO.persist(game);

            loadGames();
        }
    }
}