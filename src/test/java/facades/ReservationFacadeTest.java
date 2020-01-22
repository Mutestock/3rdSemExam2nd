package facades;

import entities.Reservation;
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
public class ReservationFacadeTest {

    private final MultiFacade<Reservation> MULTI_RESERVATION_FACADE;
    private static EntityManagerFactory emf;

    Reservation reservation01 = new Reservation(new Date(2019, 10, 11), new Date(2020, 2, 3));
    Reservation reservation02 = new Reservation(new Date(2014, 4, 12), new Date(2015, 4, 2));

    public ReservationFacadeTest() {
        MULTI_RESERVATION_FACADE = new MultiFacade(Reservation.class, emf);
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
            em.createNamedQuery("Reservation.removeAll").executeUpdate();
            em.getTransaction().commit();

            Stream.of(
                    reservation01,
                    reservation02
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
        Assertions.assertEquals("2019/10/11", dateFormat.format(((Reservation) MULTI_RESERVATION_FACADE.find(reservation01.getId())).getDateFrom()));
    }

    @Test
    public void findAll() {
        Assertions.assertEquals(2, MULTI_RESERVATION_FACADE.findAll().size());
    }

    @Test
    public void create() {
        int size = MULTI_RESERVATION_FACADE.findAll().size();
        Reservation reservation03 = new Reservation(new Date(2191, 2, 2), new Date(2012, 2, 9));
        MULTI_RESERVATION_FACADE.create(reservation03);
        Assertions.assertEquals(size + 1, MULTI_RESERVATION_FACADE.findAll().size());
    }

    @Disabled
    @Test
    public void update() {
        reservation01.setDateFrom(new Date(123, 1, 1));
        MULTI_RESERVATION_FACADE.edit(reservation01);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Assertions.assertEquals("123/1/1", dateFormat.format(((Reservation) MULTI_RESERVATION_FACADE.find(reservation01.getId())).getDateFrom()));
    }

    @Test
    public void remove() {
        int size = MULTI_RESERVATION_FACADE.findAll().size();
        MULTI_RESERVATION_FACADE.remove(reservation01.getId());
        Assertions.assertEquals(size - 1, MULTI_RESERVATION_FACADE.findAll().size());
    }

}
