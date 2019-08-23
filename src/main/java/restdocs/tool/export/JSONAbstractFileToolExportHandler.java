package restdocs.tool.export;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

public abstract class JSONAbstractFileToolExportHandler extends AbstractFileToolExportHandler {

  private Logger LOG = LoggerFactory.getLogger(this.getClass());
  private ObjectMapper objectMapper;

  @Override
  public void initialize(File workingDirectory, String applicationName) throws IOException {
    super.initialize(workingDirectory, applicationName);
    this.objectMapper = new ObjectMapper();
    this.objectMapper.registerModule(new JavaTimeModule());
    this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
  }

  @Override
  public void updateDocFile() throws IOException {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("link:++data:application/json;base64,");
    stringBuilder.append(Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(exportFile)));
    stringBuilder.append("++[Download - Right Click And Save As]");
    FileUtils.writeByteArrayToFile(docFile, stringBuilder.toString().getBytes(), false);
  }

  protected <T> T getFileData(Class<T> clazz) {
    T export = null;
    try {
      export = objectMapper.readValue(super.exportFile, clazz);
    } catch(Exception e) {
      if(super.exportFile.length() != 0) {
        LOG.error("Exception reading JSON export file.", e);
      }
    }

    return export;
  }

  protected void updateFileData(Object object) throws IOException {
    super.exportFile.delete();
    super.exportFile.createNewFile();

    objectMapper.writeValue(super.exportFile, object);
  }
}
