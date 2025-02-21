package quiz;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import answer.Answer;
import game.CurrentGame;
import game.GameDAO;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.application.NavigationHandler;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import question.Question;
import question.QuestionDAO;
import report.Report;
import score.Score;
import user.CurrentUser;

@Named
@ViewScoped
public class QuizController implements Serializable {

    private int index = 0;

    private List<Question> pendingQuestions;

    private Question question;

    private int submittedAnswers = 0;

    private boolean reportCurrentQuestion = false;

    private String message = "";

    private List<Answer> answersForCurrentQuestion;

    private int score = 0;

    @Inject
    private CurrentGame currentGame;

    @Inject
    private CurrentUser currentUser;

    @Inject
    private GameDAO gameDAO;

    @Inject
    private QuestionDAO questionDAO;

    @PostConstruct
    public void init() {
        loadQuestions();
    }

    //Getter

    public Question getQuestion() {
        return this.question;
    }

    public List<Answer> getAnswers() {
        return this.answersForCurrentQuestion;
    }

    public int getScore() {
        return this.score;
    }

    public int getIndex() {
        return this.index;
    }

    public int getSubmittedAnswers() {
        return this.submittedAnswers;
    }

    public int getMaxIndex() {
        return this.pendingQuestions != null ? this.pendingQuestions.size() - 1 : 0;
    }

    public boolean getReportCurrentQuestion() {
        return this.reportCurrentQuestion;
    }

    public String getMessage() {
        return this.message;
    }

    //Setter

    public void setReportCurrentQuestion(boolean reportCurrentQuestion) {
        this.reportCurrentQuestion = reportCurrentQuestion;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    //Other

    //Loads all Question entities from a Quiz entity and shuffles the Answer choices
    public void loadQuestions() {
        this.pendingQuestions = currentGame.getGame().getQuiz().getQuestions();
        if(pendingQuestions != null && !pendingQuestions.isEmpty()) {
            this.question = pendingQuestions.get(index);
            this.answersForCurrentQuestion = this.question.getAnswers();
            Collections.shuffle(answersForCurrentQuestion);
        }else {
            this.question = null;
            this.answersForCurrentQuestion = null;
        }
    }
    
    //Loads the next Question entity and shuffles the Answer choices
    public void nextQuestion() {
        if(this.pendingQuestions != null && !this.pendingQuestions.isEmpty()) {
            this.index += 1;
            this.question = this.pendingQuestions.get(this.index);
            this.answersForCurrentQuestion = this.question.getAnswers();
            Collections.shuffle(answersForCurrentQuestion);
        }else {
            this.question = null;
            this.answersForCurrentQuestion = null;
        }
    }

    //Validates the given Answer
    public void validateAnswer(Answer answer) {
        if(answer != null) {
            if(this.submittedAnswers < 10) {
                if(answer.getIsCorrect()) {
                    this.score +=1;
                }
                this.submittedAnswers += 1;
            }
            if(this.index != getMaxIndex()) {
                nextQuestion();
            }
        }
    }

    //Create a Report for a Question
    public void reportQuestion() {
        Report newReport = new Report(this.question, currentUser.getUser(), this.message);
    
        question = questionDAO.merge(question);

        question.getReports().add(newReport);

        questionDAO.persist(question);
    
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Deine Meldung war erfolgreich.", null);
        FacesContext.getCurrentInstance().addMessage("quizForm", msg);

        this.reportCurrentQuestion = false;
        this.message = "";
    }

    //Ends the game and saves the score
    public void endGame(){
        Score newScore = new Score(currentUser.getUser(), currentGame.getGame(), score);
        currentUser.getUser().getScores().add(newScore);
        currentGame.getGame().getScores().add(newScore);
        if(!currentGame.getGame().isMultiplayer()) {
            currentGame.getGame().setIsFinished(true);
        }else {
            if(currentGame.getGame().getUsers().size() == currentGame.getGame().getScores().size()) {
                currentGame.getGame().setIsFinished(true);
            }
        }
        

        gameDAO.persist(currentGame.getGame());

        currentGame.reset();

        FacesContext fc = FacesContext.getCurrentInstance();
        NavigationHandler nh = fc.getApplication().getNavigationHandler();
        nh.handleNavigation(fc, null, "quiz.xhtml?faces-redirect=true");
    }
}