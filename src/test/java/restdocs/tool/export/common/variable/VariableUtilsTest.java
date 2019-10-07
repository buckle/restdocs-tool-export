package restdocs.tool.export.common.variable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static restdocs.tool.export.common.assertion.AssertionUtils.assertNameVariable;

public class VariableUtilsTest {

  private String TEST_NAME = " blah-blah123*&&^%$#%&*(Hah ";
  private String BASE_FORMATTED_NAME = "blah_blah123_hah";

  @Test
  void getHostVariable() {
    String hostVariable = VariableUtils.getHostVariable(TEST_NAME);

    assertNotNull(hostVariable);
    assertEquals(BASE_FORMATTED_NAME + VariableKeys.HOST, hostVariable);
  }

  @Test
  void getHostVariableWhenNull() {
    assertNull(VariableUtils.getHostVariable(null));
  }

  @Test
  void formatNameVariable() {
    String formattedName = VariableUtils.formatNameVariable(TEST_NAME);

    assertNameVariable(formattedName, "blah");
    assertEquals(BASE_FORMATTED_NAME, formattedName);
  }

  @Test
  void formatNameVariableWhenNull() {
    assertNull(VariableUtils.formatNameVariable(null));
  }

}
