package statistics;

import java.io.Serializable;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import score.ScoreDAO;

@Named
@ViewScoped
public class LeaderboardController implements Serializable {

    private List<Object[]> top10UsersByScoreCount;

    private List<Object[]> top10UsersByTotalScore;

    @Inject
    private ScoreDAO scoreDAO;

    @PostConstruct
    public void init() {
        loadLeaderboard();
    }

    //Getter

    public List<Object[]> getTop10UsersByScoreCount() {
        return this.top10UsersByScoreCount;
    }

    public List<Object[]> getTop10UsersByTotalScore() {
        return this.top10UsersByTotalScore;
    }

    //Other
    
    //Loads leaderboard statistics
    public void loadLeaderboard() {
        this.top10UsersByScoreCount = scoreDAO.getTop10UsersByScoreCount();
        this.top10UsersByTotalScore = scoreDAO.getTop10UsersByTotalScore();
    }
}