package dto;

import entities.Booking;
import entities.User;
import java.util.List;

/**
 *
 * @author Henning
 */
public class UserDTO {

    private String userName;
    private String password;
    private List<Booking> bookingList;

    public UserDTO(User user) {
        this.userName = user.getUserName();
        this.password = user.getUserPass();
        this.bookingList = user.getBookingList();
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

    public List<Booking> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

}
