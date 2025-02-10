package category;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import  jakarta.inject.Named;

@Named
@RequestScoped
public class CategoryBean {

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
    public void saveToDatabase() {
        Category newCategory = new Category(category);
        categoryDAO.persist(newCategory);
    }
}