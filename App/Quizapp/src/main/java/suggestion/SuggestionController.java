package suggestion;

import java.io.Serializable;
import java.util.List;

import category.Category;
import category.CategoryDAO;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import question.Question;
import question.QuestionController;
import question.QuestionDAO;

@Named
@ViewScoped
public class SuggestionController implements Serializable {

    private int index = 0;

    private List<Suggestion> pendingSuggestions;

    private Suggestion suggestion;

    @Inject
    private SuggestionDAO suggestionDAO;

    @Inject
    private QuestionDAO questionDAO;

    @Inject
    private CategoryDAO categoryDAO;

    @Inject
    private QuestionController questionController;

    @PostConstruct
    public void init() {
        loadSuggestions();
    }

    //Getter

    public Suggestion getSuggestion() {
        return this.suggestion;
    }

    public int getIndex() {
        return this.index;
    }

    public int getMaxIndex() {
        return this.pendingSuggestions != null ? this.pendingSuggestions.size() - 1 : 0;
    }

    //Other
    
    //Loads all suggestions from the Database
    public void loadSuggestions() {
        this.pendingSuggestions = suggestionDAO.getPendingSuggestions();
        if(pendingSuggestions != null && !pendingSuggestions.isEmpty()) {
            this.suggestion = pendingSuggestions.get(index);
        }else {
            this.suggestion = null;
        }
    }

    //Accepts a Suggestion
    public void acceptSuggestion() {
        if(this.suggestion != null) {
            this.suggestion.setIsAccepted(true);
            suggestionDAO.merge(this.suggestion);

            Question question = suggestion.getQuestion();
            question.setIsActive(true);
            questionDAO.merge(question);
        }
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Der Vorschlag wurde akzeptiert.", null);
        FacesContext.getCurrentInstance().addMessage("suggestionsForm", msg);
        
        questionController.loadQuestions();

        acceptedOrDeclined();
    }
    
    //Declines a suggestion
    public void declineSuggestion() {
        if(this.suggestion != null) {
            Suggestion deletedSuggestion = suggestionDAO.merge(this.suggestion);

            suggestionDAO.remove(deletedSuggestion);
    
            Question question = this.suggestion.getQuestion();
            if(!question.getIsActive()) {
                Category category = categoryDAO.merge(question.getCategory());
                category.getQuestions().remove(question);
                categoryDAO.merge(category);
            }
        }
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Der Vorschlag wurde abgelehnt.", null);
        FacesContext.getCurrentInstance().addMessage("suggestionsForm", msg);
        acceptedOrDeclined();
    }

    //Deletes the accepted/declined suggestions from the list and selects the next
    public void acceptedOrDeclined() {
        this.pendingSuggestions.remove(this.suggestion);
        if(this.pendingSuggestions != null && !this.pendingSuggestions.isEmpty()) {
            this.index = Math.min(Math.max(this.index, 0), this.pendingSuggestions.size() - 1);
            this.suggestion = this.pendingSuggestions.get(this.index);
        }else {
            this.index = 0;
            this.suggestion = null;
        }
    }
    
    //Selects the next suggestion
    public void nextSuggestion() {
        if(this.pendingSuggestions != null && !this.pendingSuggestions.isEmpty()) {
            this.index += 1;
            this.suggestion = this.pendingSuggestions.get(this.index);
        }else {
            this.suggestion = null;
        }
    }

    //Selects the previous suggestion
    public void previousSuggestion() {
        if(this.pendingSuggestions != null && !this.pendingSuggestions.isEmpty()) {
            this.index -=1 ;
            this.suggestion = this.pendingSuggestions.get(this.index);
        }else {
            this.suggestion = null;
        }
    }
}