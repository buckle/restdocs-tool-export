package restdocs.tool.export.postman.handler;

import org.springframework.restdocs.operation.Operation;
import restdocs.tool.export.common.document.Documentor;
import restdocs.tool.export.common.document.DocumentorImpl;
import restdocs.tool.export.common.handler.ToolHandler;
import restdocs.tool.export.postman.exporter.PostmanExporter;

import java.io.File;
import java.io.IOException;

public class PostmanToolHandler implements ToolHandler {

  private PostmanExporter postmanExporter;
  private Documentor documentor;

  public PostmanToolHandler() {
    this.postmanExporter = new PostmanExporter();
    this.documentor = new DocumentorImpl();
  }

  public PostmanToolHandler(PostmanExporter postmanExporter, Documentor documentor) {
    this.postmanExporter = postmanExporter;
    this.documentor = documentor;
  }

  @Override
  public void initialize(File workingDirectory, String applicationName) throws IOException {
    postmanExporter.initialize(workingDirectory, applicationName, getToolName());
    documentor.initialize(workingDirectory, applicationName, getToolName());
  }

  @Override
  public String getToolName() {
    return "POSTMAN";
  }

  @Override
  public void handleOperation(Operation operation) throws IOException {
    postmanExporter.processOperation(operation);
    documentor.document(postmanExporter.getExportFile(), "json");
  }
}
