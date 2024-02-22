package restdocs.tool.export.postman.exporter.creators;

import org.springframework.restdocs.operation.QueryParameters;
import restdocs.tool.export.common.creator.Creator;
import restdocs.tool.export.postman.exporter.QueryParam;
import restdocs.tool.export.postman.variable.PostmanVariableHandler;

import java.util.HashSet;
import java.util.Set;

import static restdocs.tool.export.postman.exporter.utils.QueryParamUtils.findQueryParamByKeyValue;

public class PostmanQueryParametersCreator implements Creator<Set<QueryParam>, QueryParameters> {

  PostmanVariableHandler postmanVariableHandler;

  public PostmanQueryParametersCreator(PostmanVariableHandler postmanVariableHandler) {
    this.postmanVariableHandler = postmanVariableHandler;
  }

  public PostmanQueryParametersCreator() {
    this.postmanVariableHandler = new PostmanVariableHandler();
  }

  @Override
  public Set<QueryParam> create(QueryParameters parameters) {
    if(parameters != null && !parameters.isEmpty()) {
      Set<QueryParam> queryParams = new HashSet<>();

      parameters.forEach((name, values) -> values.forEach(value -> {
        if(findQueryParamByKeyValue(queryParams, name, value) == null) {
          QueryParam queryParam = new QueryParam();
          queryParam.setKey(postmanVariableHandler.replaceVariables(name));
          queryParam.setValue(postmanVariableHandler.replaceVariables(value));
          queryParams.add(queryParam);
        }
      }));

      return queryParams;
    }

    return null;
  }
}
