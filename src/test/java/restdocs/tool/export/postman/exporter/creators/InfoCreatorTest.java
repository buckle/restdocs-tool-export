package restdocs.tool.export.postman.exporter.creators;

import org.junit.jupiter.api.Test;
import restdocs.tool.export.postman.exporter.Info;
import restdocs.tool.export.postman.exporter.PostmanConstants;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static restdocs.tool.export.common.assertion.AssertionUtils.assertNameReadably;

public class InfoCreatorTest {

  @Test
  void create() {
    String baseName = "bobbert";
    String appName = baseName + UUID.randomUUID();

    Info info = new InfoCreator().create(appName);

    assertNotNull(info);
    assertNotNull(UUID.fromString(info.getPostmanId()));
    assertNameReadably(info.getName(), baseName);
    assertEquals(PostmanConstants.SCHEMA_V2_1_0, info.getSchema());
  }

  @Test
  void createWhenAppNameNull() {
    assertNull(new InfoCreator().create(null));
  }
}
