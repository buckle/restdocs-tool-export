package restdocs.tool.export.postman.exporter.creators;

import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Sets;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.restdocs.operation.OperationRequest;
import restdocs.tool.export.postman.exporter.Body;
import restdocs.tool.export.postman.exporter.Header;
import restdocs.tool.export.postman.exporter.Request;
import restdocs.tool.export.postman.exporter.Url;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RequestCreatorTest {

  @Test
  void create() {
    HttpHeaders httpHeaders = mock(HttpHeaders.class);
    OperationRequest operationRequest = mock(OperationRequest.class);
    when(operationRequest.getHeaders()).thenReturn(httpHeaders);
    when(operationRequest.getMethod()).thenReturn(HttpMethod.POST);

    Set<Header> headers = Sets.newSet(new Header());
    HeadersCreator headersCreator = mock(HeadersCreator.class);
    when(headersCreator.create(httpHeaders)).thenReturn(headers);

    Url url = mock(Url.class);
    UrlCreator urlCreator = mock(UrlCreator.class);
    when(urlCreator.create(operationRequest)).thenReturn(url);

    Body body = mock(Body.class);
    BodyCreator bodyCreator = mock(BodyCreator.class);
    when(bodyCreator.create(operationRequest)).thenReturn(body);

    Request request = new RequestCreator(headersCreator, urlCreator, bodyCreator).create(operationRequest);

    assertNotNull(request);
    assertEquals(HttpMethod.POST.toString(), request.getMethod());
    assertEquals(headers, request.getHeaders());
    assertEquals(url, request.getUrl());
    assertEquals(body, request.getBody());
  }

  @Test
  void createWhenOperationRequestNull() {
    assertNull(new RequestCreator().create(null));
  }

  @Test
  void createWhenOperationRequestMethodNull() {
    OperationRequest operationRequest = mock(OperationRequest.class);

    assertNull(new RequestCreator().create(operationRequest));
  }
}
