package category;

import java.io.Serializable;
import java.util.List;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;


@Named
@ViewScoped
public class CategoryController implements Serializable {

    private Category category;

    @Inject
    private CategoryDAO categoryDAO;

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

        categoryDAO.persist(deleteCategory);

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Die Kategorie wurde erfolgreich gelöscht.", null);
        FacesContext.getCurrentInstance().addMessage("deleteCategoryForm", msg);
    }
}