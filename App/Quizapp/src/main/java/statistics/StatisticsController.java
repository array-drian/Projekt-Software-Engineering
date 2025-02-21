package statistics;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import category.Category;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import score.ScoreDAO;
import user.CurrentUser;

@Named
@ViewScoped
public class StatisticsController implements Serializable {

    private Long totalGamesPlayed;

    private Long totalQuestionsAnswered;

    private Long totalCorrectAnswers;

    private double averageCorrectAnswers;

    private Category favouriteCategory;

    @Inject
    private CurrentUser currentUser;

    @Inject
    private ScoreDAO scoreDAO;

    @PostConstruct
    public void init() {
        loadStatistics();
    }

    //Getter

    public Long getTotalGamesPlayed() {
        return this.totalGamesPlayed;
    }

    public Long getTotalQuestionsAnswered() {
        return this.totalQuestionsAnswered;
    }

    public Long getTotalCorrectAnswers() {
        return this.totalCorrectAnswers;
    }

    public double getAverageCorrectAnswers() {
        return this.averageCorrectAnswers;
    }

    public Category getFavouriteCategory() {
        return this.favouriteCategory;
    }

    //Other
    
    //Loads all statistics
    public void loadStatistics() {
        int userID = currentUser.getUser().getUserID();
        this.totalGamesPlayed = scoreDAO.getScoreCountForUser(userID);
        this.totalQuestionsAnswered = this.totalGamesPlayed * 10;
        this.totalCorrectAnswers = scoreDAO.getTotalScoreForUser(userID);
        this.averageCorrectAnswers = (totalGamesPlayed == 0) ? 0.0 : new BigDecimal((double) totalCorrectAnswers / totalGamesPlayed)
            .setScale(2, RoundingMode.HALF_UP)
            .doubleValue();
        this.favouriteCategory = scoreDAO.getMostPlayedCategoryForUser(userID);
    }
}