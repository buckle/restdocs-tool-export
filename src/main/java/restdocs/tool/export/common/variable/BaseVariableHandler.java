package restdocs.tool.export.common.variable;

import restdocs.tool.export.ToolExportSnippet;
import restdocs.tool.export.common.utils.ExportUtils;

import java.util.regex.Pattern;

public class BaseVariableHandler implements VariableHandler {

  public final Pattern VARIABLE_PATTERN = Pattern.compile("<<([^>]*)>>");

  @Override
  public String createHostVariable(String applicationName) {
    String nameVariable = formatNameVariable(applicationName);
    if(nameVariable != null) {
      String hostVariable = nameVariable + VariableKeys.HOST;
      ToolExportSnippet.addVariable(hostVariable);
      return encapsulateVariable(hostVariable);
    }

    return null;
  }

  @Override
  public String replaceVariables(String value) {
    if (value != null) {
      return VARIABLE_PATTERN.matcher(value).replaceAll(r -> {
        String variableName = formatNameVariable(r.group(1));
        ToolExportSnippet.addVariable(variableName);
        return encapsulateVariable(variableName);
      });
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
