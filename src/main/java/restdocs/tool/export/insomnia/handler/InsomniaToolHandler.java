package restdocs.tool.export.insomnia.handler;

import org.springframework.restdocs.operation.Operation;
import restdocs.tool.export.common.document.Documentor;
import restdocs.tool.export.common.document.DocumentorImpl;
import restdocs.tool.export.common.handler.ToolHandler;
import restdocs.tool.export.insomnia.exporter.InsomniaExporter;

import java.io.File;
import java.io.IOException;

public class InsomniaToolHandler implements ToolHandler {

  private InsomniaExporter insomniaExporter;
  private Documentor documentor;

  public InsomniaToolHandler() {
    this.insomniaExporter = new InsomniaExporter();
    this.documentor = new DocumentorImpl();
  }

  public InsomniaToolHandler(InsomniaExporter insomniaExporter, Documentor documentor) {
    this.insomniaExporter = insomniaExporter;
    this.documentor = documentor;
  }

  @Override
  public void initialize(File workingDirectory, String applicationName) throws IOException {
    insomniaExporter.initialize(workingDirectory, applicationName, getToolName());
    documentor.initialize(workingDirectory, applicationName, getToolName());
  }

  @Override
  public String getToolName() {
    return "INSOMNIA";
  }

  @Override
  public void handleOperation(Operation operation) throws IOException {
    insomniaExporter.processOperation(operation);
    documentor.document(insomniaExporter.getExportFile(), "json");
  }

}
