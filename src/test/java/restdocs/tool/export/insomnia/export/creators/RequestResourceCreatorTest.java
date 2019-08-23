package restdocs.tool.export.insomnia.export.creators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Sets;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.operation.OperationRequest;
import org.springframework.restdocs.operation.Parameters;
import restdocs.tool.export.insomnia.export.Pair;
import restdocs.tool.export.insomnia.export.PairBuilder;
import restdocs.tool.export.insomnia.export.Resource;

import java.net.URI;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static restdocs.tool.export.insomnia.export.InsomniaAssertionUtils.*;
import static restdocs.tool.export.insomnia.export.InsomniaConstants.REQUEST_ID;
import static restdocs.tool.export.insomnia.export.InsomniaConstants.REQUEST_TYPE;

public class RequestResourceCreatorTest {

  private HeadersCreator headersCreator;
  private ParametersCreator parametersCreator;
  private Operation operation;
  private OperationRequest request;

  @BeforeEach
  void setUp() {
    headersCreator = mock(HeadersCreator.class);
    parametersCreator = mock(ParametersCreator.class);
    operation = mock(Operation.class);
    request = mock(OperationRequest.class);
    when(operation.getRequest()).thenReturn(request);
  }

  @Test
  void create() throws Exception {
    String name = "Name " + UUID.randomUUID();
    when(operation.getName()).thenReturn(name);

    URI uri = new URI("/a/url");
    when(request.getUri()).thenReturn(uri);

    when(request.getMethod()).thenReturn(HttpMethod.GET);

    HttpHeaders httpHeaders = mock(HttpHeaders.class);
    when(request.getHeaders()).thenReturn(httpHeaders);
    Set<Pair> headers = Sets.newSet(PairBuilder.builder().build());
    when(headersCreator.create(httpHeaders)).thenReturn(headers);

    Parameters parameters = mock(Parameters.class);
    when(request.getParameters()).thenReturn(parameters);
    Set<Pair> parameterPairs = Sets.newSet(PairBuilder.builder().build());
    when(parametersCreator.create(parameters)).thenReturn(parameterPairs);

    String body = UUID.randomUUID().toString();
    when(request.getContentAsString()).thenReturn(body);

    Resource resource = new RequestResourceCreator(headersCreator, parametersCreator).create(operation);

    assertNotNull(resource);
    assertIdMatches(REQUEST_ID, resource.getId());
    assertEquals(REQUEST_TYPE, resource.getType());
    assertName(resource.getName());
    assertEquals(HttpMethod.GET.toString(), resource.getMethod());
    assertEquals(uri.toString(), resource.getUrl());
    assertEquals(headers, resource.getHeaders());
    assertEquals(parameterPairs, resource.getParameters());
    assertEquals(body, resource.getBody().getText());
    assertTimeEpoch(resource.getCreated());
    assertTimeEpoch(resource.getModified());
  }
}
