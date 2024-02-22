package restdocs.tool.export.common.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static restdocs.tool.export.common.assertion.AssertionUtils.assertNameReadably;

public class ExportUtilsTest {

  private String TEST_NAME = " blah-blah123*&&^%$#%&*(Hah ";

  @Test
  void formatNameReadably() {
    String formattedName = ExportUtils.formatNameReadably(TEST_NAME);

    assertNameReadably(formattedName, "blah");
    assertEquals("Blah Blah123 Hah", formattedName);
  }

  @Test
  void formatNameReadablyWhenNull() {
    assertNull(ExportUtils.formatNameReadably(null));
  }

}
