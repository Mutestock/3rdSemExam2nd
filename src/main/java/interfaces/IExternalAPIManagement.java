package interfaces;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 *
 * @author Henning
 */
public interface IExternalAPIManagement {

    public <Any> Any readObject(URL url, Class objectClass);

    public <Any> Any readObjects(URL url, Class objectClass);

    public String collectJsonString(URL url);
}
