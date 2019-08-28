package restdocs.tool.export.common.file;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import restdocs.tool.export.utils.TestFileUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class RestDocsFileUtilsTest {

  private String toolName;

  @BeforeEach
  void setUp() {
    toolName = "ToolName" + UUID.randomUUID().toString();
  }

  @Test
  void createToolDirectoryWhenNew() throws Exception {
    File directory = TestFileUtils.creatTmpDirectory();

    File toolDirectory = RestDocsFileUtils.createToolDirectory(directory, toolName);
    toolDirectory.deleteOnExit();

    assertNotNull(toolDirectory);
    assertTrue(toolDirectory.isDirectory());
    assertTrue(toolDirectory.getAbsolutePath().endsWith(toolName.toLowerCase() + "-download"));
  }

  @Test
  void createToolDirectoryWhenAlreadyExists() throws Exception {
    File directory = TestFileUtils.creatTmpDirectory();
    TestFileUtils.creatDirectory(toolName.toLowerCase() + "-download", directory);

    File toolDirectory = RestDocsFileUtils.createToolDirectory(directory, toolName);
    toolDirectory.deleteOnExit();

    assertNotNull(toolDirectory);
    assertTrue(toolDirectory.isDirectory());
    assertTrue(toolDirectory.getAbsolutePath().endsWith(toolName.toLowerCase() + "-download"));
  }

  @Test
  void initToolFileWhenNew() throws Exception {
    File directory = TestFileUtils.creatTmpDirectory();
    String fileEnding = ".test";

    File file = RestDocsFileUtils.initToolFile(directory, fileEnding, toolName);
    file.deleteOnExit();

    assertNotNull(file);
    assertTrue(file.isFile());
    assertTrue(file.exists());
    assertTrue(file.getAbsolutePath().endsWith(toolName.toLowerCase() + fileEnding));
  }

  @Test
  void initToolFileWhenAlreadyExists() throws Exception {
    File directory = TestFileUtils.creatTmpDirectory();
    String fileEnding = ".test";
    File existingFile = TestFileUtils.createFile(toolName.toLowerCase() + fileEnding, directory);
    FileUtils.writeStringToFile(existingFile, "some exiting data", Charset.defaultCharset());

    File file = RestDocsFileUtils.initToolFile(directory, fileEnding, toolName);
    file.deleteOnExit();

    assertNotNull(file);
    assertTrue(file.isFile());
    assertTrue(file.exists());
    assertEquals(0, file.length());
    assertTrue(file.getAbsolutePath().endsWith(toolName.toLowerCase() + fileEnding));
  }
}
