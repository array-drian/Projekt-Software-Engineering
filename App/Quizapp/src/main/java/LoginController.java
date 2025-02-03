import java.io.Serializable;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.application.NavigationHandler;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIInput;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.faces.validator.ValidatorException;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class LoginController implements Serializable {

    @Inject
    private App app;

    @Inject
    private CurrentUser currentUser;

    private static final String salt = "H7sk6V725NVxqDq05DVnraZV";

    String userName, userPass, tempUsername;
    String failureMessage = "";

    //Getter

    public String getUserName() {
        return this.userName;
    }

    public String getUserPass() {
        return this.userPass;
    }

    public String getFailureMessage() {
        return this.failureMessage;
    }

    public String getSalt() {
        return this.salt;
    }

    //Setter

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }

    //Other

    public void checkLogin() {
        if(!currentUser.isValid()) {
            failureMessage = "Bitte loggen Sie sich ein.";
            FacesContext fc = FacesContext.getCurrentInstance();
            NavigationHandler nh = fc.getApplication().getNavigationHandler();
            nh.handleNavigation(fc, null, "index.xhtml?faces-redirect=true");
        }

    }

    public String logout() {
        currentUser.reset();
        return "index.xhtml?faces-redirect=true";
    }

    public void postValidateUser(ComponentSystemEvent ev) {
        UIInput temp = (UIInput) ev.getComponent();
        this.tempUsername = (String) temp.getValue();
    }

    public void validateLogin(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String password = (String) value;
        app.validateUsernameAndPassword(currentUser, tempUsername, password, salt);
        if (!currentUser.isValid())
            throw new ValidatorException(new FacesMessage("Login falsch!"));
    }

    public String login() {
        if (currentUser.getIsMod()) {
            this.failureMessage = "";
            return "modpanel.xhtml?faces-redirect=true";
        } else if (currentUser.getIsNormalUser()) {
            this.failureMessage = "";
            return "app.xhtml?faces-redirect=true";
        } else {
            this.failureMessage = "Passwort und Benutzername nicht erkannt.";
            return "";
        }
    }
}