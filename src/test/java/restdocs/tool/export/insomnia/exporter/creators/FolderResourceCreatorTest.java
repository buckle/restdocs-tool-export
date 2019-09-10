package restdocs.tool.export.insomnia.exporter.creators;

import org.junit.jupiter.api.Test;
import restdocs.tool.export.insomnia.exporter.Resource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static restdocs.tool.export.common.assertion.AssertionUtils.assertName;
import static restdocs.tool.export.insomnia.exporter.InsomniaConstants.FOLDER_ID;
import static restdocs.tool.export.insomnia.exporter.InsomniaConstants.REQUEST_GROUP_TYPE;
import static restdocs.tool.export.insomnia.utils.InsomniaAssertionUtils.assertIdMatches;
import static restdocs.tool.export.insomnia.utils.InsomniaAssertionUtils.assertTimeEpoch;

public class FolderResourceCreatorTest {

  @Test
  void create() {
    String baseName = "appName";
    String appName = baseName + UUID.randomUUID().toString();

    Resource folderResource = new FolderResourceCreator().create(appName);

    assertNotNull(folderResource);
    assertIdMatches(FOLDER_ID, folderResource.getId());
    assertEquals(REQUEST_GROUP_TYPE, folderResource.getType());
    assertName(folderResource.getName(), baseName);
    assertTimeEpoch(folderResource.getCreated());
    assertTimeEpoch(folderResource.getModified());
  }

  @Test
  void createWhenNameNull() {
    Resource folderResource = new FolderResourceCreator().create(null);

    assertNull(folderResource);
  }
}
