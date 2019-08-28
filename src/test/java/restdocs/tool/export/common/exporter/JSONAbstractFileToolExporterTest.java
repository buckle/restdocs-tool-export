package restdocs.tool.export.common.exporter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import restdocs.tool.export.utils.TestFileUtils;

import java.io.File;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

public class JSONAbstractFileToolExporterTest {

  private JSONAbstractFileToolExporter jsonAbstractFileToolExporter;
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() throws Exception {
    objectMapper = new ObjectMapper();
    jsonAbstractFileToolExporter = spy(JSONAbstractFileToolExporter.class);

    String toolName = "ToolName" + UUID.randomUUID().toString().replaceAll("-", "");
    String applicationName = "Test";
    File tmpDirectory = TestFileUtils.creatTmpDirectory();

    jsonAbstractFileToolExporter.initialize(tmpDirectory, applicationName, toolName);
  }

  @Test
  void getExportDataWhenNew() {
    TestJson testJson = jsonAbstractFileToolExporter.getExportData(TestJson.class);

    assertNull(testJson);
  }

  @Test
  void getExportDataWhenExistingData() throws Exception {
    TestJson testJson = new TestJson();
    testJson.setField1("Some data");
    testJson.setField2("Some data 2");

    objectMapper.writeValue(jsonAbstractFileToolExporter.getExportFile(), testJson);

    TestJson readTestJson = jsonAbstractFileToolExporter.getExportData(TestJson.class);

    assertNotNull(readTestJson);
    assertEquals(testJson.getField1(), readTestJson.getField1());
    assertEquals(testJson.getField2(), readTestJson.getField2());
  }

  @Test
  void getExportDataWhenExistingDataAndFailsMapping() throws Exception {
    TestJson testJson = new TestJson();
    testJson.setField1("Some data");
    testJson.setField2("Some data 2");

    objectMapper.writeValue(jsonAbstractFileToolExporter.getExportFile(), testJson);

    String fileData = jsonAbstractFileToolExporter.getExportData(String.class);

    assertNull(fileData);
  }

  @Test
  void updateExportDataWhenNew() throws Exception {
    TestJson testJson = new TestJson();
    testJson.setField1("Some data");
    testJson.setField2("Some data 2");

    jsonAbstractFileToolExporter.updateExportData(testJson);

    TestJson readTestJson = objectMapper.readValue(jsonAbstractFileToolExporter.getExportFile(), TestJson.class);

    assertNotNull(readTestJson);
    assertEquals(testJson.getField1(), readTestJson.getField1());
    assertEquals(testJson.getField2(), readTestJson.getField2());
  }

  @Test
  void updateExportDataWhenExistingData() throws Exception {
    TestJson testJson = new TestJson();
    testJson.setField1("Some data" + UUID.randomUUID().toString());
    testJson.setField2("Some data 2" + UUID.randomUUID().toString());

    objectMapper.writeValue(jsonAbstractFileToolExporter.getExportFile(), testJson);

    TestJson testJsonNew = new TestJson();
    testJsonNew.setField1("Some data");
    testJsonNew.setField2("Some data 2");

    jsonAbstractFileToolExporter.updateExportData(testJsonNew);

    TestJson readTestJson = objectMapper.readValue(jsonAbstractFileToolExporter.getExportFile(), TestJson.class);

    assertNotNull(readTestJson);
    assertEquals(testJsonNew.getField1(), readTestJson.getField1());
    assertEquals(testJsonNew.getField2(), readTestJson.getField2());
  }
}
