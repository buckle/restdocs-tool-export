package restdocs.tool.export;

import org.springframework.restdocs.RestDocumentationContext;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.snippet.Snippet;
import restdocs.tool.export.common.handler.ToolHandler;
import restdocs.tool.export.common.handler.ToolHandlers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ToolExportSnippet implements Snippet {

  private static final String INIT_PROPERTY = "restdocs.tool.export.initialized";

  private static ToolExportSnippet instance;
  private List<ToolHandler> toolHandlerImpls = new ArrayList<>();
  private String applicationName;

  private ToolExportSnippet(String applicationName, ToolHandlers... toolHandlers)
      throws IllegalAccessException, InstantiationException {

    this.applicationName = applicationName;

    for(ToolHandlers toolHandler : toolHandlers) {
      toolHandlerImpls.add(toolHandler.getHandlerClass().newInstance());
    }
  }

  public static ToolExportSnippet getInstance(String applicationName, ToolHandlers... toolHandlers)
      throws InstantiationException, IllegalAccessException {

    if(instance == null) {
      instance = new ToolExportSnippet(applicationName, toolHandlers);
    }

    return instance;
  }

  protected static void resetInstance() {
    instance = null;
    System.clearProperty(INIT_PROPERTY);
  }

  @Override
  public void document(Operation operation) throws IOException {
    RestDocumentationContext context = (RestDocumentationContext) operation.getAttributes().get(RestDocumentationContext.class.getName());
    File outputDirectory = context.getOutputDirectory();
    init(outputDirectory);

    for(ToolHandler toolHandler : getToolHandlers()) {
      toolHandler.handleOperation(operation);
    }
  }

  protected void init(File outputDirectory) throws IOException {
    if(System.getProperty(INIT_PROPERTY) == null) {
      System.setProperty(INIT_PROPERTY, "true");

      for(ToolHandler toolHandler : getToolHandlers()) {
        toolHandler.initialize(outputDirectory, applicationName);
      }
    }
  }

  protected List<ToolHandler> getToolHandlers() {
    return toolHandlerImpls;
  }
}
