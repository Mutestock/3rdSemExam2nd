package facades;

import entities.Booking;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

/**
 *
 * @author Henning
 */
public class BookingFacadeTest {

    private final MultiFacade<Booking> MULTI_BOOKING_FACADE;
    private static EntityManagerFactory emf;

    Booking booking01 = new Booking(new Date(2012,2,2));
    Booking booking02 = new Booking(new Date(1992,02,21));

    public BookingFacadeTest() {
        MULTI_BOOKING_FACADE = new MultiFacade(Booking.class, emf);
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST, EMF_Creator.Strategy.DROP_AND_CREATE);
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Booking.removeAll").executeUpdate();
            em.getTransaction().commit();

            Stream.of(
                    booking01,
                    booking02
            ).forEach(o -> {
                em.getTransaction().begin();
                em.persist(o);
                em.getTransaction().commit();
            });
        } finally {
            em.close();
        }
    }

    @Disabled
    @Test
    public void find() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Assertions.assertEquals("2012/10/11", dateFormat.format(((Booking) MULTI_BOOKING_FACADE.find(booking01.getId())).getBookingDate()));
    }

    @Test
    public void findAll() {
        Assertions.assertEquals(2, MULTI_BOOKING_FACADE.findAll().size());
    }

    @Test
    public void create() {
        int size = MULTI_BOOKING_FACADE.findAll().size();
        MULTI_BOOKING_FACADE.create(new Booking(new Date(1, 2, 3)));
        Assertions.assertEquals(size + 1, MULTI_BOOKING_FACADE.findAll().size());
    }

    @Disabled
    @Test
    public void update() {
        booking01.setBookingDate(new Date(2013, 2, 1));
        MULTI_BOOKING_FACADE.edit(booking01);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Assertions.assertEquals("2/2/2", dateFormat.format(((Booking) MULTI_BOOKING_FACADE.find(booking01.getId())).getBookingDate()));
    }

    @Test
    public void removeBasic() {
        int size = MULTI_BOOKING_FACADE.findAll().size();
        MULTI_BOOKING_FACADE.remove(booking01.getId());
        Assertions.assertEquals(size - 1, MULTI_BOOKING_FACADE.findAll().size());
    }

}
