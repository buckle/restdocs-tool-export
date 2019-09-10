package restdocs.tool.export.common.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static restdocs.tool.export.common.assertion.AssertionUtils.assertName;

public class ExportUtilsTest {

  @Test
  void formatName() {
    String name = "blah-blah123*&&^%$#%&*(Hah";

    String formattedName = ExportUtils.formatName(name);

    assertName(formattedName, "blah");
    assertEquals("Blah Blah123 Hah", formattedName);
  }

  @Test
  void formatNameWhenNull() {
    assertNull(ExportUtils.formatName(null));
  }

}
