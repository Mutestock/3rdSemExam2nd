package facades;

import entities.Image;
import entities.User;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

/**
 *
 * @author Henning
 */
public class ImageFacadeTest {

    private final MultiFacade<Image> MULTI_IMAGE_FACADE;
    private static EntityManagerFactory emf;

    Image image01 = new Image("https://chickenonaraft.com/");
    Image image02 = new Image("http://www.notpron.org/notpron/");

    public ImageFacadeTest() {
        MULTI_IMAGE_FACADE = new MultiFacade(Image.class, emf);
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
            em.createNamedQuery("Image.removeAll").executeUpdate();
            em.getTransaction().commit();

            Stream.of(
                    image01,
                    image02
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
        Assertions.assertEquals("http://www.notpron.org/notpron/", ((Image) MULTI_IMAGE_FACADE.find(image02.getId())).getImageURL());
    }

    @Test
    public void findAll() {
        Assertions.assertEquals(2, MULTI_IMAGE_FACADE.findAll().size());
    }

    @Test
    public void create() {
        int size = MULTI_IMAGE_FACADE.findAll().size();
        MULTI_IMAGE_FACADE.create(new Image("https://www.metasploit.com/"));
        Assertions.assertEquals(size + 1, MULTI_IMAGE_FACADE.findAll().size());
    }

    @Test
    public void update() {
        image01.setImageURL("https://portswigger.net/burp");
        MULTI_IMAGE_FACADE.edit(image01);
        Assertions.assertEquals("https://portswigger.net/burp", ((Image) MULTI_IMAGE_FACADE.find(image01.getId())).getImageURL());
    }

    @Test
    public void removeBasic() {
        int size = MULTI_IMAGE_FACADE.findAll().size();
        MULTI_IMAGE_FACADE.remove(image01.getId());
        Assertions.assertEquals(size - 1, MULTI_IMAGE_FACADE.findAll().size());
    }

}
