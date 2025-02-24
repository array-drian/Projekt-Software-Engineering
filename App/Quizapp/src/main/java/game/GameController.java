package game;

import java.io.Serializable;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.application.NavigationHandler;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import quiz.QuizBean;
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

    @Inject
    private QuizBean quizBean;

    @PostConstruct
    public void init() {
        loadGames();
    }

    //Getter

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

    //Other

    //Fill all Lists with Data fomr the Database
    public void loadGames() {
        int userID = currentUser.getUser().getUserID();
        this.pendingSingleplayerGames = gameDAO.getPendingSingleplayerGamesForUser(userID);
        this.pendingMultiplayerGames = gameDAO.getPendingMultiplayerGamesForUser(userID);
        this.waitingMultiplayerGamesWithoutUser = gameDAO.getWaitingMultiplayerGamesWithoutUser(userID);
        this.waitingMultiplayerGamesForUser = gameDAO.getWaitingMultiplayerGamesForUser(userID);
    }

    //Create a quiz
    public void createGame() {
        quizBean.createQuiz();
        loadGames();
    }

    //Play a Game
    public void playGame(Game game) {
        if(game != null) {
            this.currentGame.setGame(game);
            FacesContext fc = FacesContext.getCurrentInstance();
            NavigationHandler nh = fc.getApplication().getNavigationHandler();
            nh.handleNavigation(fc, null, "game.xhtml?faces-redirect=true");
        }
    }

    //Join a multiplayer Game
    public void joinGame(Game game) {
        if(game != null) {
            Game joinedGame = gameDAO.merge(game);
            joinedGame.getUsers().add(currentUser.getUser());
            gameDAO.persist(joinedGame);

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Du bist dem Spiel beigetreten.", null);
            FacesContext.getCurrentInstance().addMessage("joinGameForm", msg);

            loadGames();
        }
    }
}