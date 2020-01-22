package dto;

import entities.Booking;
import entities.Kayak;
import entities.User;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Henning
 */
public class BookingDTO {

    private Date date;
    private List<Kayak> kayakList;
    private User user;

    public BookingDTO(Booking booking) {
        this.date = booking.getBookingDate();
        this.kayakList = booking.getKayakList();
        this.user = booking.getUser();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Kayak> getKayakList() {
        return kayakList;
    }

    public void setKayakList(List<Kayak> kayakList) {
        this.kayakList = kayakList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
