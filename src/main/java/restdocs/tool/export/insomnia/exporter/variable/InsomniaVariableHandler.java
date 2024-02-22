package restdocs.tool.export.insomnia.exporter.variable;

import restdocs.tool.export.common.variable.BaseVariableHandler;
import restdocs.tool.export.common.variable.VariableHandler;

public class InsomniaVariableHandler extends BaseVariableHandler implements VariableHandler {

  @Override
  public String encapsulateVariable(String variable) {
    if(variable != null) {
      return "{{_." + variable + "}}";
    }

    return null;
  }
}
