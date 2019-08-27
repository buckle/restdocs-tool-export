package restdocs.tool.export.common.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import restdocs.tool.export.utils.TestFileUtils;

import java.io.File;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class JSONAbstractFileToolExportHandlerTest {

  private JSONAbstractFileToolExportHandler jsonAbstractFileToolExportHandler;
  private File exportFile;
  private File docFile;

  @BeforeEach
  void setUp() throws Exception {
    jsonAbstractFileToolExportHandler = spy(JSONAbstractFileToolExportHandler.class);
    exportFile = TestFileUtils.creatTmpFile(".export");
    doReturn(exportFile).when(jsonAbstractFileToolExportHandler).getExportFile();
    docFile = TestFileUtils.creatTmpFile(".adoc");
    doReturn(docFile).when(jsonAbstractFileToolExportHandler).getDocFile();
  }

  @Test
  void getFileData() {

    // TODO
    jsonAbstractFileToolExportHandler.getFileData(TestJson.class);

  }
}
