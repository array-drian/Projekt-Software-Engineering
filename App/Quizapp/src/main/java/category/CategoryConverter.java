package category;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("categoryConverter")
@ApplicationScoped
public class CategoryConverter implements Converter<Category> {

    @Inject
    private CategoryDAO categoryDAO;

    //Convert a String to an Object
    @Override
    public Category getAsObject(FacesContext context, UIComponent component, String value) {
        if(value == null || value.trim().isEmpty()) {
            return null;
        }
        return categoryDAO.getCategoryAtIndex(Integer.parseInt(value));
    }

    //Convert an Object to a String
    @Override
    public String getAsString(FacesContext context, UIComponent component, Category category) {
        if(category == null) {
            return "";
        }
        return String.valueOf(category.getCategoryID());
    }
}
