package restdocs.tool.export.insomnia.export;

import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Sets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static restdocs.tool.export.insomnia.export.InsomniaConstants.REQUEST_GROUP_TYPE;
import static restdocs.tool.export.insomnia.export.InsomniaConstants.REQUEST_TYPE;

public class InsomniaExporterTest {

  @Test
  void findExistingFolderResource() {
    Resource resource1 = mock(Resource.class);
    when(resource1.getType()).thenReturn(REQUEST_TYPE);

    Resource resource2 = mock(Resource.class);
    when(resource2.getType()).thenReturn(REQUEST_GROUP_TYPE);

    Resource folderResource = new InsomniaExporter().findExistingFolderResource(Sets.newSet(resource1, resource2));

    assertNotNull(folderResource);
    assertEquals(resource2, folderResource);
  }

  @Test
  void findExistingFolderResourceWhenNoneMatch() {
    Resource resource1 = mock(Resource.class);
    when(resource1.getType()).thenReturn(REQUEST_TYPE);

    Resource resource2 = mock(Resource.class);
    when(resource2.getType()).thenReturn(REQUEST_TYPE);

    Resource folderResource = new InsomniaExporter().findExistingFolderResource(Sets.newSet(resource1, resource2));

    assertNull(folderResource);
  }

  @Test
  void findExistingFolderResourceWhenNullResources() {
    assertNull(new InsomniaExporter().findExistingFolderResource(null));
  }
}
