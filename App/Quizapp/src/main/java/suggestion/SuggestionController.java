package suggestion;

import java.io.Serializable;
import java.util.List;

import answer.AnswerDAO;
import jakarta.annotation.PostConstruct; // Add this import
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import question.Question;
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
    private AnswerDAO answerDAO;

    @PostConstruct
    public void init() {
        loadSuggestions();
    }

    public void loadSuggestions() {
        pendingSuggestions = suggestionDAO.getPendingSuggestions();
        if (pendingSuggestions != null && !pendingSuggestions.isEmpty()) {
            suggestion = pendingSuggestions.get(index);
        } else {
            suggestion = null;
        }
    }

    public void acceptSuggestion() {
        if (suggestion != null) {
            suggestion.setIsAccepted(true);
            suggestionDAO.merge(suggestion);

            Question question = suggestion.getQuestion();
            question.setIsActive(true);
            questionDAO.merge(question);
        }
        nextSuggestion();
    }
    
    public void declineSuggestion() {
        if (suggestion != null) {
            suggestionDAO.remove(suggestion);
    
            Question question = suggestion.getQuestion();
            if (!question.getIsActive()) {
                questionDAO.merge(question);
                questionDAO.remove(question);
            }
        }
        nextSuggestion();
    }
    
    public void nextSuggestion() {
        if (pendingSuggestions != null && !pendingSuggestions.isEmpty()) {
            index = Math.min(index + 1, pendingSuggestions.size() - 1);
            suggestion = pendingSuggestions.get(index);
        } else {
            suggestion = null;
        }
    }

    public void previousSuggestion() {
        if (pendingSuggestions != null && !pendingSuggestions.isEmpty()) {
            index = Math.max(index - 1, 0);
            suggestion = pendingSuggestions.get(index);
        } else {
            suggestion = null;
        }
    }

    public Suggestion getSuggestion() {
        return suggestion;
    }

    public int getIndex() {
        return index;
    }

    public int getMaxIndex() {
        return pendingSuggestions != null ? pendingSuggestions.size() - 1 : 0;
    }
}