package restdocs.tool.export.insomnia.export.creators;

import org.junit.jupiter.api.Test;
import restdocs.tool.export.insomnia.export.Resource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static restdocs.tool.export.insomnia.export.InsomniaAssertionUtils.assertIdMatches;
import static restdocs.tool.export.insomnia.export.InsomniaAssertionUtils.assertName;
import static restdocs.tool.export.insomnia.export.InsomniaAssertionUtils.assertTimeEpoch;
import static restdocs.tool.export.insomnia.export.InsomniaConstants.FOLDER_ID;
import static restdocs.tool.export.insomnia.export.InsomniaConstants.REQUEST_GROUP_TYPE;

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
