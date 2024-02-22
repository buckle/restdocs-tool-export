package restdocs.tool.export.postman.variable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import restdocs.tool.export.ToolExportSnippet;
import restdocs.tool.export.common.variable.VariableKeys;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

public class PostmanVariableHandlerTest {
  private PostmanVariableHandler postmanVariableHandler;

  @BeforeEach
  void setUp() {
    postmanVariableHandler = new PostmanVariableHandler();
  }

  @Test
  void createHostVariable() {
    try (MockedStatic<ToolExportSnippet> mockToolExportSnippet = mockStatic(ToolExportSnippet.class)) {
      String appName = "some application name";

      String hostVariable = postmanVariableHandler.createHostVariable(appName);

      assertNotNull(hostVariable);
      assertEquals("{{some_application_name" + VariableKeys.HOST + "}}", hostVariable);

      mockToolExportSnippet.verify(() -> ToolExportSnippet.addVariable("some_application_name" + VariableKeys.HOST));
    }
  }

  @Test
  void encapsulateVariable() {
    String variable = "some_variable_name";

    String variableFormatted = postmanVariableHandler.encapsulateVariable(variable);

    assertNotNull(variableFormatted);
    assertEquals("{{" + variable + "}}", variableFormatted);
  }

  @Test
  void encapsulateVariableWhenNull() {
    assertNull(postmanVariableHandler.encapsulateVariable(null));
  }

  @Test
  void replaceVariables() {
    assertNull(postmanVariableHandler.replaceVariables(null));
  }

  @Test
  void replaceVariablesNoMatches() {
    try (MockedStatic<ToolExportSnippet> mockToolExportSnippet = mockStatic(ToolExportSnippet.class)) {
      assertEquals("test", postmanVariableHandler.replaceVariables("test"));
      mockToolExportSnippet.verify(() -> ToolExportSnippet.addVariable(anyString()), never()
      );
    }
  }

  @Test
  void replaceVariablesIsFormatted() {
    try (MockedStatic<ToolExportSnippet> mockToolExportSnippet = mockStatic(ToolExportSnippet.class)) {
      assertEquals("here is a {{test_api_key}}", postmanVariableHandler.replaceVariables("here is a <<Test-API-Key>>"));
      mockToolExportSnippet.verify(() -> ToolExportSnippet.addVariable("test_api_key"));
      mockToolExportSnippet.verify(() -> ToolExportSnippet.addVariable("Test-API-Key"), never());
    }
  }

  @Test
  void replaceVariablesMultipleMatches() {
    try (MockedStatic<ToolExportSnippet> mockToolExportSnippet = mockStatic(ToolExportSnippet.class)) {
      assertEquals("{{test}} {{here}} and {{here}}", postmanVariableHandler.replaceVariables("<<test>> <<here>> and <<Here>>"));
      mockToolExportSnippet.verify(() -> ToolExportSnippet.addVariable("test"));
      mockToolExportSnippet.verify(() -> ToolExportSnippet.addVariable("here"), times(2));
    }
  }
}
