package restdocs.tool.export.insomnia.exporter.creators;

import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Sets;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.OperationRequest;
import restdocs.tool.export.insomnia.exporter.Body;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BodyCreatorTest {

  @Test
  void create() {
    String contentBody = UUID.randomUUID().toString();

    OperationRequest operationRequest = mock(OperationRequest.class);
    when(operationRequest.getContentAsString()).thenReturn(contentBody);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    when(operationRequest.getHeaders()).thenReturn(headers);

    Body body = new BodyCreator().create(operationRequest);

    assertNotNull(body);
    assertEquals(contentBody, body.getText());
    assertEquals(MediaType.APPLICATION_JSON.toString(), body.getMimeType());
  }

  @Test
  void createWhenNullRequest() {
    Body body = new BodyCreator().create(null);

    assertNull(body);
  }

  @Test
  void createWhenHeadersNull() {
    String contentBody = UUID.randomUUID().toString();

    OperationRequest operationRequest = mock(OperationRequest.class);
    when(operationRequest.getContentAsString()).thenReturn(contentBody);

    when(operationRequest.getHeaders()).thenReturn(null);

    Body body = new BodyCreator().create(operationRequest);

    assertNotNull(body);
    assertEquals(contentBody, body.getText());
    assertNull(body.getMimeType());
  }

  @Test
  void createWhenContentTypeNull() {
    String contentBody = UUID.randomUUID().toString();

    OperationRequest operationRequest = mock(OperationRequest.class);
    when(operationRequest.getContentAsString()).thenReturn(contentBody);

    HttpHeaders headers = new HttpHeaders();
    headers.setAllow(Sets.newSet(HttpMethod.GET));
    when(operationRequest.getHeaders()).thenReturn(headers);

    Body body = new BodyCreator().create(operationRequest);

    assertNotNull(body);
    assertEquals(contentBody, body.getText());
    assertNull(body.getMimeType());
  }
}
