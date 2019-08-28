package restdocs.tool.export.common.document;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import restdocs.tool.export.utils.TestFileUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class DocumentorImplTest {

  private DocumentorImpl documentor;
  private File workingDirectory;
  private String applicationName;
  private String toolName;

  @BeforeEach
  void setUp() throws Exception {
    documentor = new DocumentorImpl();

    workingDirectory = TestFileUtils.creatTmpDirectory();
    applicationName = "appName" + UUID.randomUUID().toString();
    toolName = "ToolName" + UUID.randomUUID().toString().replaceAll("-", "");
  }

  @Test
  void initialize() throws Exception {
    documentor.initialize(workingDirectory, applicationName, toolName);

    File docFile = documentor.getDocFile();

    assertNotNull(docFile);
    assertTrue(docFile.isFile());
    assertTrue(docFile.getAbsolutePath().endsWith(".adoc"));
    assertTrue(docFile.getAbsolutePath().contains(workingDirectory.getAbsolutePath()));
    assertTrue(docFile.getAbsolutePath().contains(toolName.toLowerCase()));
  }

  @Test
  void document() throws Exception {
    documentor.initialize(workingDirectory, applicationName, toolName);

    String fileContent = "{\"field\":\"data\"}";
    File file = TestFileUtils.creatTmpFile(".json");
    FileUtils.writeByteArrayToFile(file, fileContent.getBytes(), false);

    documentor.document(file, "json");
    File docFile = documentor.getDocFile();

    assertNotNull(docFile);
    String docFileContent = FileUtils.readFileToString(docFile, Charset.defaultCharset());
    assertEquals("link:++data:application/json;base64," +
                 Base64.getEncoder().encodeToString(fileContent.getBytes()) +
                 "++[Download - Right Click And Save As]",
                 docFileContent);
  }
}
