package restdocs.tool.export.insomnia.export;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static restdocs.tool.export.insomnia.export.InsomniaAssertionUtils.assertIdMatches;
import static restdocs.tool.export.insomnia.export.InsomniaConstants.FOLDER_ID;

@ExtendWith(MockitoExtension.class)
public class InsomniaExportUtilsTest {

  @Test
  void generateId() {
    String folderID = InsomniaExportUtils.generateId(FOLDER_ID);

    assertNotNull(folderID);
    assertIdMatches(FOLDER_ID, folderID);
  }

  @Test
  void formatBaseId() {
    String formattedBaseId = InsomniaExportUtils.formatBaseId(InsomniaExportUtils.getBaseId());

    assertNotNull(formattedBaseId);
    assertIdMatches("", formattedBaseId);
  }

  @Test
  void formatBaseIdWhenNull() {
    assertNull(InsomniaExportUtils.formatBaseId(null));
  }

  @Test
  void getBaseId() {
    String baseId = InsomniaExportUtils.getBaseId();

    assertNotNull(baseId);
    assertTrue(baseId.matches("[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}"));
  }


}
