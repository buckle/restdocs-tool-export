package restdocs.tool.export.postman.exporter.creators;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.operation.OperationRequest;
import org.springframework.restdocs.operation.QueryParameters;
import restdocs.tool.export.ToolExportSnippet;
import restdocs.tool.export.common.ExportConstants;
import restdocs.tool.export.postman.exporter.QueryParam;
import restdocs.tool.export.postman.exporter.Url;
import restdocs.tool.export.postman.variable.PostmanVariableHandler;

import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UrlCreatorTest {

  @Test
  void create() throws Exception {
    try (MockedStatic<ToolExportSnippet> mockToolExportSnippet = mockStatic(ToolExportSnippet.class)) {

      String rawUrl = "https://localhost.somedomain.io:8080/path/stuff?param1=pval1";
      URI uri = new URI(rawUrl);

      OperationRequest request = mock(OperationRequest.class);
      when(request.getUri()).thenReturn(uri);

      Operation operation = mock(Operation.class);
      when(operation.getRequest()).thenReturn(request);

      // PostmanQueryParameterCreate retrieves the query params directly from the uri
      PostmanQueryParametersCreator queryParametersCreator = spy(new PostmanQueryParametersCreator());
      Url url = new UrlCreator(queryParametersCreator, new PostmanVariableHandler()).create(operation);

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
      mockToolExportSnippet.verify(() -> ToolExportSnippet.addVariable(anyString()), never());
    }
  }

  @Test
  void createWhenHostVariableEnabled() throws Exception {
    try (MockedStatic<ToolExportSnippet> mockToolExportSnippet = mockStatic(ToolExportSnippet.class)) {

      String rawUrl = "https://localhost.somedomain.io:8080/path/stuff?param1=pval1";
      URI uri = new URI(rawUrl);

      String applicationName = "AppName";
      Map<String, Object> attributes = new HashMap<>();
      attributes.put(ExportConstants.APPLICATION_NAME, applicationName);
      attributes.put(ExportConstants.HOST_VARIABLE_ENABLED, true);

      OperationRequest request = mock(OperationRequest.class);
      when(request.getUri()).thenReturn(uri);

      Operation operation = mock(Operation.class);
      when(operation.getRequest()).thenReturn(request);
      when(operation.getAttributes()).thenReturn(attributes);

      PostmanVariableHandler postmanVariableHandler = spy(PostmanVariableHandler.class);
      String hostVariable = "{{appname_host}}";
      when(postmanVariableHandler.getHostVariable(applicationName)).thenReturn(hostVariable);

      PostmanQueryParametersCreator queryParametersCreator = spy(new PostmanQueryParametersCreator(postmanVariableHandler));
      Url url = new UrlCreator(queryParametersCreator, new PostmanVariableHandler()).create(operation);

      assertNotNull(url);
      assertEquals(hostVariable, url.getRaw());
      assertNull(url.getProtocol());

      assertEquals(1, url.getHost().size());
      String hostPart = url.getHost().iterator().next();
      assertEquals(hostVariable, hostPart);

      assertNull(url.getPort());

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
      mockToolExportSnippet.verify(() -> ToolExportSnippet.addVariable(hostVariable));
    }
  }

  @Test
  void createWhenNullRequest() {
    assertNull(new UrlCreator().create(null));
  }

  @Test
  void createWhenNullRequestURI() {
    OperationRequest request = mock(OperationRequest.class);
    when(request.getUri()).thenReturn(null);

    Operation operation = mock(Operation.class);
    when(operation.getRequest()).thenReturn(request);

    Url url = new UrlCreator().create(operation);
    assertNull(url);
  }

  @Test
  void createWhenEmptyRequestURI() throws Exception {
    URI uri = new URI("");

    OperationRequest request = mock(OperationRequest.class);
    when(request.getUri()).thenReturn(uri);

    Operation operation = mock(Operation.class);
    when(operation.getRequest()).thenReturn(request);
    Url url = new UrlCreator().create(operation);

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

    Operation operation = mock(Operation.class);
    when(operation.getRequest()).thenReturn(request);
    Url url = new UrlCreator().create(operation);

    assertNotNull(url);
    assertNotNull(url.getRaw());
    assertNull(url.getQuery());
  }
}
