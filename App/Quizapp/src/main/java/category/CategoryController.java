package category;

import java.io.Serializable;
import java.util.List;

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
public class CategoryController implements Serializable {

    private Category category;

    @Inject
    private CategoryDAO categoryDAO;

    @Inject
    private QuestionDAO questionDAO;

    @Inject
    private QuestionController questionController;

    //Getter

    public Category getCategory() {
        return this.category;
    }

    //Setter

    public void setCategory(Category category) {
        this.category = category;
    }

    //Other

    //Get a List of all available Categories
    public List<Category> getAllCategories() {
        return categoryDAO.getAllCategorys();
    }

    //Delete a Category
    public void deleteCurrentCategory() {
        Category deleteCategory = categoryDAO.merge(category);

        deleteCategory.setIsActive(false);

        for(Question question : deleteCategory.getQuestions()) {
            Question deletedQuestion = questionDAO.merge(question);

            deletedQuestion.setIsActive(false);

            questionDAO.merge(deletedQuestion);
        }

        categoryDAO.merge(deleteCategory);

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Die Kategorie wurde erfolgreich gelöscht.", null);
        FacesContext.getCurrentInstance().addMessage("deleteCategoryForm", msg);

        questionController.loadQuestions();
    }
}