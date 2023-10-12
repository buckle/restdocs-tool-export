package restdocs.tool.export.postman.exporter.creators;

import org.junit.jupiter.api.Test;
import org.springframework.restdocs.operation.OperationRequest;
import org.springframework.restdocs.operation.QueryParameters;
import restdocs.tool.export.postman.exporter.QueryParam;
import restdocs.tool.export.postman.exporter.Url;

import java.net.URI;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UrlCreatorTest {

  @Test
  void create() throws Exception {
    String rawUrl = "https://localhost.somedomain.io:8080/path/stuff?param1=pval1";
    URI uri = new URI(rawUrl);

    OperationRequest request = mock(OperationRequest.class);
    when(request.getUri()).thenReturn(uri);

    // PostmanQueryParameterCreate retrieves the query params directly from the uri
    PostmanQueryParametersCreator queryParametersCreator = spy(new PostmanQueryParametersCreator());
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

    assertEquals(1, url.getQuery().size());
    QueryParam firstQueryParam = url.getQuery().iterator().next();
    assertEquals("param1", firstQueryParam.getKey());
    assertEquals("pval1", firstQueryParam.getValue());
    verify(queryParametersCreator).create(QueryParameters.from(request));
  }

  @Test
  void createWhenNullRequest() {
    assertNull(new UrlCreator().create(null));
  }

  @Test
  void createWhenNullRequestURI() {
    OperationRequest request = mock(OperationRequest.class);
    when(request.getUri()).thenReturn(null);

    Url url = new UrlCreator().create(request);
    assertNull(url);
  }

  @Test
  void createWhenEmptyRequestURI() throws Exception {
    URI uri = new URI("");

    OperationRequest request = mock(OperationRequest.class);
    when(request.getUri()).thenReturn(uri);

    Url url = new UrlCreator().create(request);

    assertNotNull(url);
    assertNull(url.getRaw());
    assertNull(url.getProtocol());
    assertNull(url.getHost());
    assertNull(url.getPort());
    assertNull(url.getPath());
    assertNull(url.getQuery());
  }

  @Test
  void createWhenNoRequestParameters() throws Exception {
    String rawUrl = "https://localhost.somedomain.io:8080/path/stuff";
    URI uri = new URI(rawUrl);

    OperationRequest request = mock(OperationRequest.class);
    when(request.getUri()).thenReturn(uri);

    Url url = new UrlCreator().create(request);

    assertNotNull(url);
    assertNotNull(url.getRaw());
    assertNull(url.getQuery());
  }
}
