package restdocs.tool.export;

import org.springframework.restdocs.operation.Operation;

import java.io.File;
import java.io.IOException;

public interface ToolExportHandler {

  void initialize(File workingDirectory) throws IOException;

  String getToolName();

  void handleOperation(Operation operation, String applicationName) throws IOException;

  void updateDocFile() throws IOException;

}
