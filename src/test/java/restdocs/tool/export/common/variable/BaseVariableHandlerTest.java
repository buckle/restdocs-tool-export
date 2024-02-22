package restdocs.tool.export.common.variable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import restdocs.tool.export.ToolExportSnippet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
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
  void createHostVariable() {
    try (MockedStatic<ToolExportSnippet> mockToolExportSnippet = mockStatic(ToolExportSnippet.class)) {
      String hostVariable = variableHandler.createHostVariable(TEST_NAME);

      assertNotNull(hostVariable);
      assertEquals(BASE_FORMATTED_NAME + VariableKeys.HOST, hostVariable);
      mockToolExportSnippet.verify(() -> ToolExportSnippet.addVariable(BASE_FORMATTED_NAME + VariableKeys.HOST));
    }
  }

  @Test
  void createHostVariableWhenNull() {
    assertNull(variableHandler.createHostVariable(null));
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

  @Test
  void replaceVariables() {
    assertNull(variableHandler.replaceVariables(null));
  }

  @Test
  void replaceVariablesNoMatches() {
    assertEquals("test", variableHandler.replaceVariables("test"));
  }

  @Test
  void replaceVariablesIsFormatted() {
    assertEquals("here is a test_api_key", variableHandler.replaceVariables("here is a <<Test-API-Key>>"));
  }

  @Test
  void replaceVariablesMultipleMatches() {
    assertEquals("test here and here", variableHandler.replaceVariables("<<test>> here and <<here>>"));
  }
}
