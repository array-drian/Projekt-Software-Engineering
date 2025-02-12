package quiz;

import java.util.ArrayList;
import java.util.List;

import category.Category;
import category.CategoryDAO;
import game.Game;
import game.GameDAO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import question.Question;
import question.QuestionDAO;
import user.CurrentUser;
import user.User;

@Named
@RequestScoped
public class QuizBean {

    private List<Question> questions = new ArrayList<>();
    private Category category;
    private boolean isMultiplayer = false;

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

    //Setter

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setIsMultiplayer(boolean isMultiplayer) {
        this.isMultiplayer = isMultiplayer;
    }

    //Other

    public List<Category> getAllCategories() {
        return categoryDAO.getAllCategorys();
    }

    public void createQuiz() {
        User user = currentUser.getUser();
        this.questions = questionDAO.getRandomQuestions(category.getCategoryID());
        if (category == null || questions.size() != 10) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid input."));
            return;
        }
    
        Quiz newQuiz = new Quiz(category);
    
        for (Question question : questions) {
            newQuiz.getQuestions().add(question);
        }
        
        Game newGame = new Game(isMultiplayer, newQuiz);
        newGame.getUsers().add(user);
        gameDAO.persist(newGame);
    
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Quiz created successfully!"));
    }
}