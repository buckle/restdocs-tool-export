package restdocs.tool.export.common.variable;

import restdocs.tool.export.common.utils.ExportUtils;

public class BaseVariableHandler implements VariableHandler {

  @Override
  public String getHostVariable(String applicationName) {
    String nameVariable = formatNameVariable(applicationName);
    if(nameVariable != null) {
      return encapsulateVariable(nameVariable + VariableKeys.HOST);
    }

    return null;
  }

  @Override
  public String formatNameVariable(String name) {
    String readably = ExportUtils.formatNameReadably(name);

    if(readably != null) {
      return readably.replaceAll(" ", "_").toLowerCase();
    }

    return null;
  }

  @Override
  public String encapsulateVariable(String variable) {
    return variable;
  }
}
