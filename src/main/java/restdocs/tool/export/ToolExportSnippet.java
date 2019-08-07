package restdocs.tool.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.RestDocumentationContext;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class ToolExportSnippet implements Snippet {

  @Autowired private List<ToolExportHandler> exportHandlers;

  private boolean initialized = false;

  @Override
  public void document(Operation operation) throws IOException {
    RestDocumentationContext context = (RestDocumentationContext) operation.getAttributes().get(RestDocumentationContext.class.getName());
    File outputDirectory = context.getOutputDirectory();
    init(outputDirectory);

    for(ToolExportHandler exportHandler : exportHandlers) {
      exportHandler.handleOperation(operation);
      exportHandler.updateDocFile();
    }
  }

  protected void init(File outputDirectory) throws IOException {
    if(!initialized) {
      initialized = true;
      for(ToolExportHandler exportHandler : exportHandlers) {
        exportHandler.initialize(outputDirectory);
      }
    }
  }
}
