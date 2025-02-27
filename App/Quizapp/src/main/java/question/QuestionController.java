package question;

import java.io.Serializable;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.PersistenceException;


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

        try {
            questionDAO.persist(changedQuestion);
    
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Die Frage wurde erfolgreich bearbeitet.", null);
            FacesContext.getCurrentInstance().addMessage("editQuestionsForm", msg);
        }catch (PersistenceException  e) {
            if (e.getMessage() != null && e.getMessage().contains("Duplicate entry")) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Die frage existiert bereits.", null);
                FacesContext.getCurrentInstance().addMessage("editQuestionsForm", msg);
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ein Fehler ist beim Speichern aufgetreten.", null);
                FacesContext.getCurrentInstance().addMessage("editQuestionsForm", msg);
            }
        }
    }

    //Delete a Question
    public void deleteQuestion(Question question) {
        Question deletedQuestion = questionDAO.merge(question);

        deletedQuestion.setIsActive(false);

        questionDAO.persist(deletedQuestion);

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Die Frage wurde erfolgreich gelöscht.", null);
        FacesContext.getCurrentInstance().addMessage("editQuestionsForm", msg);

        loadQuestions();
    }
}