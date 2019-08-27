package restdocs.tool.export.common.file;

import java.io.File;
import java.io.IOException;

public class RestDocsFileUtils {

  public static File createToolDirectory(File workingDirectory, String toolName) {
    File file = new File(workingDirectory.getAbsolutePath() + "/" + toolName.toLowerCase() + "-download");
    file.mkdir();

    return file;
  }

  public static File initToolFile(File implDir, String fileEnding, String toolName) throws IOException {
    File file = new File(implDir.getAbsoluteFile() + "/" + toolName.toLowerCase() + fileEnding);

    if(!file.exists()) {
      file.createNewFile();
    } else {
      file.delete();
      file.createNewFile();
    }

    return file;
  }

}
