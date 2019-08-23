package restdocs.tool.export.insomnia.handler;

import org.springframework.restdocs.operation.Operation;
import restdocs.tool.export.JSONAbstractFileToolExportHandler;
import restdocs.tool.export.insomnia.export.Export;
import restdocs.tool.export.insomnia.export.InsomniaExporter;

import java.io.File;
import java.io.IOException;

public class InsomniaToolExportHandler extends JSONAbstractFileToolExportHandler {

  private InsomniaExporter insomniaExporter;

  @Override
  public void initialize(File workingDirectory) throws IOException {
    super.initialize(workingDirectory);
    this.insomniaExporter =  new InsomniaExporter();
  }

  @Override
  public String getToolName() {
    return "INSOMNIA";
  }

  @Override
  public void handleOperation(Operation operation, String applicationName) throws IOException {
    Export export = getExport(super.exportFile, Export.class);
    export = insomniaExporter.processOperation(operation, export, applicationName);
    updateJSONFile(super.exportFile, export);
  }

}
