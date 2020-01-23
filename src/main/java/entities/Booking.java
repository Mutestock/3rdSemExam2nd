package entities;

import static entities.Booking_.bookingDate;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Henning
 */
@Entity
@Table(name = "Booking", catalog = "", schema = "")
@NamedQueries({
    @NamedQuery(name = "Booking.findAll", query = "SELECT b FROM Booking b"),
    @NamedQuery(name = "Booking.findById", query = "SELECT b FROM Booking b WHERE b.id = :id"),
    @NamedQuery(name = "Booking.removeAll", query = "DELETE FROM Booking b"),
    @NamedQuery(name = "Booking.findByBookingData", query = "SELECT b FROM Booking b WHERE b.bookingDate = :bookingDate")})
public class Booking implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "bookingDate")
    @Temporal(TemporalType.DATE)
    private Date bookingDate = java.util.Calendar.getInstance().getTime();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "booking", fetch = FetchType.LAZY)
    private List<Kayak> kayakList = new ArrayList();
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    public Booking() {
    }

    public Booking(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
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
