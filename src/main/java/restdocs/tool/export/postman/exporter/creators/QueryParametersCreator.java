package restdocs.tool.export.postman.exporter.creators;

import org.springframework.restdocs.operation.Parameters;
import restdocs.tool.export.common.creator.Creator;
import restdocs.tool.export.postman.exporter.QueryParam;

import java.util.HashSet;
import java.util.Set;

import static restdocs.tool.export.postman.utils.QueryParamUtils.findQueryParamByKeyValue;

public class QueryParametersCreator implements Creator<Set<QueryParam>, Parameters> {

  @Override
  public Set<QueryParam> create(Parameters parameters) {
    if(parameters != null && !parameters.isEmpty()) {
      Set<QueryParam> queryParams = new HashSet<>();

      parameters.forEach((name, values) -> values.forEach(value -> {
        if(findQueryParamByKeyValue(queryParams, name, value) == null) {
          QueryParam queryParam = new QueryParam();
          queryParam.setKey(name);
          queryParam.setValue(value);
          queryParams.add(queryParam);
        }
      }));

      return queryParams;
    }

    return null;
  }
}
