package suggestion;

import java.util.ArrayList;
import java.util.List;

import answer.Answer;
import answer.AnswerDAO;
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
public class SuggestionBean {

    private String question;
    private String category;
    private String correctAnswer;
    private List<String> answers = new ArrayList<>();
    
    @Inject
    private CurrentUser currentUser;

    @Inject
    private SuggestionDAO suggestionDAO;
    
    @Inject
    private AnswerDAO answerDAO;

    @Inject
    private QuestionDAO questionDAO;

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

    public void submitSuggestion() {
        User user = currentUser.getUser();
        if (user == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No logged-in user found."));
            return;
        }

        if (question == null || question.trim().isEmpty() || correctAnswer.trim().isEmpty() ||answers.size() != 3) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid input."));
            return;
        }

        Question newQuestion = new Question(question, category);
        questionDAO.persist(newQuestion);

        Answer newCorrectAnswer = new Answer(correctAnswer, newQuestion, true);
        answerDAO.persist(newCorrectAnswer);

        for (String answerText : answers) {
            Answer answer = new Answer(answerText, newQuestion);
            answerDAO.persist(answer);
        }

        Suggestion suggestion = new Suggestion(newQuestion, user);
        suggestionDAO.persist(suggestion);

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Suggestion submitted successfully!"));
    }
}