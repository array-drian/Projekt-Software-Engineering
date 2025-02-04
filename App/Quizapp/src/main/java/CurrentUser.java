import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@Named
@SessionScoped
public class CurrentUser implements Serializable {

    private User user = null;

    public void reset() {
        this.user= null;
    }

    //Getter

    public User getUser() {
        return this.user;
    }

    //Setter

    public void setUser(User user) {
        this.user = user;
    }

    //Other

    public boolean isValid() {
        return this.user != null;
    }

}