package restdocs.tool.export;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.restdocs.RestDocumentationContext;
import org.springframework.restdocs.operation.Operation;
import restdocs.tool.export.common.ExportConstants;
import restdocs.tool.export.common.handler.ToolHandler;
import restdocs.tool.export.common.handler.ToolHandlers;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static restdocs.tool.export.common.ExportConstants.APPLICATION_NAME;
import static restdocs.tool.export.common.ExportConstants.HOST_VARIABLE_ENABLED;

public class ToolExportSnippetTest {

  private String applicationName = "AppName" + UUID.randomUUID();
  private File outputDirectory = mock(File.class);
  private Operation operation;

  @BeforeEach
  void setUp() {
    ToolExportSnippet.resetInstance();

    RestDocumentationContext restDocumentationContext = mock(RestDocumentationContext.class);
    when(restDocumentationContext.getOutputDirectory()).thenReturn(outputDirectory);

    Map<String, Object> operationAttributes = new HashMap<>();
    operationAttributes.put(RestDocumentationContext.class.getName(), restDocumentationContext);

    operation = mock(Operation.class);
    when(operation.getAttributes()).thenReturn(operationAttributes);
  }

  @Test
  void initInstance() throws Exception {
    assertNull(ToolExportSnippet.getProperty(HOST_VARIABLE_ENABLED, Boolean.class));
    ToolExportSnippet instance = ToolExportSnippet.initInstance(applicationName, ToolHandlers.INSOMNIA);
    assertTrue(ToolExportSnippet.getProperty(HOST_VARIABLE_ENABLED, Boolean.class));

    assertNotNull(instance);
    assertFalse(instance.getToolHandlers().isEmpty());
  }

  @Test
  void initInstanceWhenAlreadyCreated() throws Exception {
    ToolExportSnippet instance = ToolExportSnippet.initInstance(applicationName, ToolHandlers.INSOMNIA);
    assertTrue(ToolExportSnippet.getProperty(HOST_VARIABLE_ENABLED, Boolean.class));

    ToolExportSnippet.setProperty(HOST_VARIABLE_ENABLED, false);
    assertFalse(ToolExportSnippet.getProperty(HOST_VARIABLE_ENABLED, Boolean.class));

    ToolExportSnippet instance2 = ToolExportSnippet.initInstance(applicationName, ToolHandlers.INSOMNIA);
    // ensure setDefaultProperties not called again
    assertFalse(ToolExportSnippet.getProperty(HOST_VARIABLE_ENABLED, Boolean.class));

    assertNotNull(instance);
    assertEquals(instance, instance2);
  }

  @Test
  void getInstanceWhenNotInitialized() {
     assertNull(ToolExportSnippet.getInstance());
  }

  @Test
  void getInstanceWhenInitialized() throws Exception {
    ToolExportSnippet instance = ToolExportSnippet.initInstance(applicationName, ToolHandlers.INSOMNIA);
    assertEquals(instance, ToolExportSnippet.getInstance());
  }

  @Test
  void document() throws Exception {
    ToolExportSnippet toolExportSnippet = spy(ToolExportSnippet.initInstance(applicationName, ToolHandlers.INSOMNIA));
    ToolHandler toolHandler = mock(ToolHandler.class);
    doReturn(Arrays.asList(toolHandler)).when(toolExportSnippet).getToolHandlers();

    toolExportSnippet.document(operation);

    InOrder inOrder = Mockito.inOrder(operation, toolHandler, toolExportSnippet);
    inOrder.verify(toolHandler, times(1)).initialize(outputDirectory, applicationName);
    inOrder.verify(toolExportSnippet, times(1)).setAttributes(operation);
    inOrder.verify(toolHandler, times(1)).handleOperation(operation);
  }

  @Test
  void documentWhenAlreadyInitialized() throws Exception {
    ToolExportSnippet toolExportSnippet = spy(ToolExportSnippet.initInstance(applicationName, ToolHandlers.INSOMNIA));
    ToolHandler toolHandler = mock(ToolHandler.class);
    doReturn(Arrays.asList(toolHandler)).when(toolExportSnippet).getToolHandlers();

    toolExportSnippet.document(operation);
    toolExportSnippet.document(operation);

    verify(toolHandler, times(1)).initialize(outputDirectory, applicationName);
    verify(toolExportSnippet, times(2)).setAttributes(operation);
    verify(toolHandler, times(2)).handleOperation(operation);
  }

  @Test
  void setAndGetPropertyWhenString() throws Exception {
    String propertyName = "some.name";
    String value = "some.value";

    ToolExportSnippet toolExportSnippet = spy(ToolExportSnippet.initInstance(applicationName, ToolHandlers.INSOMNIA));

    ToolExportSnippet.setProperty(propertyName, value);

    assertEquals(value, ToolExportSnippet.getProperty(propertyName, String.class));
  }

  @Test
  void setAndGetPropertyWhenInteger() throws Exception {
    String propertyName = "some.name";
    Integer value = 77;

    ToolExportSnippet toolExportSnippet = spy(ToolExportSnippet.initInstance(applicationName, ToolHandlers.INSOMNIA));

    ToolExportSnippet.setProperty(propertyName, value);

    assertEquals(value, ToolExportSnippet.getProperty(propertyName, Integer.class).intValue());
  }

  @Test
  void setDefaultProperties() throws Exception {
    ToolExportSnippet toolExportSnippet = spy(ToolExportSnippet.initInstance(applicationName, ToolHandlers.INSOMNIA));
    toolExportSnippet.setDefaultProperties();

    assertTrue(ToolExportSnippet.getProperty(ExportConstants.HOST_VARIABLE_ENABLED, Boolean.class));
  }

  @Test
  void setAttributes() throws Exception {
    ToolExportSnippet toolExportSnippet = spy(ToolExportSnippet.initInstance(applicationName, ToolHandlers.INSOMNIA));
    ToolExportSnippet.setProperty(APPLICATION_NAME, applicationName);

    toolExportSnippet.setAttributes(operation);

    assertEquals(applicationName, operation.getAttributes().get(APPLICATION_NAME));
  }
}
