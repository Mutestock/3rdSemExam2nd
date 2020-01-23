package dto;

import entities.Booking;
import entities.Image;
import entities.Kayak;
import entities.Reservation;
import java.util.List;

/**
 *
 * @author Henning
 */
public class KayakDTO {

    //Actively choosing to include id
    private long id;
    private Booking booking;
    private String color;
    private String description;
    private List<Image> imageList;
    private String model;
    private String name;
    private int personCount;
    private List<Reservation> reservationList;
    private String year;

    public KayakDTO(Kayak kayak) {
        this.id = kayak.getId();
        this.booking = kayak.getBooking();
        this.color = kayak.getColor();
        this.description = kayak.getDescription();
        this.imageList = kayak.getImageList();
        this.model = kayak.getModel();
        this.name = kayak.getName();
        this.personCount = kayak.getPersonsAllowed();
        this.reservationList = kayak.getReservationList();
        this.year = kayak.getYear();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPersonCount() {
        return personCount;
    }

    public void setPersonCount(int personCount) {
        this.personCount = personCount;
    }

    public List<Reservation> getReservationList() {
        return reservationList;
    }

    public void setReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    
    
}
