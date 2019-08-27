package restdocs.tool.export.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class TestFileUtils {

  public static File creatTmpDirectory() throws IOException {
    File directory = Files.createTempDirectory("restdocs").toFile();
    directory.deleteOnExit();
    return directory;
  }

  public static File creatDirectory(String path, File parentDir) throws IOException {
    File directory = new File(parentDir.getAbsolutePath() + "/" + path);
    directory.mkdir();
    directory.deleteOnExit();
    return directory;
  }

  public static File creatTmpFile(String suffix) throws IOException {
    File directory = Files.createTempFile("restdocs", suffix).toFile();
    directory.deleteOnExit();
    return directory;
  }

  public static File createFile(String path, File parentDir) {
    File file = new File(parentDir.getAbsolutePath() + "/" + path);
    file.deleteOnExit();
    return file;
  }
}
