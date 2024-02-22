package restdocs.tool.export.postman.variable;

import restdocs.tool.export.common.variable.BaseVariableHandler;
import restdocs.tool.export.common.variable.VariableHandler;

public class PostmanVariableHandler extends BaseVariableHandler implements VariableHandler {

  @Override
  public String encapsulateVariable(String variable) {
    if(variable != null) {
      return "{{" + variable + "}}";
    }

    return null;
  }
}
