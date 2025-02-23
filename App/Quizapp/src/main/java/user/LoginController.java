package user;
import java.io.Serializable;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIInput;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.faces.validator.ValidatorException;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import other.App;

@Named
@ViewScoped
public class LoginController implements Serializable {

    @Inject
    private App app;

    @Inject
    private CurrentUser currentUser;

    private static final String salt = "H7sk6V725NVxqDq05DVnraZV";

    private String userName;

    private String userPass;

    private String tempUsername;

    private boolean login = true;

    //Getter

    public String getUserName() {
        return this.userName;
    }

    public String getUserPass() {
        return this.userPass;
    }

    public String getSalt() {
        return this.salt;
    }

    public boolean getLogin() {
        return this.login;
    }

    //Setter

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    //Other

    //Logout a user
    public String logout() {
        currentUser.reset();
        return "index.xhtml?faces-redirect=true";
    }

    //Validate the Username
    public void postValidateUser(ComponentSystemEvent ev) {
        UIInput temp = (UIInput) ev.getComponent();
        this.tempUsername = (String) temp.getValue();
    }

    //Validate Username and Password
    public void validateLogin(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String password = (String) value;
        app.validateUsernameAndPassword(currentUser, tempUsername, password, salt);
        if(!currentUser.isValid()) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login falsch.", "Benutzername oder Passwort ist ungültig.");
            throw new ValidatorException(message);
        }
    }

    //Login user
    public String login() {
        if(currentUser.getUser().getIsMod()) {
            return "modpanel.xhtml?faces-redirect=true";
        }else if(!currentUser.getUser().getIsMod()) {
            return "app.xhtml?faces-redirect=true";
        }else {
            return "";
        }
    }
}