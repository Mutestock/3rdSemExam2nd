package facades;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Henning
 */
public class BasicExtensionFacade<T> extends AbstractFacade {

    private EntityManagerFactory emf;
    private BasicExtensionFacade instance;

    public BasicExtensionFacade(Class entityClass, EntityManagerFactory emf) {
        super(entityClass);
        this.emf = emf;
    }
    
    public BasicExtensionFacade getFacade(){
        if(instance == null){
            instance = new BasicExtensionFacade(ENTITY_CLASS, emf);
        }
        return instance;
    }

    @Override
    protected EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

}
