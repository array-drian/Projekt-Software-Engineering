package user;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import other.App;

@Named
@RequestScoped
public class UserBean {

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
    public void saveToDatabase() {
        String hashedPass = app.hashPassword(userName, userPass, loginController.getSalt());
        User newUser = new User(userName, hashedPass, isMod);
        userDAO.persist(newUser);
    }
}