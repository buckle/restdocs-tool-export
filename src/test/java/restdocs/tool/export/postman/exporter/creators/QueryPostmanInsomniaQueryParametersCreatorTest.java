package restdocs.tool.export.postman.exporter.creators;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.restdocs.operation.OperationRequestFactory;
import org.springframework.restdocs.operation.QueryParameters;
import restdocs.tool.export.postman.exporter.QueryParam;
import restdocs.tool.export.postman.exporter.utils.QueryParamUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class QueryPostmanInsomniaQueryParametersCreatorTest {

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
