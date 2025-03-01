package category;

import java.io.Serializable;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import  jakarta.inject.Named;
import jakarta.persistence.PersistenceException;

@Named
@ViewScoped
public class CategoryBean implements Serializable {

    private String category;

    @Inject
    private CategoryDAO categoryDAO;

    //Getter

    public String getCategory() {
        return this.category;
    }

    //Setter

    public void setCategory(String category) {
        this.category = category;
    }

    //Other
    
    //Save the current Entity to the Database
    public void saveToDatabase() {
        Category newCategory = new Category(category);
        try {
            categoryDAO.persist(newCategory);

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Die Kategorie " + category + " wurde erfolgreich erstellt.", null);
            FacesContext.getCurrentInstance().addMessage("createCategoryForm", msg);
        }catch (PersistenceException  e) {
            if (e.getMessage() != null && e.getMessage().contains("Duplicate entry")) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Die Kategorie " + category + " existiert bereits.", null);
                FacesContext.getCurrentInstance().addMessage("createCategoryForm", msg);
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ein Fehler ist beim Speichern aufgetreten.", null);
                FacesContext.getCurrentInstance().addMessage("createCategoryForm", msg);
            }
        }
    }
}