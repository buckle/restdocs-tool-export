package restdocs.tool.export.postman.exporter.creators;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.restdocs.operation.OperationRequestFactory;
import org.springframework.restdocs.operation.QueryParameters;
import restdocs.tool.export.postman.exporter.QueryParam;
import restdocs.tool.export.postman.exporter.utils.QueryParamUtils;
import restdocs.tool.export.postman.variable.PostmanVariableHandler;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PostmanQueryParametersCreatorTest {

  @Test
  void create() throws Exception {
    QueryParameters parameters = QueryParameters.from(new OperationRequestFactory().create(new URI("/?param1=prize1&param2=prize2&param1=surprise"), HttpMethod.GET, null, null, null));

    Set<QueryParam> queryParams = new PostmanQueryParametersCreator().create(parameters);

    assertNotNull(queryParams);
    assertEquals(3, queryParams.size());
    assertNotNull(QueryParamUtils.findQueryParamByKeyValue(queryParams, "param1", "prize1"));
    assertNotNull(QueryParamUtils.findQueryParamByKeyValue(queryParams, "param2", "prize2"));
    assertNotNull(QueryParamUtils.findQueryParamByKeyValue(queryParams, "param1", "surprise"));
  }

  @Test
  void createWhenDuplicateNameValueParameters() throws Exception {
    QueryParameters parameters = QueryParameters.from(new OperationRequestFactory().create(new URI("/?param1=prize1&param1=prize1"), HttpMethod.GET, null, null, null));

    Set<QueryParam> queryParams = new PostmanQueryParametersCreator().create(parameters);

    assertNotNull(queryParams);
    assertEquals(1, queryParams.size());
    assertNotNull(QueryParamUtils.findQueryParamByKeyValue(queryParams, "param1", "prize1"));
  }

  @Test
  void createWhenVariableParamValue() throws Exception {
    URI uri = new URI("https", "localhost", "/", "param1=prize1&<<param2>>=<<prize2>>", null);
    QueryParameters parameters = QueryParameters.from(new OperationRequestFactory().create(uri, HttpMethod.GET, null, null, null));
    PostmanVariableHandler postmanVariableHandler = (spy(PostmanVariableHandler.class));
    String replacedQueryKey = "replaced";
    String replacedQueryValue = "replaced-value";
    when(postmanVariableHandler.replaceVariables(eq("<<param2>>"))).thenReturn(replacedQueryKey);
    when(postmanVariableHandler.replaceVariables(eq("<<prize2>>"))).thenReturn(replacedQueryValue);

    Set<QueryParam> queryParams = new PostmanQueryParametersCreator(postmanVariableHandler).create(parameters);

    assertNotNull(queryParams);
    assertEquals(2, queryParams.size());
    QueryParam param1Value1 = QueryParamUtils.findQueryParamByKeyValue(queryParams, "param1", "prize1");
    assertNotNull(param1Value1);

    QueryParam param2Value2 = QueryParamUtils.findQueryParamByKeyValue(queryParams, replacedQueryKey, replacedQueryValue);
    assertNotNull(param2Value2);

    verify(postmanVariableHandler).replaceVariables("param1");
    verify(postmanVariableHandler).replaceVariables("prize1");
    verify(postmanVariableHandler).replaceVariables("<<param2>>");
    verify(postmanVariableHandler).replaceVariables("<<prize2>>");
  }

  @Test
  void createWhenNullParameters() {
    assertNull(new PostmanQueryParametersCreator().create(null));
  }

  @Test
  void createWhenEmptyParameters() throws URISyntaxException {
    QueryParameters parameters = QueryParameters.from(new OperationRequestFactory().create(new URI("/test"), HttpMethod.GET, null, null, null));
    assertTrue(parameters.isEmpty());
    assertNull(new PostmanQueryParametersCreator().create(parameters));
  }
}
