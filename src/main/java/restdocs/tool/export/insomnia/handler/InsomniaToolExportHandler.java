package restdocs.tool.export.insomnia.handler;

import org.springframework.restdocs.operation.Operation;
import restdocs.tool.export.common.handler.JSONAbstractFileToolExportHandler;
import restdocs.tool.export.insomnia.export.Export;
import restdocs.tool.export.insomnia.export.InsomniaExporter;

import java.io.File;
import java.io.IOException;

public class InsomniaToolExportHandler extends JSONAbstractFileToolExportHandler {

  private InsomniaExporter insomniaExporter;

  @Override
  public void initialize(File workingDirectory, String applicationName) throws IOException {
    super.initialize(workingDirectory, applicationName);
    this.insomniaExporter = new InsomniaExporter();

    Export export = insomniaExporter.initializeExport(applicationName);
    updateFileData(export);
  }

  @Override
  public String getToolName() {
    return "INSOMNIA";
  }

  @Override
  public void handleOperation(Operation operation) throws IOException {
    Export export = getFileData(Export.class);
    export = insomniaExporter.processOperation(operation, export);
    updateFileData(export);
  }

}
