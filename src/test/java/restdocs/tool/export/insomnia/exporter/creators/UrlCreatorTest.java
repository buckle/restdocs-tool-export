package restdocs.tool.export.insomnia.exporter.creators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.operation.OperationRequest;
import restdocs.tool.export.common.ExportProperties;
import restdocs.tool.export.insomnia.exporter.variable.InsomniaVariableHandler;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
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
    OperationRequest operationRequest = mock(OperationRequest.class);
    URI uri = new URI("https://localhost/a/url/path");
    when(operationRequest.getUri()).thenReturn(uri);

    Map<String, Object> attributes = new HashMap<>();
    attributes.put(ExportProperties.APPLICATION_NAME, applicationName);

    Operation operation = mock(Operation.class);
    when(operation.getRequest()).thenReturn(operationRequest);
    when(operation.getAttributes()).thenReturn(attributes);

    String hostVariable = "some_host";
    when(insomniaVariableHandler.getHostVariable(applicationName)).thenReturn(hostVariable);

    String url = urlCreator.create(operation);

    assertNotNull(url);
    assertEquals(hostVariable + "/a/url/path", url);
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
    attributes.put(ExportProperties.APPLICATION_NAME, applicationName);

    Operation operation = mock(Operation.class);
    when(operation.getRequest()).thenReturn(operationRequest);
    when(operation.getAttributes()).thenReturn(attributes);

    assertNull(urlCreator.create(operation));
  }
}
