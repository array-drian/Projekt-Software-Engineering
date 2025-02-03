import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

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

    // Getters and Setters for form fields
    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return this.userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public boolean getIsMod() {
        return this.isMod;
    }

    public void setIsMod(boolean isMod) {
        this.isMod = isMod;
    }

    // Method to save user to the database
    public void saveToDatabase() {
        String hashedPass = App.hashPassword(userName, userPass, loginController.getSalt());
        User newUser = new User(userName, hashedPass, isMod);
        userDAO.persist(newUser); // This will now use the manually managed EntityManager
    }
}