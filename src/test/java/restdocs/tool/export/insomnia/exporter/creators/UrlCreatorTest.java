package restdocs.tool.export.insomnia.exporter.creators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.operation.OperationRequest;
import restdocs.tool.export.common.ExportProperties;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UrlCreatorTest {

  private UrlCreator urlCreator;
  private String applicationName;

  @BeforeEach
  void setUp() {
    urlCreator = new UrlCreator();
    applicationName = "AppName" + UUID.randomUUID().toString();
  }

  @Test
  public void create() throws Exception {
    OperationRequest operationRequest = mock(OperationRequest.class);
    URI uri = new URI("https://localhost/a/url/patch");
    when(operationRequest.getUri()).thenReturn(uri);

    Map<String, Object> attributes = new HashMap<>();
    attributes.put(ExportProperties.APPLICATION_NAME, applicationName);

    Operation operation = mock(Operation.class);
    when(operation.getRequest()).thenReturn(operationRequest);
    when(operation.getAttributes()).thenReturn(attributes);

    String url = urlCreator.create(operation);

    assertNotNull(url);
    assertEquals("{{}}", url);
  }
}
