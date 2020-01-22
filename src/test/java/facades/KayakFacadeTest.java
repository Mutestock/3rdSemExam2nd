package facades;

import entities.Kayak;
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
public class KayakFacadeTest {

    private final MultiFacade<Kayak> MULTI_KAYAK_FACADE;
    private static EntityManagerFactory emf;

    Kayak kayak01 = new Kayak("name01", "model01", "description01", "year01", "turquoise", 2);
    Kayak kayak02 = new Kayak("name02", "model02", "description02", "year02", "teal", 129);

    public KayakFacadeTest() {
        MULTI_KAYAK_FACADE = new MultiFacade(Kayak.class, emf);
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
            em.createNamedQuery("Kayak.removeAll").executeUpdate();
            em.getTransaction().commit();

            Stream.of(
                    kayak01,
                    kayak02
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
        Assertions.assertEquals("model01", ((Kayak) MULTI_KAYAK_FACADE.find(kayak01.getId())).getModel());
    }

    @Test
    public void findAll() {
        Assertions.assertEquals(2, MULTI_KAYAK_FACADE.findAll().size());
    }

    @Test
    public void create() {
        int size = MULTI_KAYAK_FACADE.findAll().size();
        MULTI_KAYAK_FACADE.create(new Kayak("name03", "model03", "description03", "year03", "scarlet", 0));
        Assertions.assertEquals(size + 1, MULTI_KAYAK_FACADE.findAll().size());
    }

    @Test
    public void update() {
        kayak01.setDescription("updated description");
        MULTI_KAYAK_FACADE.edit(kayak01);
        Assertions.assertEquals("updated description", ((Kayak) MULTI_KAYAK_FACADE.find(kayak01.getId())).getDescription());
    }

    @Test
    public void removeBasic() {
        int size = MULTI_KAYAK_FACADE.findAll().size();
        MULTI_KAYAK_FACADE.remove(kayak01.getId());
        Assertions.assertEquals(size - 1, MULTI_KAYAK_FACADE.findAll().size());
    }
}
