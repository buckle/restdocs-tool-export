package restdocs.tool.export;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.restdocs.RestDocumentationContext;
import org.springframework.restdocs.operation.Operation;
import restdocs.tool.export.common.ExportProperties;
import restdocs.tool.export.common.handler.ToolHandler;
import restdocs.tool.export.common.handler.ToolHandlers;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static restdocs.tool.export.common.ExportProperties.APPLICATION_NAME;

public class ToolExportSnippetTest {

  private String applicationName = "AppName" + UUID.randomUUID().toString();
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
    ToolExportSnippet instance = ToolExportSnippet.initInstance(ToolHandlers.INSOMNIA);

    assertNotNull(instance);
    assertFalse(instance.getToolHandlers().isEmpty());
  }

  @Test
  void initInstanceWhenAlreadyCreated() throws Exception {
    ToolExportSnippet instance = ToolExportSnippet.initInstance(ToolHandlers.INSOMNIA);
    ToolExportSnippet instance2 = ToolExportSnippet.initInstance(ToolHandlers.INSOMNIA);

    assertNotNull(instance);
    assertEquals(instance, instance2);
  }

  @Test
  void getInstanceWhenNotInitialized() {
     assertNull(ToolExportSnippet.getInstance());
  }

  @Test
  void getInstanceWhenInitialized() throws Exception {
    ToolExportSnippet instance = ToolExportSnippet.initInstance(ToolHandlers.INSOMNIA);
    assertEquals(instance, ToolExportSnippet.getInstance());
  }

  @Test
  void document() throws Exception {
    ToolExportSnippet toolExportSnippet = spy(ToolExportSnippet.initInstance(ToolHandlers.INSOMNIA));
    ToolExportSnippet.setProperty(APPLICATION_NAME, applicationName);
    ToolHandler toolHandler = mock(ToolHandler.class);
    doReturn(Arrays.asList(toolHandler)).when(toolExportSnippet).getToolHandlers();

    toolExportSnippet.document(operation);

    InOrder inOrder = Mockito.inOrder(operation, toolHandler, toolExportSnippet);
    inOrder.verify(toolExportSnippet, times(1)).setDefaultProperties();
    inOrder.verify(toolHandler, times(1)).initialize(outputDirectory, applicationName);
    inOrder.verify(toolExportSnippet, times(1)).setAttributes(operation);
    inOrder.verify(toolHandler, times(1)).handleOperation(operation);
  }

  @Test
  void documentWhenAlreadyInitialized() throws Exception {
    ToolExportSnippet toolExportSnippet = spy(ToolExportSnippet.initInstance(ToolHandlers.INSOMNIA));
    ToolExportSnippet.setProperty(APPLICATION_NAME, applicationName);
    ToolHandler toolHandler = mock(ToolHandler.class);
    doReturn(Arrays.asList(toolHandler)).when(toolExportSnippet).getToolHandlers();

    toolExportSnippet.document(operation);
    toolExportSnippet.document(operation);

    verify(toolExportSnippet, times(1)).setDefaultProperties();
    verify(toolHandler, times(1)).initialize(outputDirectory, applicationName);
    verify(toolExportSnippet, times(2)).setAttributes(operation);
    verify(toolHandler, times(2)).handleOperation(operation);
  }

  @Test
  void setAndGetPropertyWhenString() throws Exception {
    String propertyName = "some.name";
    String value = "some.value";

    ToolExportSnippet toolExportSnippet = spy(ToolExportSnippet.initInstance(ToolHandlers.INSOMNIA));

    ToolExportSnippet.setProperty(propertyName, value);

    assertEquals(value, toolExportSnippet.getProperty(propertyName, String.class));
  }

  @Test
  void setAndGetPropertyWhenInteger() throws Exception {
    String propertyName = "some.name";
    Integer value = 77;

    ToolExportSnippet toolExportSnippet = spy(ToolExportSnippet.initInstance(ToolHandlers.INSOMNIA));

    ToolExportSnippet.setProperty(propertyName, value);

    assertEquals(value, toolExportSnippet.getProperty(propertyName, Integer.class).intValue());
  }

  @Test
  void setDefaultProperties() throws Exception {
    ToolExportSnippet toolExportSnippet = spy(ToolExportSnippet.initInstance(ToolHandlers.INSOMNIA));
    toolExportSnippet.setDefaultProperties();

    assertTrue(toolExportSnippet.getProperty(ExportProperties.HOST_VARIABLE_ENABLED, Boolean.class));
  }

  @Test
  void setAttributes() throws Exception {
    ToolExportSnippet toolExportSnippet = spy(ToolExportSnippet.initInstance(ToolHandlers.INSOMNIA));
    ToolExportSnippet.setProperty(APPLICATION_NAME, applicationName);

    toolExportSnippet.setAttributes(operation);

    assertEquals(applicationName, operation.getAttributes().get(APPLICATION_NAME));
  }
}
