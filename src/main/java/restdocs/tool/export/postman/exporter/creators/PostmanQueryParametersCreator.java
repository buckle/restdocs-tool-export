package restdocs.tool.export.postman.exporter.creators;

import org.springframework.restdocs.operation.QueryParameters;
import restdocs.tool.export.common.creator.Creator;
import restdocs.tool.export.postman.exporter.QueryParam;

import java.util.HashSet;
import java.util.Set;

import static restdocs.tool.export.postman.exporter.utils.QueryParamUtils.findQueryParamByKeyValue;

public class PostmanQueryParametersCreator implements Creator<Set<QueryParam>, QueryParameters> {

  @Override
  public Set<QueryParam> create(QueryParameters parameters) {
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
