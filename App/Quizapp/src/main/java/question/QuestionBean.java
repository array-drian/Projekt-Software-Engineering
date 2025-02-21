package question;

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

@Named
@ViewScoped
public class QuestionBean implements Serializable {

    private String question;

    private Category category;

    private String correctAnswer;

    private List<String> answers = new ArrayList<>();

    @Inject
    private CategoryDAO categoryDAO;

    //Constructor

    public QuestionBean() {
        for(int i = 0; i < 3; i++) {
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

    //Other

    //Get a List of all available Categories
    public List<Category> getAllCategories() {
        return categoryDAO.getAllCategorys();
    }

    //Create a Question
    public void createQuestion() {
        if(question == null || question.trim().isEmpty() || category == null ||correctAnswer.trim().isEmpty() || answers.size() != 3) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Deine Eingaben sind ungültig.", null);
            FacesContext.getCurrentInstance().addMessage("createQuestionsForm", msg);
            return;
        }
    
        Question newQuestion = new Question(question, category, true);
    
        Answer newCorrectAnswer = new Answer(correctAnswer, newQuestion, true);
        newQuestion.getAnswers().add(newCorrectAnswer);
    
        for(String answerText : answers) {
            Answer answer = new Answer(answerText, newQuestion);
            newQuestion.getAnswers().add(answer);
        }
    
        category.getQuestions().add(newQuestion);
        categoryDAO.persist(category);
    
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Die Frage wurde erfolgreich erstellt.", null);
        FacesContext.getCurrentInstance().addMessage("createQuestionsForm", msg);
    }
}