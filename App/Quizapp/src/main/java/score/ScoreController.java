package score;

import java.io.Serializable;
import java.util.List;

import game.Game;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import user.CurrentUser;

@Named
@ViewScoped
public class ScoreController implements Serializable {
    private List<Game> finishedSingleplayerGamesForUser;
    private List<Game> finishedMultiplayerGamesForUser;
    private List<Game> playedButUnfinishedMultiplayerGamesForUser;

    @Inject
    private CurrentUser currentUser;

    @Inject
    private ScoreDAO scoreDAO;

    @PostConstruct
    public void init() {
        loadGames();
    }

    public void loadGames() {
        int userID = currentUser.getUser().getUserID();
        this.finishedSingleplayerGamesForUser = scoreDAO.getFinishedSingleplayerGamesForUser(userID);
        this.finishedMultiplayerGamesForUser = scoreDAO.getFinishedMuliplayerGamesForUser(userID);
        this.playedButUnfinishedMultiplayerGamesForUser = scoreDAO.getPlayedButUnfinishedMuliplayerGamesForUser(userID);
    }

    public int getScoreForUserInGame(Game game) {
        if (game != null) {
            for (Score score : game.getScores()) {
                if (score.getUser().getUserID() == currentUser.getUser().getUserID()) {
                    return score.getScore();
                }
            }
        }
        return 0;
    }

    public List<Game> getFinishedSingleplayerGamesForUser() {
        return this.finishedSingleplayerGamesForUser;
    }

    public List<Game> getFinishedMultiplayerGamesForUser() {
        return this.finishedMultiplayerGamesForUser;
    }

    public List<Game> getPlayedButUnfinishedMultiplayerGamesForUser() {
        return this.playedButUnfinishedMultiplayerGamesForUser;
    }
}