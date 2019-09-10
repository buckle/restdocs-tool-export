package restdocs.tool.export.postman.exporter.creators;

import org.junit.jupiter.api.Test;
import restdocs.tool.export.postman.exporter.Collection;
import restdocs.tool.export.postman.exporter.Info;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CollectionCreatorTest {

  @Test
  void create() {
    String appName = "appName" + UUID.randomUUID().toString();

    Info info = mock(Info.class);
    InfoCreator infoCreator = mock(InfoCreator.class);
    when(infoCreator.create(appName)).thenReturn(info);

    Collection collection = new CollectionCreator(infoCreator).create(appName);

    assertNotNull(collection);
    assertEquals(info, collection.getInfo());
    assertNull(collection.getItems());
  }

  @Test
  void createWhenAppNameNull() {
    assertNull(new CollectionCreator().create(null));
  }
}
