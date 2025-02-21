package other;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import user.CurrentUser;
import user.User;
import user.UserDAO;

@Named
@ApplicationScoped
public class App implements Serializable {

    @Inject
    private UserDAO userDAO;

    //Return a hashed Password
    public String hashPassword(String name, String pass, String salt) {
        try {
            MessageDigest digester = MessageDigest.getInstance("SHA-512");
            byte[] hashBytes = digester.digest((name + pass + salt)
                .getBytes(StandardCharsets.UTF_8));
            return new String(Base64.getEncoder().encode(hashBytes));
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Validate Username and Password
    public void validateUsernameAndPassword(CurrentUser currentUser, String userName, String userPass, String salt) {
        String passHash = hashPassword(userName, userPass, salt);
        currentUser.reset();
        User user = userDAO.getUserByNameAndPassword(userName, passHash);
        if(user != null && user.getIsActive()) {
            currentUser.setUser(user);
        }
    }
}