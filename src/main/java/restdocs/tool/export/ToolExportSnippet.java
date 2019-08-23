package restdocs.tool.export;

import org.springframework.restdocs.RestDocumentationContext;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.snippet.Snippet;
import restdocs.tool.export.insomnia.handler.InsomniaToolExportHandler;

import java.io.File;
import java.io.IOException;

public class ToolExportSnippet implements Snippet {

  private static ToolExportSnippet toolExportSnippet;
  private InsomniaToolExportHandler insomniaToolExportHandler;
  private String applicationName;

  private ToolExportSnippet(String applicationName) {
    this.applicationName = applicationName;
  }

  public static ToolExportSnippet get(String applicationName) {
    if(toolExportSnippet == null) {
      toolExportSnippet = new ToolExportSnippet(applicationName);
    }

    return toolExportSnippet;
  }

  @Override
  public void document(Operation operation) throws IOException {
    RestDocumentationContext context = (RestDocumentationContext) operation.getAttributes().get(RestDocumentationContext.class.getName());
    File outputDirectory = context.getOutputDirectory();
    init(outputDirectory);

    insomniaToolExportHandler.handleOperation(operation, applicationName);
  }

  protected void init(File outputDirectory) throws IOException {
    if(System.getProperty("restdocs.tool.export.initialized") == null) {
      System.setProperty("restdocs.tool.export.initialized", "true");

      insomniaToolExportHandler = new InsomniaToolExportHandler();
      insomniaToolExportHandler.initialize(outputDirectory);
    }
  }
}
