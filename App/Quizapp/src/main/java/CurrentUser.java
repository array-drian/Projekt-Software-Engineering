import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@Named
@SessionScoped
public class CurrentUser implements Serializable {

    private int userID = 0;
    private boolean isMod, isNormalUser = false;

    public void reset() {
        this.userID = 0;
        this.isMod = false;
        this.isNormalUser = false;
    }

    //Getter

    public int getUserID() {
        return this.userID;
    }

    public boolean getIsMod() {
        return this.isMod;
    }

    public boolean getIsNormalUser() {
        return this.isNormalUser;
    }

    //Setter

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setIsMod(boolean isMod) {
        this.isMod = isMod;
    }

    public void setIsNormalUser(boolean isNormalUser) {
        this.isNormalUser = isNormalUser;
    }

    //Other

    public boolean isValid() {
        return this.userID != 0 && (this.isMod || this.isNormalUser);
    }

}