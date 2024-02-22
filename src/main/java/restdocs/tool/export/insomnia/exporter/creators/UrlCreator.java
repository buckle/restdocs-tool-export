package restdocs.tool.export.insomnia.exporter.creators;

import org.springframework.restdocs.operation.Operation;
import org.springframework.web.util.UriComponentsBuilder;
import restdocs.tool.export.ToolExportSnippet;
import restdocs.tool.export.common.ExportConstants;
import restdocs.tool.export.common.creator.Creator;
import restdocs.tool.export.insomnia.exporter.variable.InsomniaVariableHandler;

import java.net.URI;
import java.net.URLDecoder;
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
      if (attributes.get(ExportConstants.HOST_VARIABLE_ENABLED) != null && (boolean)attributes.get(ExportConstants.HOST_VARIABLE_ENABLED)) {
        String applicationName = (String) attributes.get(ExportConstants.APPLICATION_NAME);
        String hostVariable = insomniaVariableHandler.getHostVariable(applicationName);
        ToolExportSnippet.addVariable(hostVariable);

        return hostVariable + insomniaVariableHandler.replaceVariables(uri.getPath());
      } else {
        return insomniaVariableHandler.replaceVariables(
            URLDecoder.decode(
                UriComponentsBuilder.fromUri(operation.getRequest().getUri()).replaceQuery(null).build().toString()
            )
        );
      }
    }

    return null;
  }
}
