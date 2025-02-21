package user;
import java.io.Serializable;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import other.App;

@Named
@ViewScoped
public class UserBean implements Serializable{

    private String userName;

    private String userPass;

    private boolean isMod = false;

    @Inject
    private UserDAO userDAO;

    @Inject
    private LoginController loginController;

    @Inject
    private App app;

    //Getter

    public String getUserName() {
        return this.userName;
    }

    public String getUserPass() {
        return this.userPass;
    }

    public boolean getIsMod() {
        return this.isMod;
    }
    
    //Setter

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public void setIsMod(boolean isMod) {
        this.isMod = isMod;
    }

    //Other
    
    //Save the current user to the Database
    public void saveToDatabase() {
        String hashedPass = app.hashPassword(userName, userPass, loginController.getSalt());
        User newUser = new User(userName, hashedPass, isMod);
        userDAO.persist(newUser);

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Dein Konto wurde erstellt.", null);
        FacesContext.getCurrentInstance().addMessage("registrationForm", msg);
    }
}