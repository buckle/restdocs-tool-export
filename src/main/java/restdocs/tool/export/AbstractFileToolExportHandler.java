package restdocs.tool.export;

import java.io.File;
import java.io.IOException;

public abstract class AbstractFileToolExportHandler implements ToolExportHandler {

  protected File exportFile;
  protected File docFile;

  @Override
  public void initialize(File workingDirectory) throws IOException {
    File implDir = getToolDirectory(workingDirectory);
    exportFile = initFile(implDir, ".export");
    docFile = initFile(implDir, ".adoc");
  }

  protected File getToolDirectory(File workingDirectory) {
    File file = new File(workingDirectory.getAbsolutePath() + "/" + getToolName().toLowerCase() + "-download");

    if(!file.exists()) {
      file.mkdir();
    }

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
}
