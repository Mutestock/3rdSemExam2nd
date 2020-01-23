package dto;

import entities.Booking;
import entities.Role;
import entities.User;
import java.util.List;

/**
 *
 * @author Henning
 */
public class UserDTO {

    private String userName;
    private List<Booking> bookingList;
    private List<Role> roleList;

    public UserDTO(User user) {
        this.userName = user.getUserName();
        this.bookingList = user.getBookingList();
        this.roleList = user.getRoleList();
    }

    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Booking> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

}
