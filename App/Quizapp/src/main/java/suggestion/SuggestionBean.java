package suggestion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import answer.Answer;
import category.Category;
import category.CategoryDAO;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import question.Question;
import user.CurrentUser;
import user.User;

@Named
@ViewScoped
public class SuggestionBean implements Serializable{

    private String question;
    private Category category;
    private String correctAnswer;
    private List<String> answers = new ArrayList<>();
    
    @Inject
    private CurrentUser currentUser;

    @Inject
    private SuggestionDAO suggestionDAO;

    @Inject
    private CategoryDAO categoryDAO;


    //Constructor
    public SuggestionBean() {
        for (int i = 0; i < 3; i++) {
            answers.add("");
        }
    }

    //Getter

    public String getQuestion() {
        return this.question;
    }

    public Category getCategory() {
        return this.category;
    }

    public String getCorrectAnswer() {
        return this.correctAnswer;
    }

    public List<String> getAnswers() {
        return this.answers;
    }

    //Setter

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    //Other

    public List<Category> getAllCategories() {
        return categoryDAO.getAllCategorys();
    }

    public void submitSuggestion() {
        User user = currentUser.getUser();
        if (user == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No logged-in user found."));
            return;
        }
    
        if (question == null || question.trim().isEmpty() || category == null || correctAnswer.trim().isEmpty() || answers.size() != 3) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid input."));
            return;
        }
    
        Question newQuestion = new Question(question, category);
    
        Answer newCorrectAnswer = new Answer(correctAnswer, newQuestion, true);
        newQuestion.getAnswers().add(newCorrectAnswer);
    
        for (String answerText : answers) {
            Answer answer = new Answer(answerText, newQuestion);
            newQuestion.getAnswers().add(answer);
        }
    
        category.getQuestions().add(newQuestion);
        categoryDAO.persist(category);
    
        Suggestion suggestion = new Suggestion(newQuestion, user);
        suggestionDAO.persist(suggestion);
    
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Suggestion submitted successfully!"));
    }    
    
}