package other;

import java.io.Serializable;

import game.CurrentGame;
import jakarta.faces.application.NavigationHandler;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import user.CurrentUser;

@Named
@ViewScoped
public class CheckerController implements Serializable {
    
    @Inject
    private CurrentUser currentUser;

    @Inject
    private CurrentGame currentGame;

    //Check if a user is logged in
    public void checkLogin() {
        if(!currentUser.isValid()) {
            FacesContext fc = FacesContext.getCurrentInstance();
            NavigationHandler nh = fc.getApplication().getNavigationHandler();
            nh.handleNavigation(fc, null, "index.xhtml?faces-redirect=true");
        }
    }

    public void checkLoginAndNoMod() {
        if(!currentUser.isValid()) {
            FacesContext fc = FacesContext.getCurrentInstance();
            NavigationHandler nh = fc.getApplication().getNavigationHandler();
            nh.handleNavigation(fc, null, "index.xhtml?faces-redirect=true");
        }else if(currentUser.getUser().getIsMod()) {
            FacesContext fc = FacesContext.getCurrentInstance();
            NavigationHandler nh = fc.getApplication().getNavigationHandler();
            nh.handleNavigation(fc, null, "modpanel.xhtml?faces-redirect=true");
        }
    }

    //Check if a user is logged in and is a moderator
    public void checkLoginAndPermission() {
        if(!currentUser.isValid()) {
            FacesContext fc = FacesContext.getCurrentInstance();
            NavigationHandler nh = fc.getApplication().getNavigationHandler();
            nh.handleNavigation(fc, null, "index.xhtml?faces-redirect=true");
        }else if(!currentUser.getUser().getIsMod()) {
            FacesContext fc = FacesContext.getCurrentInstance();
            NavigationHandler nh = fc.getApplication().getNavigationHandler();
            nh.handleNavigation(fc, null, "app.xhtml?faces-redirect=true");
        }
    }

    //Check if a user is logged in and a game is set
    public void checkGameAndUser() {
        if(!currentUser.isValid()) {
            FacesContext fc = FacesContext.getCurrentInstance();
            NavigationHandler nh = fc.getApplication().getNavigationHandler();
            nh.handleNavigation(fc, null, "index.xhtml?faces-redirect=true");
        }else if(!currentGame.isValid()) {
            FacesContext fc = FacesContext.getCurrentInstance();
            NavigationHandler nh = fc.getApplication().getNavigationHandler();
            nh.handleNavigation(fc, null, "quiz.xhtml?faces-redirect=true");
        }
    }
}