package restdocs.tool.export.common.exporter;

import restdocs.tool.export.common.file.RestDocsFileUtils;

import java.io.File;
import java.io.IOException;

public abstract class AbstractFileToolExporter implements ToolExporter {

  private File exportFile;

  @Override
  public void initialize(File workingDirectory, String applicationName, String toolName) throws IOException {
    File implDir = RestDocsFileUtils.createToolDirectory(workingDirectory, toolName);
    exportFile = RestDocsFileUtils.initToolFile(implDir, ".export", toolName);
  }

  @Override
  public File getExportFile() {
    return exportFile;
  }
}
