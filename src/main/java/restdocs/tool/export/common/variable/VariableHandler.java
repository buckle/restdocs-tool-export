package restdocs.tool.export.common.variable;

public interface VariableHandler {

  String getHostVariable(String applicationName);

  String formatNameVariable(String name);

  String encapsulateVariable(String variable);
}
