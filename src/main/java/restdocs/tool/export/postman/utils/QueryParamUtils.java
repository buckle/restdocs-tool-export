package restdocs.tool.export.postman.utils;

import restdocs.tool.export.postman.exporter.QueryParam;

import java.util.Set;

public class QueryParamUtils {

  public static QueryParam findQueryParamByKeyValue(Set<QueryParam> queryParams, String key, String value) {
    if(queryParams != null && key != null && value != null) {
      return queryParams.stream()
                        .filter(queryParam -> key.equals(queryParam.getKey()) &&
                                              value.equals(queryParam.getValue()))
                        .findFirst()
                        .orElse(null);
    }

    return null;
  }

}
