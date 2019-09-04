package restdocs.tool.export.postman.utils;

import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Sets;
import restdocs.tool.export.postman.exporter.QueryParam;
import restdocs.tool.export.postman.exporter.QueryParamBuilder;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class QueryParamUtilsTest {

  @Test
  void findQueryParamByKeyValue() {
    QueryParam queryParam1 = QueryParamBuilder.builder().build();
    QueryParam queryParam2 = QueryParamBuilder.builder().build();
    Set<QueryParam> queryParams = Sets.newSet(queryParam1, queryParam2);

    assertEquals(queryParam1, QueryParamUtils.findQueryParamByKeyValue(queryParams, queryParam1.getKey(), queryParam1.getValue()));
    assertEquals(queryParam2, QueryParamUtils.findQueryParamByKeyValue(queryParams, queryParam2.getKey(), queryParam2.getValue()));
    assertNull(QueryParamUtils.findQueryParamByKeyValue(queryParams, UUID.randomUUID().toString(), UUID.randomUUID().toString()));
  }

  @Test
  void findQueryParamByKeyValueWhenNullParams() {
    QueryParam queryParam1 = QueryParamBuilder.builder().build();

    assertNull(QueryParamUtils.findQueryParamByKeyValue(null, queryParam1.getKey(), queryParam1.getValue()));
  }

  @Test
  void findQueryParamByKeyValueWhenKeyNull() {
    QueryParam queryParam1 = QueryParamBuilder.builder().build();

    assertNull(QueryParamUtils.findQueryParamByKeyValue(Sets.newSet(queryParam1), null, queryParam1.getValue()));
  }

  @Test
  void findQueryParamByKeyValueWhenValueNull() {
    QueryParam queryParam1 = QueryParamBuilder.builder().build();

    assertNull(QueryParamUtils.findQueryParamByKeyValue(Sets.newSet(queryParam1), queryParam1.getKey(), null));
  }
}
