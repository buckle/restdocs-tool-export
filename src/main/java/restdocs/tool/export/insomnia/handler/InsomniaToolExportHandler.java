package restdocs.tool.export.insomnia.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.restdocs.operation.Operation;
import org.springframework.stereotype.Component;
import restdocs.tool.export.AbstractFileToolExportHandler;
import restdocs.tool.export.insomnia.domain.Export;
import restdocs.tool.export.insomnia.exporter.InsomniaExporter;

import java.io.File;
import java.io.IOException;

@Component
public class InsomniaToolExportHandler extends AbstractFileToolExportHandler {

  private Logger LOG = LoggerFactory.getLogger(this.getClass());
  private InsomniaExporter insomniaExporter;
  private ObjectMapper objectMapper;

  @Override
  public void initialize(File workingDirectory) throws IOException {
    super.initialize(workingDirectory);
    this.objectMapper = new ObjectMapper();
    this.objectMapper.registerModule(new JavaTimeModule());
    this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    this.insomniaExporter =  new InsomniaExporter();
  }

  @Override
  public String getToolName() {
    return "INSOMNIA";
  }

  @Override
  public void handleOperation(Operation operation) throws IOException {
    Export export = getExport(super.exportFile);
    export = insomniaExporter.processOperation(operation, export);
    updateJSONFile(super.exportFile, export);
  }

  protected Export getExport(File jsonFile) {
    Export export = null;
    try {
      export = objectMapper.readValue(jsonFile, Export.class);
    } catch(Exception e) {
      if(jsonFile.length() != 0) {
        LOG.error("Exception reading Insomnia export file.", e);
      }
    }

    return export;
  }

  protected void updateJSONFile(File jsonFile, Export export) throws IOException {
    jsonFile.delete();
    jsonFile.createNewFile();

    objectMapper.writeValue(jsonFile, export);
  }

}
