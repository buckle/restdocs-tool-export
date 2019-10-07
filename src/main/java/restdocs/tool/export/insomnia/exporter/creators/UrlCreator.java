package restdocs.tool.export.insomnia.exporter.creators;

import org.springframework.restdocs.operation.Operation;
import restdocs.tool.export.common.ExportProperties;
import restdocs.tool.export.common.creator.Creator;
import restdocs.tool.export.insomnia.exporter.variable.InsomniaVariableHandler;

import java.net.URI;
import java.util.Map;

public class UrlCreator implements Creator<String, Operation> {

  private InsomniaVariableHandler insomniaVariableHandler;

  public UrlCreator(InsomniaVariableHandler insomniaVariableHandler) {
    this.insomniaVariableHandler = insomniaVariableHandler;
  }

  public UrlCreator() {
    this.insomniaVariableHandler = new InsomniaVariableHandler();
  }

  @Override
  public String create(Operation operation) {
    if(operation != null &&
       operation.getRequest() != null &&
       operation.getAttributes() != null &&
       operation.getRequest().getUri() != null) {

      URI uri = operation.getRequest().getUri();

      Map<String, Object> attributes = operation.getAttributes();
      String applicationName = (String) attributes.get(ExportProperties.APPLICATION_NAME);
      String hostVariable = insomniaVariableHandler.getHostVariable(applicationName);

      return hostVariable + uri.getPath();
    }

    return null;
  }
}
