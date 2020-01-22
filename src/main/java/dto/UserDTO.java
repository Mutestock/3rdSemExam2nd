package dto;

import entities.User;
import java.util.List;

/**
 *
 * @author Henning
 */
public class UserDTO {

    private String userName;
    private String password;
    private List menuPlanList;

    public UserDTO(User user) {
        this.userName = user.getUserName();
        this.password = user.getUserPass();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List getMenuPlanList() {
        return menuPlanList;
    }

    public void setMenuPlanList(List menuPlanList) {
        this.menuPlanList = menuPlanList;
    }

}
