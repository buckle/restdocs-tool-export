package restdocs.tool.export;

import org.springframework.restdocs.RestDocumentationContext;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.snippet.Snippet;
import restdocs.tool.export.common.ExportProperties;
import restdocs.tool.export.common.handler.ToolHandler;
import restdocs.tool.export.common.handler.ToolHandlers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToolExportSnippet implements Snippet {

  private static final String INIT_PROPERTY = "restdocs.tool.export.initialized";

  private static ToolExportSnippet instance;
  private static Map<String, Object> properties = new HashMap<>();
  private List<ToolHandler> toolHandlerImpls = new ArrayList<>();

  private ToolExportSnippet(ToolHandlers... toolHandlers)
      throws IllegalAccessException, InstantiationException {

    for(ToolHandlers toolHandler : toolHandlers) {
      toolHandlerImpls.add(toolHandler.getHandlerClass().newInstance());
    }
  }

  public static ToolExportSnippet initInstance(ToolHandlers... toolHandlers)
      throws InstantiationException, IllegalAccessException {

    if(instance == null) {
      instance = new ToolExportSnippet(toolHandlers);
    }

    return instance;
  }

  public static ToolExportSnippet getInstance() {
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
    setAttributes(operation);

    for(ToolHandler toolHandler : getToolHandlers()) {
      toolHandler.handleOperation(operation);
    }
  }

  public static void setProperty(String propertyName, Object value) {
    properties.put(propertyName, value);
  }

  public static <T> T getProperty(String propertyName, Class<T> type) {
    return (T) properties.get(propertyName);
  }

  protected void setDefaultProperties() {
    setProperty(ExportProperties.HOST_VARIABLE_ENABLED, true);
  }

  protected void setAttributes(Operation operation) {
    Map<String, Object> attributes = operation.getAttributes();
    attributes.putAll(properties);
  }

  protected void init(File outputDirectory) throws IOException {
    if(System.getProperty(INIT_PROPERTY) == null) {
      System.setProperty(INIT_PROPERTY, "true");

      setDefaultProperties();

      for(ToolHandler toolHandler : getToolHandlers()) {
        toolHandler.initialize(outputDirectory, getProperty(ExportProperties.APPLICATION_NAME, String.class));
      }
    }
  }

  protected List<ToolHandler> getToolHandlers() {
    return toolHandlerImpls;
  }
}
