package restdocs.tool.export.insomnia.exporter.variable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import restdocs.tool.export.common.variable.VariableKeys;

import static org.junit.jupiter.api.Assertions.*;

public class InsomniaVariableHandlerTest {

  private InsomniaVariableHandler insomniaVariableHandler;

  @BeforeEach
  void setUp() {
    insomniaVariableHandler = new InsomniaVariableHandler();
  }

  @Test
  void getHostVariable() {
    String appName = "some application name";

    String hostVariable = insomniaVariableHandler.getHostVariable(appName);

    assertNotNull(hostVariable);
    assertEquals("{{some_application_name" + VariableKeys.HOST + "}}", hostVariable);
  }

  @Test
  void encapsulateVariable() {
    String variable = "some_variable_name";

    String variableFormatted = insomniaVariableHandler.encapsulateVariable(variable);

    assertNotNull(variableFormatted);
    assertEquals("{{" + variable + "}}", variableFormatted);
  }

  @Test
  void encapsulateVariableWhenNull() {
    assertNull(insomniaVariableHandler.encapsulateVariable(null));
  }
}
