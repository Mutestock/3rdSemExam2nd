package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Henning
 */
@Entity
@Table(name = "Kayak", catalog = "", schema = "")
@NamedQueries({
    @NamedQuery(name = "Kayak.findAll", query = "SELECT k FROM Kayak k"),
    @NamedQuery(name = "Kayak.findById", query = "SELECT k FROM Kayak k WHERE k.id = :id"),
    @NamedQuery(name = "Kayak.findByName", query = "SELECT k FROM Kayak k WHERE k.name = :name"),
    @NamedQuery(name = "Kayak.findByModel", query = "SELECT k FROM Kayak k WHERE k.model = :model"),
    @NamedQuery(name = "Kayak.findByDescription", query = "SELECT k FROM Kayak k WHERE k.description = :description"),
    @NamedQuery(name = "Kayak.findByYear", query = "SELECT k FROM Kayak k WHERE k.year = :year"),
    @NamedQuery(name = "Kayak.findByColor", query = "SELECT k FROM Kayak k WHERE k.color = :color"),
    @NamedQuery(name = "Kayak.removeAll", query = "DELETE FROM Kayak k"),
    @NamedQuery(name = "Kayak.findByPersonsAllowed", query = "SELECT k FROM Kayak k WHERE k.personsAllowed = :personsAllowed")})
public class Kayak implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Size(max = 255)
    @Column(name = "name")
    private String name;
    @Size(max = 255)
    @Column(name = "model")
    private String model;
    @Size(max = 255)
    @Column(name = "description")
    private String description;
    @Size(max = 255)
    @Column(name = "year")
    private String year;
    @Size(max = 255)
    @Column(name = "color")
    private String color;
    @Column(name = "personsAllowed")
    private Integer personsAllowed;
    @JoinTable(name = "Reservation_has_Kayak", joinColumns = {
        @JoinColumn(name = "Kayak_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "Reservation_id", referencedColumnName = "id")})
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Reservation> reservationList = new ArrayList();
    @JoinColumn(name = "Booking_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Booking booking;
    @JoinTable(name = "Kayak_Image", joinColumns = {
        @JoinColumn(name = "Kayak_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "Image_id", referencedColumnName = "id")})
    @ManyToMany
    private List<Image> imageList = new ArrayList();

    public Kayak() {
    }

    public Kayak(String name, String model, String description, String year, String color, Integer personsAllowed) {
        this.name = name;
        this.model = model;
        this.description = description;
        this.year = year;
        this.color = color;
        this.personsAllowed = personsAllowed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getPersonsAllowed() {
        return personsAllowed;
    }

    public void setPersonsAllowed(Integer personsAllowed) {
        this.personsAllowed = personsAllowed;
    }

    public List<Reservation> getReservationList() {
        return reservationList;
    }

    public void setReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }

}
