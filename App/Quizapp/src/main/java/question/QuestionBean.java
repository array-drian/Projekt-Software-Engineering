package question;

import java.util.ArrayList;
import java.util.List;

import answer.Answer;
import answer.AnswerDAO;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@RequestScoped
public class QuestionBean {

    private String question;
    private String category;
    private String correctAnswer;
    private List<String> answers = new ArrayList<>();

    @Inject
    private QuestionDAO questionDAO;
    
    @Inject
    private AnswerDAO answerDAO;

    //Constructor
    public QuestionBean() {
        for (int i = 0; i < 3; i++) {
            answers.add("");
        }
    }

    //Getter

    public String getQuestion() {
        return this.question;
    }

    public String getCategory() {
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

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    //Other

    public void createQuestion() {
        if (question == null || question.trim().isEmpty() || correctAnswer.trim().isEmpty() || answers.size() != 3) {
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
    
        questionDAO.persist(newQuestion);
    
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Question created successfully!"));
    }
}