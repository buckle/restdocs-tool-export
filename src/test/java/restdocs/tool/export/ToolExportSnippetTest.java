package restdocs.tool.export;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.RestDocumentationContext;
import org.springframework.restdocs.operation.Operation;
import restdocs.tool.export.common.handler.ToolHandler;
import restdocs.tool.export.common.handler.ToolHandlers;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    ToolExportSnippet instance = ToolExportSnippet.initInstance(applicationName, ToolHandlers.INSOMNIA);

    assertNotNull(instance);
    assertFalse(instance.getToolHandlers().isEmpty());
  }

  @Test
  void initInstanceWhenAlreadyCreated() throws Exception {
    ToolExportSnippet instance = ToolExportSnippet.initInstance(applicationName, ToolHandlers.INSOMNIA);
    ToolExportSnippet instance2 = ToolExportSnippet.initInstance(applicationName, ToolHandlers.INSOMNIA);

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

    verify(toolHandler, times(1)).initialize(outputDirectory, applicationName);
    verify(toolHandler, times(1)).handleOperation(operation);
  }

  @Test
  void documentWhenAlreadyInitialized() throws Exception {
    ToolExportSnippet toolExportSnippet = spy(ToolExportSnippet.initInstance(applicationName, ToolHandlers.INSOMNIA));
    ToolHandler toolHandler = mock(ToolHandler.class);
    doReturn(Arrays.asList(toolHandler)).when(toolExportSnippet).getToolHandlers();

    toolExportSnippet.document(operation);
    toolExportSnippet.document(operation);

    verify(toolHandler, times(1)).initialize(outputDirectory, applicationName);
    verify(toolHandler, times(2)).handleOperation(operation);
  }
}
