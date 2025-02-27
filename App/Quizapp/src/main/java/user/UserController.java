package user;

import java.io.Serializable;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.application.NavigationHandler;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.PersistenceException;
import other.App;

@Named
@ViewScoped
public class UserController implements Serializable {

    private String tempPassword;

    private String newPassword;

    private String confirmPassword;

    @Inject
    private UserDAO userDAO;

    @Inject
    private App app;

    @Inject
    private LoginController loginController;

    //Getter

    public String getTempPassword() {
        return this.tempPassword;
    }

    public String getNewPassword() {
        return this.newPassword;
    }

    public String getConfirmPassword() {
        return this.confirmPassword;
    }

    //Setter

    public void setTempPassword(String tempPassword) {
        this.tempPassword = tempPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    //Other

    //Updates a user
    public void saveUser(User user) {
        String hashedInputPassword = app.hashPassword(this.tempPassword, loginController.getSalt());

        if(!user.getUserPass().equals(hashedInputPassword)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Dein altes Passwort ist falsch.", null);
            FacesContext.getCurrentInstance().addMessage("editUserForm", msg);
            return;
        }

        if(!newPassword.equals(confirmPassword)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Die neuen Passwörter stimmen nicht überein.", null);
            FacesContext.getCurrentInstance().addMessage("editUserForm", msg);
            return;
        }

        if(this.newPassword != null && !this.newPassword.isEmpty()) {
            user.setUserPass(app.hashPassword(this.newPassword, loginController.getSalt()));
        }

        User changedUser = userDAO.merge(user);

        try {
            userDAO.persist(changedUser);

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Nutzerdaten erfolgreich geändert.", null);
            FacesContext.getCurrentInstance().addMessage("editUserForm", msg);
        }catch (PersistenceException  e) {
            if (e.getCause() != null && e.getCause().getMessage().contains("Duplicate entry")) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nutzername wird bereits verwendet.", null);
                FacesContext.getCurrentInstance().addMessage("editUserForm", msg);
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ein Fehler ist beim Speichern aufgetreten.", null);
                FacesContext.getCurrentInstance().addMessage("editUserForm", msg);
            }
        }
    }

    //"Deletes" a user by setting it inactive
    public void deleteUser(User user) {
        String hashedInputPassword = app.hashPassword(this.tempPassword, loginController.getSalt());

        if(!user.getUserPass().equals(hashedInputPassword)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Dein altes Passwort ist falsch.", null);
            FacesContext.getCurrentInstance().addMessage("editUserForm", msg);
            return;
        }

        User deletedUser = userDAO.merge(user);

        deletedUser.setIsActive(false);

        userDAO.persist(deletedUser);

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Nutzer erfolgreich gelöscht.", null);
        FacesContext.getCurrentInstance().addMessage("editUserForm", msg);
        
        FacesContext fc = FacesContext.getCurrentInstance();
        NavigationHandler nh = fc.getApplication().getNavigationHandler();
        nh.handleNavigation(fc, null, "index.xhtml?faces-redirect=true");
    }
}