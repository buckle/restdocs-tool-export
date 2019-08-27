package restdocs.tool.export.common.exporter;

import org.springframework.restdocs.operation.Operation;

import java.io.File;
import java.io.IOException;

public interface ToolExporter {

  void initialize(File workingDirectory, String applicationName, String toolName) throws IOException;

  void processOperation(Operation operation) throws IOException;

  File getExportFile();

  <T> T getExportData(Class<T> clazz);

  void updateExportData(Object object) throws IOException;
}
