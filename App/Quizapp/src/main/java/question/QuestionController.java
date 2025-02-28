package question;

import java.io.Serializable;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;


@Named
@ViewScoped
public class QuestionController implements Serializable {
    
    private List<Question> allQuestions;

    @Inject
    private QuestionDAO questionDAO;

    @PostConstruct
    public void init() {
        loadQuestions();
    }

    //Getter

    public List<Question> getAllQuestions() {
        return this.allQuestions;
    }

    //Other

    //Load all Question entities fomr the Database
    public void loadQuestions() {
        this.allQuestions = questionDAO.getAllQuestions();
    }

    //Change a Question
    public void changeQuestion(Question question) {
        if(questionDAO.checkQuestion(question) > 0) {
            Question revertQuestion = questionDAO.getQuestionAtIndex(question.getQuestionID());

            question.setQuestion(revertQuestion.getQuestion());

            for (int i = 0; i < Math.min(question.getAnswers().size(), revertQuestion.getAnswers().size()); i++) {
                question.getAnswers().get(i).setAnswer(revertQuestion.getAnswers().get(i).getAnswer());
            }

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Die frage existiert bereits.", null);
            FacesContext.getCurrentInstance().addMessage("editQuestionsForm", msg);
            return;
        }

        Question changedQuestion = questionDAO.merge(question);

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Die Frage wurde erfolgreich bearbeitet.", null);
        FacesContext.getCurrentInstance().addMessage("editQuestionsForm", msg);
    }

    //Delete a Question
    public void deleteQuestion(Question question) {
        question.setIsActive(false);

        Question deletedQuestion = questionDAO.merge(question);

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Die Frage wurde erfolgreich gelöscht.", null);
        FacesContext.getCurrentInstance().addMessage("editQuestionsForm", msg);

        loadQuestions();
    }
}