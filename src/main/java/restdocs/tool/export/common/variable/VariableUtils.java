package restdocs.tool.export.common.variable;

import restdocs.tool.export.common.utils.ExportUtils;

public class VariableUtils {

  public static String getHostVariable(String applicationName) {
    String nameVariable = formatNameVariable(applicationName);
    if(nameVariable != null) {
      return nameVariable + VariableKeys.HOST;
    }

    return null;
  }

  public static String formatNameVariable(String name) {
    String readably = ExportUtils.formatNameReadably(name);

    if(readably != null) {
      return readably.replaceAll(" ", "_").toLowerCase();
    }

    return null;
  }
}
