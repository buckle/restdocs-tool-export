package restdocs.tool.export.postman.exporter.creators;

import org.springframework.restdocs.operation.Parameters;
import restdocs.tool.export.common.creator.Creator;
import restdocs.tool.export.postman.exporter.QueryParam;

import java.util.Set;

public class ParametersCreator implements Creator<Set<QueryParam>, Parameters> {

  @Override
  public Set<QueryParam> create(Parameters parameters) {
    return null;
  }
}
