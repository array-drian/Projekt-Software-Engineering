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
        Question changedQuestion = questionDAO.merge(question);

        questionDAO.persist(changedQuestion);

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Die Frage wurde erfolgreich bearbeitet.", null);
        FacesContext.getCurrentInstance().addMessage("editQuestionsForm", msg);
    }
}