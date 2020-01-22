package utils;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Henning
 */
public class ExternalAPIManagementTest {

    private final ExternalAPIManagement EM = new ExternalAPIManagement();

    public ExternalAPIManagementTest() {

    }

    @Test
    public void testReadObjectPlaceholder() {
        JSONPlaceholderComment comment = EM.readObject(ExternalAPIManagement.ExternalURL.JSON_PLACEHOLDER.URLConcat("comments/4"), JSONPlaceholderComment.class);
        Assertions.assertNotNull(comment);
        Assertions.assertEquals(4, comment.getId());
    }

    @Test
    public void testReadObjectsPlaceholder() {
        List<JSONPlaceholderComment> commentList = EM.readObjects(ExternalAPIManagement.ExternalURL.JSON_PLACEHOLDER.URLConcat("comments"), JSONPlaceholderComment.class);
        Assertions.assertEquals(commentList.get(4).getClass().getSimpleName(), JSONPlaceholderComment.class.getSimpleName());
        assertNotNull(commentList.get(0).getEmail());
    }

    @Test
    public void testCollectJsonStringPlaceholder() {
        Assertions.assertNotNull(EM.collectJsonString(ExternalAPIManagement.ExternalURL.JSON_PLACEHOLDER.URLConcat("comments/4")));
    }

}
