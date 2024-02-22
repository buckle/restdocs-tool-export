package restdocs.tool.export.insomnia.exporter.creators;

import org.junit.jupiter.api.Test;
import restdocs.tool.export.insomnia.exporter.Resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static restdocs.tool.export.insomnia.exporter.InsomniaConstants.ENVIRONMENT_TYPE;
import static restdocs.tool.export.insomnia.exporter.InsomniaConstants.ENV_ID;
import static restdocs.tool.export.insomnia.utils.InsomniaAssertionUtils.assertIdMatches;
import static restdocs.tool.export.insomnia.utils.InsomniaAssertionUtils.assertTimeEpoch;

public class EnvironmentResourceCreatorTest {

  @Test
  void create() {
    String appName = "App_Name";
    Resource environmentResource = new EnvironmentResourceCreator().create(appName);

    assertNotNull(environmentResource);
    assertIdMatches(ENV_ID, environmentResource.getId());
    assertEquals(ENVIRONMENT_TYPE, environmentResource.getType());
    assertEquals("App Name Environment", environmentResource.getName());
    assertTimeEpoch(environmentResource.getCreated());
    assertTimeEpoch(environmentResource.getModified());
  }

  @Test
  void createWithNullAppName() {
    Resource environmentResource = new EnvironmentResourceCreator().create(null);

    assertNotNull(environmentResource);
    assertIdMatches(ENV_ID, environmentResource.getId());
    assertEquals(ENVIRONMENT_TYPE, environmentResource.getType());
    assertEquals("null Environment", environmentResource.getName());
    assertTimeEpoch(environmentResource.getCreated());
    assertTimeEpoch(environmentResource.getModified());
  }
}
