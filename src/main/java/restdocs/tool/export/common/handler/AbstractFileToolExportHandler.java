package restdocs.tool.export.common.handler;

import java.io.File;
import java.io.IOException;

public abstract class AbstractFileToolExportHandler implements ToolExportHandler {

  private File exportFile;
  private File docFile;

  @Override
  public void initialize(File workingDirectory, String applicationName) throws IOException {
    File implDir = getToolDirectory(workingDirectory);
    exportFile = initFile(implDir, ".export");
    docFile = initFile(implDir, ".adoc");
  }

  protected File getToolDirectory(File workingDirectory) {
    File file = new File(workingDirectory.getAbsolutePath() + "/" + getToolName().toLowerCase() + "-download");
    file.mkdir();

    return file;
  }

  protected File initFile(File implDir, String fileEnding) throws IOException {
    File file = new File(implDir.getAbsoluteFile() + "/" + getToolName().toLowerCase() + fileEnding);

    if(!file.exists()) {
      file.createNewFile();
    } else {
      file.delete();
      file.createNewFile();
    }

    return file;
  }

  protected File getExportFile() {
    return exportFile;
  }

  protected File getDocFile() {
    return docFile;
  }
}
