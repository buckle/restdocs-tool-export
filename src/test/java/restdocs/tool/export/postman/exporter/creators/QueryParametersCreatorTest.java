package restdocs.tool.export.postman.exporter.creators;

import org.junit.jupiter.api.Test;
import org.springframework.restdocs.operation.Parameters;
import org.springframework.restdocs.operation.QueryStringParser;
import restdocs.tool.export.postman.exporter.QueryParam;
import restdocs.tool.export.postman.exporter.utils.QueryParamUtils;

import java.net.URI;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class QueryParametersCreatorTest {

  @Test
  void create() throws Exception {
    Parameters parameters = new QueryStringParser().parse(new URI("/?param1=prize1&param2=prize2&param1=surprise"));

    Set<QueryParam> queryParams = new QueryParametersCreator().create(parameters);

    assertNotNull(queryParams);
    assertEquals(3, queryParams.size());
    assertNotNull(QueryParamUtils.findQueryParamByKeyValue(queryParams, "param1", "prize1"));
    assertNotNull(QueryParamUtils.findQueryParamByKeyValue(queryParams, "param2", "prize2"));
    assertNotNull(QueryParamUtils.findQueryParamByKeyValue(queryParams, "param1", "surprise"));
  }

  @Test
  void createWhenDuplicateNameValueParameters() throws Exception {
    Parameters parameters = new QueryStringParser().parse(new URI("/?param1=prize1&param1=prize1"));

    Set<QueryParam> queryParams = new QueryParametersCreator().create(parameters);

    assertNotNull(queryParams);
    assertEquals(1, queryParams.size());
    assertNotNull(QueryParamUtils.findQueryParamByKeyValue(queryParams, "param1", "prize1"));
  }

  @Test
  void createWhenNullParameters() {
    assertNull(new QueryParametersCreator().create(null));
  }

  @Test
  void createWhenEmptyParameters() {
    assertNull(new QueryParametersCreator().create(new Parameters()));
  }
}
