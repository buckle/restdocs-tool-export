package restdocs.tool.export;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

public abstract class AbstractFileToolExportHandler implements ToolExportHandler {

  protected File exportFile;
  protected File docFile;

  @Override
  public void initialize(File workingDirectory) throws IOException {
    File implDir = getToolDirectory(workingDirectory);
    exportFile = initFile(implDir, ".export");
    docFile = initFile(implDir, ".adoc");
  }

  @Override
  public void updateDocFile() throws IOException {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("link:++data:application/json;base64,");
    stringBuilder.append(Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(exportFile)));
    stringBuilder.append("++[Download - Right Click And Save As]");
    FileUtils.writeByteArrayToFile(docFile, stringBuilder.toString().getBytes(), false);
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
