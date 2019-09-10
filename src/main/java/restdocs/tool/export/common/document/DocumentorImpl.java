package restdocs.tool.export.common.document;

import org.apache.commons.io.FileUtils;
import restdocs.tool.export.common.utils.RestDocsFileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class DocumentorImpl implements Documentor {

  private File docFile;

  @Override
  public void initialize(File workingDirectory, String applicationName, String toolName) throws IOException {
    File implDir = RestDocsFileUtils.createToolDirectory(workingDirectory, toolName);
    docFile = RestDocsFileUtils.initToolFile(implDir, ".adoc", toolName);
  }

  @Override
  public File getDocFile() {
    return docFile;
  }

  @Override
  public void document(File content, String type) throws IOException {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("link:++data:application/" + type + ";base64,");
    stringBuilder.append(Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(content)));
    stringBuilder.append("++[Download - Right Click And Save As]");
    FileUtils.writeByteArrayToFile(getDocFile(), stringBuilder.toString().getBytes(), false);
  }
}
