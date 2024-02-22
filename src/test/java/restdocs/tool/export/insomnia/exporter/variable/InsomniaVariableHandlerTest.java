package restdocs.tool.export.insomnia.exporter.variable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import restdocs.tool.export.ToolExportSnippet;
import restdocs.tool.export.common.variable.VariableKeys;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

public class InsomniaVariableHandlerTest {

  private InsomniaVariableHandler insomniaVariableHandler;

  @BeforeEach
  void setUp() {
    insomniaVariableHandler = new InsomniaVariableHandler();
  }

  @Test
  void createHostVariable() {
    try (MockedStatic<ToolExportSnippet> mockToolExportSnippet = mockStatic(ToolExportSnippet.class)) {
      String appName = "some application name";

      String hostVariable = insomniaVariableHandler.createHostVariable(appName);

      assertNotNull(hostVariable);
      assertEquals("{{_.some_application_name" + VariableKeys.HOST + "}}", hostVariable);

      mockToolExportSnippet.verify(() -> ToolExportSnippet.addVariable("some_application_name" + VariableKeys.HOST ));
    }
  }

  @Test
  void encapsulateVariable() {
    String variable = "some_variable_name";

    String variableFormatted = insomniaVariableHandler.encapsulateVariable(variable);

    assertNotNull(variableFormatted);
    assertEquals("{{_." + variable + "}}", variableFormatted);
  }

  @Test
  void encapsulateVariableWhenNull() {
    assertNull(insomniaVariableHandler.encapsulateVariable(null));
  }

  @Test
  void replaceVariables() {
    assertNull(insomniaVariableHandler.replaceVariables(null));
  }

  @Test
  void replaceVariablesNoMatches() {
    try (MockedStatic<ToolExportSnippet> mockToolExportSnippet = mockStatic(ToolExportSnippet.class)) {
      assertEquals("test", insomniaVariableHandler.replaceVariables("test"));
      mockToolExportSnippet.verify(() -> ToolExportSnippet.addVariable(anyString()), never()
      );
    }
  }

  @Test
  void replaceVariablesIsFormatted() {
    try (MockedStatic<ToolExportSnippet> mockToolExportSnippet = mockStatic(ToolExportSnippet.class)) {
      assertEquals("here is a {{_.test_api_key}}", insomniaVariableHandler.replaceVariables("here is a <<Test-API-Key>>"));
      mockToolExportSnippet.verify(() -> ToolExportSnippet.addVariable("test_api_key"));
      mockToolExportSnippet.verify(() -> ToolExportSnippet.addVariable("Test-API-Key"), never());
    }
  }

  @Test
  void replaceVariablesMultipleMatches() {
    try (MockedStatic<ToolExportSnippet> mockToolExportSnippet = mockStatic(ToolExportSnippet.class)) {
      assertEquals("{{_.test}} {{_.here}} and {{_.here}}", insomniaVariableHandler.replaceVariables("<<test>> <<here>> and <<Here>>"));
      mockToolExportSnippet.verify(() -> ToolExportSnippet.addVariable("test"));
      mockToolExportSnippet.verify(() -> ToolExportSnippet.addVariable("here"), times(2));
    }
  }
}
