package restdocs.tool.export.common.exporter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import restdocs.tool.export.utils.TestFileUtils;

import java.io.File;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.spy;

public class AbstractFileToolExporterTest {

  private AbstractFileToolExporter abstractFileToolExporter;

  @BeforeEach
  void setUp() {
    abstractFileToolExporter = spy(AbstractFileToolExporter.class);
  }

  @Test
  void initialize() throws Exception {
    File workingDirectory = TestFileUtils.creatTmpDirectory();
    String applicationName = "appName" + UUID.randomUUID().toString();
    String toolName = "ToolName" + UUID.randomUUID().toString().replaceAll("-", "");

    abstractFileToolExporter.initialize(workingDirectory, applicationName, toolName);

    File exportFile = abstractFileToolExporter.getExportFile();

    assertNotNull(exportFile);
    assertTrue(exportFile.isFile());
    assertTrue(exportFile.getAbsolutePath().endsWith(".export"));
    assertTrue(exportFile.getAbsolutePath().contains(workingDirectory.getAbsolutePath()));
    assertTrue(exportFile.getAbsolutePath().contains(toolName.toLowerCase()));
  }

}
