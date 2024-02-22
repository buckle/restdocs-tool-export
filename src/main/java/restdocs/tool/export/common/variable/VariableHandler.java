package restdocs.tool.export.common.variable;

public interface VariableHandler {

  String createHostVariable(String applicationName);

  String replaceVariables(String value);

  String formatNameVariable(String name);

  String encapsulateVariable(String variable);
}
