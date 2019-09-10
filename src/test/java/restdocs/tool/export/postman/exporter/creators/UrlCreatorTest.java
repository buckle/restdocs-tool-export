package restdocs.tool.export.postman.exporter.creators;

import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Sets;
import org.springframework.restdocs.operation.OperationRequest;
import org.springframework.restdocs.operation.Parameters;
import restdocs.tool.export.postman.exporter.QueryParam;
import restdocs.tool.export.postman.exporter.QueryParamBuilder;
import restdocs.tool.export.postman.exporter.Url;

import java.net.URI;
import java.util.Iterator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UrlCreatorTest {

  @Test
  void create() throws Exception {
    String rawUrl = "https://localhost.somedomain.io:8080/path/stuff?param1=pval1";
    URI uri = new URI(rawUrl);
    Parameters parameters = mock(Parameters.class);

    OperationRequest request = mock(OperationRequest.class);
    when(request.getUri()).thenReturn(uri);
    when(request.getParameters()).thenReturn(parameters);

    Set<QueryParam> queryParams = Sets.newSet(QueryParamBuilder.builder().build());
    QueryParametersCreator queryParametersCreator = mock(QueryParametersCreator.class);
    when(queryParametersCreator.create(parameters)).thenReturn(queryParams);

    Url url = new UrlCreator(queryParametersCreator).create(request);

    assertNotNull(url);
    assertEquals(rawUrl, url.getRaw());
    assertEquals("https", url.getProtocol());

    Iterator<String> hostIter = url.getHost().iterator();
    int hostAssert = 0;
    while(hostIter.hasNext()) {
      String hostPart = hostIter.next();

      switch(hostAssert) {
        case 0:
          assertEquals("localhost", hostPart);
          hostAssert++;
          break;
        case 1:
          assertEquals("somedomain", hostPart);
          hostAssert++;
          break;
        case 2:
          assertEquals("io", hostPart);
          hostAssert++;
          break;
      }
    }
    assertEquals(3, hostAssert);

    assertEquals(8080, url.getPort());

    Iterator<String> pathIter = url.getPath().iterator();
    int pathAssert = 0;
    while(pathIter.hasNext()) {
      String pathPart = pathIter.next();

      switch(pathAssert) {
        case 0:
          assertEquals("path", pathPart);
          pathAssert++;
          break;
        case 1:
          assertEquals("stuff", pathPart);
          pathAssert++;
          break;
      }
    }
    assertEquals(2, pathAssert);

    assertEquals(queryParams, url.getQueryParams());
  }

  @Test
  void createWhenNullRequest() {
    assertNull(new UrlCreator().create(null));
  }

  @Test
  void createWhenNullRequestURI() {
    Parameters parameters = mock(Parameters.class);

    OperationRequest request = mock(OperationRequest.class);
    when(request.getUri()).thenReturn(null);
    when(request.getParameters()).thenReturn(parameters);

    Set<QueryParam> queryParams = Sets.newSet();
    QueryParametersCreator queryParametersCreator = mock(QueryParametersCreator.class);
    when(queryParametersCreator.create(parameters)).thenReturn(queryParams);

    Url url = new UrlCreator(queryParametersCreator).create(request);

    assertNull(url);
  }

  @Test
  void creatWhenEmptyRequestURI() throws Exception {
    URI uri = new URI("");
    Parameters parameters = mock(Parameters.class);

    OperationRequest request = mock(OperationRequest.class);
    when(request.getUri()).thenReturn(uri);
    when(request.getParameters()).thenReturn(parameters);

    Set<QueryParam> queryParams = Sets.newSet();
    QueryParametersCreator queryParametersCreator = mock(QueryParametersCreator.class);
    when(queryParametersCreator.create(parameters)).thenReturn(queryParams);

    Url url = new UrlCreator().create(request);

    assertNotNull(url);
    assertNull(url.getRaw());
    assertNull(url.getProtocol());
    assertNull(url.getHost());
    assertNull(url.getPort());
    assertNull(url.getPath());
    assertEquals(queryParams, url.getQueryParams());
  }

  @Test
  void createWhenNullRequestParameters() throws Exception {
    String rawUrl = "https://localhost.somedomain.io:8080/path/stuff?param1=pval1";
    URI uri = new URI(rawUrl);

    OperationRequest request = mock(OperationRequest.class);
    when(request.getUri()).thenReturn(uri);
    when(request.getParameters()).thenReturn(null);

    Url url = new UrlCreator().create(request);

    assertNotNull(url);
    assertNotNull(url.getRaw());
    assertNull(url.getQueryParams());
  }
}
