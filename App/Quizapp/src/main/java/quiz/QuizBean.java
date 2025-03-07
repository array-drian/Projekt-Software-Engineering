package quiz;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import category.Category;
import category.CategoryDAO;
import game.Game;
import game.GameDAO;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import question.Question;
import question.QuestionDAO;
import user.CurrentUser;
import user.User;

@Named
@ViewScoped
public class QuizBean implements Serializable {

    private List<Question> questions = new ArrayList<>();

    private Category category;

    private boolean isMultiplayer = false;

    private int maxPlayers = 0;

    @Inject
    private CategoryDAO categoryDAO;

    @Inject
    private QuestionDAO questionDAO;

    @Inject
    private GameDAO gameDAO;

    @Inject
    private CurrentUser currentUser;

    //Getter

    public List<Question> getQuestions() {
        return this.questions;
    }

    public Category getCategory() {
        return this.category;
    }

    public boolean getIsMultiplayer() {
        return this.isMultiplayer;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    //Setter

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setIsMultiplayer(boolean isMultiplayer) {
        this.isMultiplayer = isMultiplayer;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    //Other

    //Get a List of all available Categories
    public List<Category> getAllCategories() {
        return categoryDAO.getAllCategorys();
    }

    //Create a Quiz
    public void createQuiz() {
        User user = currentUser.getUser();
        this.questions = questionDAO.getRandomQuestions(category.getCategoryID());
        if(category == null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Deine Eingaben sind ungültig.", null);
            FacesContext.getCurrentInstance().addMessage("createQuizForm", msg);
            return;
        }

        if(questions.size() != 10) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Es sind nicht genügend Fragen vorhanden.", null);
            FacesContext.getCurrentInstance().addMessage("createQuizForm", msg);
            return;
        }
    
        Quiz newQuiz = new Quiz(category);
    
        for(Question question : questions) {
            newQuiz.getQuestions().add(question);
        }
        
        Game newGame = new Game(isMultiplayer, newQuiz);

        if(this.isMultiplayer) {
           newGame.setMaxPlayers(maxPlayers);
        }

        newGame.getUsers().add(user);
        gameDAO.persist(newGame);

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Das Quiz wurde erfolgreich erstellt.", null);
        FacesContext.getCurrentInstance().addMessage("createQuizForm", msg);
    }
}