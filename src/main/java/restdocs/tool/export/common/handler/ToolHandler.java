package restdocs.tool.export.common.handler;

import org.springframework.restdocs.operation.Operation;

import java.io.File;
import java.io.IOException;

public interface ToolHandler {

  void initialize(File workingDirectory, String applicationName) throws IOException;

  String getToolName();

  void handleOperation(Operation operation) throws IOException;

}
