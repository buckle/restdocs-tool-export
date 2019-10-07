package restdocs.tool.export.common.variable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static restdocs.tool.export.common.assertion.AssertionUtils.assertNameVariable;

public class BaseVariableHandlerTest {

  private BaseVariableHandler variableHandler;
  private String TEST_NAME = " blah-blah123*&&^%$#%&*(Hah ";
  private String BASE_FORMATTED_NAME = "blah_blah123_hah";

  @BeforeEach
  void setUp() {
    variableHandler = new BaseVariableHandler();
  }

  @Test
  void getHostVariable() {
    String hostVariable = variableHandler.getHostVariable(TEST_NAME);

    assertNotNull(hostVariable);
    assertEquals(BASE_FORMATTED_NAME + VariableKeys.HOST, hostVariable);
  }

  @Test
  void getHostVariableWhenNull() {
    assertNull(variableHandler.getHostVariable(null));
  }

  @Test
  void formatNameVariable() {
    String formattedName = variableHandler.formatNameVariable(TEST_NAME);

    assertNameVariable(formattedName, "blah");
    assertEquals(BASE_FORMATTED_NAME, formattedName);
  }

  @Test
  void formatNameVariableWhenNull() {
    assertNull(variableHandler.formatNameVariable(null));
  }

}
