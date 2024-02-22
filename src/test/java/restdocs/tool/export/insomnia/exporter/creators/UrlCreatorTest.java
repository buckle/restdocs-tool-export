package restdocs.tool.export.insomnia.exporter.creators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.operation.OperationRequest;
import restdocs.tool.export.ToolExportSnippet;
import restdocs.tool.export.common.ExportConstants;
import restdocs.tool.export.insomnia.exporter.variable.InsomniaVariableHandler;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

public class UrlCreatorTest {

  private UrlCreator urlCreator;
  private String applicationName;
  private InsomniaVariableHandler insomniaVariableHandler;

  @BeforeEach
  void setUp() {
    insomniaVariableHandler = mock(InsomniaVariableHandler.class);
    urlCreator = new UrlCreator(insomniaVariableHandler);
    applicationName = "AppName";
  }

  @Test
  public void create() throws Exception {
    try (MockedStatic<ToolExportSnippet> mockToolExportSnippet = mockStatic(ToolExportSnippet.class)) {
      OperationRequest operationRequest = mock(OperationRequest.class);
      URI uri = new URI("https://localhost/a/url/path");
      when(operationRequest.getUri()).thenReturn(uri);

      Map<String, Object> attributes = new HashMap<>();
      attributes.put(ExportConstants.APPLICATION_NAME, applicationName);
      attributes.put(ExportConstants.HOST_VARIABLE_ENABLED, true);

      Operation operation = mock(Operation.class);
      when(operation.getRequest()).thenReturn(operationRequest);
      when(operation.getAttributes()).thenReturn(attributes);

      String hostVariable = "some_host";
      when(insomniaVariableHandler.getHostVariable(applicationName)).thenReturn(hostVariable);
      when(insomniaVariableHandler.replaceVariables(anyString())).thenReturn("/a/url/path");
      String url = urlCreator.create(operation);

      assertNotNull(url);
      assertEquals(hostVariable + "/a/url/path", url);
      mockToolExportSnippet.verify(() -> ToolExportSnippet.addVariable(hostVariable));
    }
  }

  @Test
  void createWhenOperationNull() {
    assertNull(urlCreator.create(null));
  }

  @Test
  void createWhenOperationRequestNull() {
    Operation operation = mock(Operation.class);
    when(operation.getRequest()).thenReturn(null);

    assertNull(urlCreator.create(operation));
  }

  @Test
  void createWhenOperationAttributesNull() {
    OperationRequest operationRequest = mock(OperationRequest.class);

    Operation operation = mock(Operation.class);
    when(operation.getRequest()).thenReturn(operationRequest);
    when(operation.getAttributes()).thenReturn(null);

    assertNull(urlCreator.create(operation));
  }

  @Test
  void createWhenOperationRequestURINull() {
    OperationRequest operationRequest = mock(OperationRequest.class);
    when(operationRequest.getUri()).thenReturn(null);

    Map<String, Object> attributes = new HashMap<>();
    attributes.put(ExportConstants.APPLICATION_NAME, applicationName);

    Operation operation = mock(Operation.class);
    when(operation.getRequest()).thenReturn(operationRequest);
    when(operation.getAttributes()).thenReturn(attributes);

    assertNull(urlCreator.create(operation));
  }

  @Test
  public void createWhenHostVariableEnabledFalse() throws Exception {
    InsomniaVariableHandler insomniaVariableHandler1 = new InsomniaVariableHandler();
    UrlCreator urlCreator1 = new UrlCreator(insomniaVariableHandler1);

    OperationRequest operationRequest = mock(OperationRequest.class);
    URI uri = new URI("https", "localhost", "/a/url/<<variable-path>>", "q=test", null);
    when(operationRequest.getUri()).thenReturn(uri);

    Map<String, Object> attributes = new HashMap<>();
    attributes.put(ExportConstants.APPLICATION_NAME, applicationName);
    attributes.put(ExportConstants.HOST_VARIABLE_ENABLED, false);

    Operation operation = mock(Operation.class);
    when(operation.getRequest()).thenReturn(operationRequest);
    when(operation.getAttributes()).thenReturn(attributes);

    String url = urlCreator1.create(operation);

    assertNotNull(url);
    assertEquals("https://localhost/a/url/{{_.variable_path}}", url);
  }

  @Test
  public void createWhenNoHostOrPathVariables() throws Exception {
    try (MockedStatic<ToolExportSnippet> mockToolExportSnippet = mockStatic(ToolExportSnippet.class)) {
      InsomniaVariableHandler insomniaVariableHandler1 = new InsomniaVariableHandler();
      UrlCreator urlCreator1 = new UrlCreator(insomniaVariableHandler1);

      OperationRequest operationRequest = mock(OperationRequest.class);
      URI uri = new URI("https", "localhost", "/a/url/path", "q=test", null);
      when(operationRequest.getUri()).thenReturn(uri);

      Map<String, Object> attributes = new HashMap<>();
      attributes.put(ExportConstants.APPLICATION_NAME, applicationName);
      attributes.put(ExportConstants.HOST_VARIABLE_ENABLED, false);

      Operation operation = mock(Operation.class);
      when(operation.getRequest()).thenReturn(operationRequest);
      when(operation.getAttributes()).thenReturn(attributes);

      String url = urlCreator1.create(operation);

      assertNotNull(url);
      assertEquals("https://localhost/a/url/path", url);
      mockToolExportSnippet.verify(() -> ToolExportSnippet.addVariable(anyString()), never());
    }
  }
}
