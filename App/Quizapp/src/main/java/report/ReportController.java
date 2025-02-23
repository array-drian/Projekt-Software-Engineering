package report;

import java.io.Serializable;
import java.util.List;

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
public class ReportController implements Serializable {

    private int index = 0;

    private List<Report> pendingReports;

    private Report report;

    @Inject
    private ReportDAO reportDAO;

    @Inject
    private QuestionDAO questionDAO;

    @Inject
    private QuestionController questionController;

    @PostConstruct
    public void init() {
        loadReports();
    }

    //Getter

    public List<Report> getPendingReports() {
        return this.pendingReports;
    }

    public Report getReport() {
        return this.report;
    }

    public int getIndex() {
        return this.index;
    }

    public int getMaxIndex() {
        return this.pendingReports != null ? this.pendingReports.size() - 1 : 0;
    }

    //Other
    
    //Loads all pending Reports from the Database
    public void loadReports() {
        this.pendingReports = reportDAO.getPendingReports();
        if(pendingReports != null && !pendingReports.isEmpty()) {
            this.report = pendingReports.get(index);
        }else {
            this.report = null;
        }
    }

    //Changes a Question
    public void changeQuestion() {
        Question changedQuestion = questionDAO.merge(this.report.getQuestion());

        questionDAO.persist(changedQuestion);

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Die Frage wurde erfolgreich bearbeitet.", null);
        FacesContext.getCurrentInstance().addMessage("reportsForm", msg);

        questionController.loadQuestions();

        settleReport();
    }

    //Decline a Report
    public void declineReport() {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Die Meldung wurde abgelehnt.", null);
        FacesContext.getCurrentInstance().addMessage("reportsForm", msg);

        settleReport();
    }

    //Settles the Report
    public void settleReport() {
        if(this.report != null) {
            this.report.setIsActive(false);
            reportDAO.merge(this.report);
        }
        settled();
    }
    
    //Removes Report form list and selects next Report
    public void settled() {
        this.pendingReports.remove(this.report);
        if(this.pendingReports != null && !this.pendingReports.isEmpty()) {
            this.index = Math.min(Math.max(this.index, 0), this.pendingReports.size() - 1);
            this.report = this.pendingReports.get(this.index);
        }else {
            this.index = 0;
            this.report = null;
        }
    }
    
    //Selects next Report
    public void nextSuggestion() {
        if(this.pendingReports != null && !this.pendingReports.isEmpty()) {
            this.index += 1;
            this.report = this.pendingReports.get(this.index);
        }else {
            this.report = null;
        }
    }

    //Selects previous Report
    public void previousSuggestion() {
        if(this.pendingReports != null && !this.pendingReports.isEmpty()) {
            this.index -=1 ;
            this.report = this.pendingReports.get(this.index);
        }else {
            this.report = null;
        }
    }
}