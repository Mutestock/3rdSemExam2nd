package facades;

import entities.Booking;
import entities.Role;
import entities.User;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;
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
@Disabled
public class UserFacadeTest {

    //Strangely enough the userfacade extends the abstract facade, but the multifacade has functionality for user as well.
    //Would require some refactoring
    //Only the remove Override
    private final MultiFacade<User> MULTI_USER_FACADE;
    private final UserFacade USER_FACADE;
    private final MultiFacade<Booking> MULTI_BOOKING_FACADE;
    private final MultiFacade<Role> MULTI_ROLE_FACADE;
    private static EntityManagerFactory emf;

    User user01 = new User("Mark Kek", "1234BadPassword");
    User user02 = new User("Jasper_the_great_1337", "FoxyBoxes");
    Booking booking01 = new Booking(new Date(2020, 11, 4));
    Booking booking02 = new Booking(new Date(2020, 9, 19));
    Role role01 = new Role("admin");

    public UserFacadeTest() {
        MULTI_USER_FACADE = new MultiFacade(User.class, emf);
        MULTI_BOOKING_FACADE = new MultiFacade(Booking.class, emf);
        USER_FACADE = new UserFacade();
        MULTI_ROLE_FACADE = new MultiFacade(Role.class, emf);
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
            em.createNamedQuery("User.removaAll").executeUpdate();
            em.createNamedQuery("Booking.removeAll").executeUpdate();
            em.createNamedQuery("Roles.removaAll");
            em.getTransaction().commit();

            Stream.of(
                    user01,
                    user02,
                    booking01,
                    booking02,
                    role01
            ).forEach(o -> {
                em.getTransaction().begin();
                em.persist(o);
                em.getTransaction().commit();
            });
        } finally {
            em.close();
        }
    }

    @Test
    public void find() {
        Assertions.assertEquals("Mark Kek", ((User) USER_FACADE.find(user01.getUserName())).getUserName());
    }

    //Using opportunity to check relations
    @Test
    public void findBookRelations() {
        user01.setBookingList(
                Stream.of(
                        booking01,
                        booking02
                ).collect(Collectors.toList())
        );
        Booking booking = (Booking) MULTI_BOOKING_FACADE
                .find(((User) USER_FACADE
                        .find(user01.getUserName()))
                        .getBookingList()
                        .get(0)
                        .getId());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Assertions.assertEquals("2020/11/4", dateFormat.format(booking.getBookingDate()));
    }

    @Test
    public void findAll() {
        Assertions.assertEquals(2, USER_FACADE.findAll().size());
    }

    @Test
    public void create() {
        int size = USER_FACADE.findAll().size();
        USER_FACADE.create(new User("John Jerkson", "Sssssh"));
        Assertions.assertEquals(size + 1, USER_FACADE.findAll().size());
        Assertions.assertFalse(((User) USER_FACADE.find(user01.getUserName())).getUserPass().equals("Sssssh"));
    }

    //Strange but ok
    @Test
    public void update() {
        user01.setUserName("newUserName");
        USER_FACADE.edit(user01);
        Assertions.assertEquals("newUserName", ((User) USER_FACADE.find(user01.getUserName())).getUserName());
    }

    @Test
    public void updateRelations() {
        user01.setRoleList(
                Stream.of(
                        role01
                ).collect(Collectors.toList())
        );
        Assertions.assertEquals("admin", ((User) USER_FACADE.find(user01.getUserName())).getRoleList().get(0).getRoleName());
    }

    @Test
    public void remove() {
        int size = USER_FACADE.findAll().size();
        MULTI_USER_FACADE.remove(user01.getUserName());
        Assertions.assertEquals(size - 1, USER_FACADE.findAll().size());
    }

    @Test
    public void removeRelations() {
        int size = USER_FACADE.findAll().size();
        user01.addRole(role01);
        user01.setBookingList(
                Stream.of(
                        booking01
                ).collect(Collectors.toList())
        );
        USER_FACADE.edit(user01);
        Assertions.assertEquals("Mark Kek", ((Role) MULTI_ROLE_FACADE.find(role01.getRoleName())).getUserList().get(0).getUserName());
        Assertions.assertEquals("Mark Kek", ((Booking) MULTI_BOOKING_FACADE.find(booking01.getId())).getUser().getUserName());
        MULTI_USER_FACADE.remove(user01.getUserName());
        Assertions.assertEquals(0, ((Role) MULTI_ROLE_FACADE.find(role01.getRoleName())).getUserList().size());
        Assertions.assertEquals(null, ((Booking) MULTI_BOOKING_FACADE.find(booking01.getId())).getUser());
        Assertions.assertEquals(size - 1, USER_FACADE.findAll().size());
    }
}
