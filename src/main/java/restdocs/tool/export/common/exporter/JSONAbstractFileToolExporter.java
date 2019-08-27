package restdocs.tool.export.common.exporter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public abstract class JSONAbstractFileToolExporter extends AbstractFileToolExporter {

  private Logger LOG = LoggerFactory.getLogger(this.getClass());
  private ObjectMapper objectMapper;

  @Override
  public void initialize(File workingDirectory, String applicationName, String toolName) throws IOException {
    super.initialize(workingDirectory, applicationName, toolName);
    this.objectMapper = new ObjectMapper();
    this.objectMapper.registerModule(new JavaTimeModule());
    this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
  }

  @Override
  public <T> T getExportData(Class<T> clazz) {
    T export = null;
    File exportFile = getExportFile();
    try {
      export = objectMapper.readValue(exportFile, clazz);
    } catch(Exception e) {
      if(exportFile.length() != 0) {
        LOG.error("Exception reading JSON export file.", e);
      }
    }

    return export;
  }

  @Override
  public void updateExportData(Object object) throws IOException {
    objectMapper.writeValue(getExportFile(), object);
  }
}
