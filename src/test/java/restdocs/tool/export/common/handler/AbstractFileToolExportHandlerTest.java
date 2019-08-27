package restdocs.tool.export.common.handler;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import restdocs.tool.export.utils.TestFileUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AbstractFileToolExportHandlerTest {

  private AbstractFileToolExportHandler abstractFileToolExportHandler;
  private String toolName;

  @BeforeEach
  void setUp() {
    abstractFileToolExportHandler = spy(AbstractFileToolExportHandler.class);
    toolName = "ToolName" + UUID.randomUUID().toString().replaceAll("-", "");
    doReturn(toolName).when(abstractFileToolExportHandler).getToolName();
  }

  @Test
  void initialize() throws Exception {
    File directory = TestFileUtils.creatTmpDirectory();
    File toolDir = mock(File.class);
    doReturn(toolDir).when(abstractFileToolExportHandler).getToolDirectory(directory);

    File exportFile = mock(File.class);
    doReturn(exportFile).when(abstractFileToolExportHandler).initFile(toolDir, ".export");

    File docFile = mock(File.class);
    doReturn(docFile).when(abstractFileToolExportHandler).initFile(toolDir, ".adoc");

    abstractFileToolExportHandler.initialize(directory, null);

    assertEquals(exportFile, abstractFileToolExportHandler.getExportFile());
    assertEquals(docFile, abstractFileToolExportHandler.getDocFile());
  }

  @Test
  void getToolDirectoryWhenNew() throws Exception {
    File directory = TestFileUtils.creatTmpDirectory();

    File toolDirectory = abstractFileToolExportHandler.getToolDirectory(directory);
    toolDirectory.deleteOnExit();

    assertNotNull(toolDirectory);
    assertTrue(toolDirectory.isDirectory());
    assertTrue(toolDirectory.getAbsolutePath().endsWith(toolName.toLowerCase() + "-download"));
  }

  @Test
  void getToolDirectoryWhenAlreadyExists() throws Exception {
    File directory = TestFileUtils.creatTmpDirectory();
    TestFileUtils.creatDirectory( toolName.toLowerCase() + "-download", directory);

    File toolDirectory = abstractFileToolExportHandler.getToolDirectory(directory);
    toolDirectory.deleteOnExit();

    assertNotNull(toolDirectory);
    assertTrue(toolDirectory.isDirectory());
    assertTrue(toolDirectory.getAbsolutePath().endsWith(toolName.toLowerCase() + "-download"));
  }

  @Test
  void initFileWhenNew() throws Exception {
    File directory = TestFileUtils.creatTmpDirectory();
    String fileEnding = ".test";

    File file = abstractFileToolExportHandler.initFile(directory, fileEnding);
    file.deleteOnExit();

    assertNotNull(file);
    assertTrue(file.isFile());
    assertTrue(file.exists());
    assertTrue(file.getAbsolutePath().endsWith(toolName.toLowerCase() + fileEnding));
  }

  @Test
  void initFileWhenAlreadyExists() throws Exception {
    File directory = TestFileUtils.creatTmpDirectory();
    String fileEnding = ".test";
    File existingFile = TestFileUtils.createFile(toolName.toLowerCase() + fileEnding, directory);
    FileUtils.writeStringToFile(existingFile, "some exiting data", Charset.defaultCharset());

    File file = abstractFileToolExportHandler.initFile(directory, fileEnding);
    file.deleteOnExit();

    assertNotNull(file);
    assertTrue(file.isFile());
    assertTrue(file.exists());
    assertEquals(0, file.length());
    assertTrue(file.getAbsolutePath().endsWith(toolName.toLowerCase() + fileEnding));
  }
}
