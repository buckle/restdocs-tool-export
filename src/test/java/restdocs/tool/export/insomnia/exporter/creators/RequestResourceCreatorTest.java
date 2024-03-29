package restdocs.tool.export.insomnia.exporter.creators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Sets;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.operation.OperationRequest;
import restdocs.tool.export.insomnia.exporter.Body;
import restdocs.tool.export.insomnia.exporter.Pair;
import restdocs.tool.export.insomnia.exporter.PairBuilder;
import restdocs.tool.export.insomnia.exporter.Resource;

import java.net.URI;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static restdocs.tool.export.common.assertion.AssertionUtils.assertNameReadably;
import static restdocs.tool.export.insomnia.exporter.InsomniaConstants.REQUEST_ID;
import static restdocs.tool.export.insomnia.exporter.InsomniaConstants.REQUEST_TYPE;
import static restdocs.tool.export.insomnia.utils.InsomniaAssertionUtils.assertIdMatches;
import static restdocs.tool.export.insomnia.utils.InsomniaAssertionUtils.assertTimeEpoch;

public class RequestResourceCreatorTest {

  private UrlCreator urlCreator;
  private HeadersCreator headersCreator;
  private InsomniaQueryParametersCreator insomniaQueryParametersCreator;
  private BodyCreator bodyCreator;
  private Operation operation;
  private OperationRequest request;

  @BeforeEach
  void setUp() {
    urlCreator = mock(UrlCreator.class);
    headersCreator = mock(HeadersCreator.class);
    insomniaQueryParametersCreator = mock(InsomniaQueryParametersCreator.class);
    bodyCreator = mock(BodyCreator.class);
    operation = mock(Operation.class);
    request = mock(OperationRequest.class);
    when(operation.getRequest()).thenReturn(request);
  }

  @Test
  void create() throws Exception {
    String baseName = "bobbert";
    String name = baseName + UUID.randomUUID();
    when(operation.getName()).thenReturn(name);

    URI uri = new URI("/a/url");
    when(request.getUri()).thenReturn(uri);
    String url = "url" + UUID.randomUUID();
    when(urlCreator.create(operation)).thenReturn(url);

    when(request.getMethod()).thenReturn(HttpMethod.GET);

    HttpHeaders httpHeaders = mock(HttpHeaders.class);
    when(request.getHeaders()).thenReturn(httpHeaders);
    Set<Pair> headers = Sets.newSet(PairBuilder.builder().build());
    when(headersCreator.create(httpHeaders)).thenReturn(headers);

    Set<Pair> parameterPairs = Sets.newSet(PairBuilder.builder().build());
    when(insomniaQueryParametersCreator.create(any())).thenReturn(parameterPairs);

    Body body = mock(Body.class);
    when(bodyCreator.create(request)).thenReturn(body);

    Resource resource = new RequestResourceCreator(urlCreator, headersCreator, insomniaQueryParametersCreator, bodyCreator).create(operation);

    assertNotNull(resource);
    assertIdMatches(REQUEST_ID, resource.getId());
    assertEquals(REQUEST_TYPE, resource.getType());
    assertNameReadably(resource.getName(), baseName);
    assertEquals(HttpMethod.GET.toString(), resource.getMethod());
    assertEquals(url, resource.getUrl());
    assertEquals(headers, resource.getHeaders());
    assertEquals(parameterPairs, resource.getParameters());
    assertEquals(body, resource.getBody());
    assertTimeEpoch(resource.getCreated());
    assertTimeEpoch(resource.getModified());
  }

  @Test
  void createWhenOperationNull() {
    Resource resource = new RequestResourceCreator(urlCreator, headersCreator, insomniaQueryParametersCreator, bodyCreator).create(null);

    assertNull(resource);
  }

  @Test
  void createWhenRequestNull() {
    when(operation.getRequest()).thenReturn(null);

    Resource resource = new RequestResourceCreator(urlCreator, headersCreator, insomniaQueryParametersCreator, bodyCreator).create(operation);

    assertNull(resource);
  }

  @Test
  void createWhenRequestUriNull() {
    when(request.getUri()).thenReturn(null);

    Resource resource = new RequestResourceCreator(urlCreator, headersCreator, insomniaQueryParametersCreator, bodyCreator).create(operation);

    assertNotNull(resource);
    assertNull(resource.getUrl());
  }

  @Test
  void createWhenHttpMethodNull() {
    when(request.getMethod()).thenReturn(null);

    Resource resource = new RequestResourceCreator(urlCreator, headersCreator, insomniaQueryParametersCreator, bodyCreator).create(operation);

    assertNotNull(resource);
    assertNull(resource.getMethod());
  }
}
