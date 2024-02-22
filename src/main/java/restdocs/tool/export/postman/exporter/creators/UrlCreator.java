package restdocs.tool.export.postman.exporter.creators;

import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.operation.OperationRequest;
import org.springframework.restdocs.operation.QueryParameters;
import restdocs.tool.export.ToolExportSnippet;
import restdocs.tool.export.common.ExportConstants;
import restdocs.tool.export.common.creator.Creator;
import restdocs.tool.export.postman.exporter.Url;
import restdocs.tool.export.postman.variable.PostmanVariableHandler;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UrlCreator implements Creator<Url, Operation> {

  private PostmanQueryParametersCreator postmanQueryParametersCreator;
  PostmanVariableHandler postmanVariableHandler;

  public UrlCreator(PostmanQueryParametersCreator postmanQueryParametersCreator, PostmanVariableHandler postmanVariableHandler) {
    this.postmanQueryParametersCreator = postmanQueryParametersCreator;
    this.postmanVariableHandler = postmanVariableHandler;
  }

  public UrlCreator() {
    this.postmanQueryParametersCreator = new PostmanQueryParametersCreator();
    this.postmanVariableHandler = new PostmanVariableHandler();
  }

  @Override
  public Url create(Operation operation) {
    if(operation != null && operation.getRequest() != null && operation.getRequest().getUri() != null) {
      OperationRequest request = operation.getRequest();
      Url url = new Url();

      URI uri = request.getUri();

      if (operation.getAttributes().get(ExportConstants.HOST_VARIABLE_ENABLED) != null && (boolean)operation.getAttributes().get(ExportConstants.HOST_VARIABLE_ENABLED)) {
        String hostVariable = postmanVariableHandler.getHostVariable((String)operation.getAttributes().get(ExportConstants.APPLICATION_NAME));
        ToolExportSnippet.addVariable(hostVariable);

        url.setRaw(hostVariable);
        url.setHost(List.of(hostVariable));
      } else {
        String rawUri = uri.toString();
        url.setRaw("".equals(rawUri) ? null : rawUri);
        url.setProtocol(uri.getScheme());

        String host = uri.getHost();
        if(host != null) {
          List<String> hostSplit = Arrays.stream(host.split("\\."))
                                         .collect(Collectors.toList());
          url.setHost(hostSplit);
        }

        int port = uri.getPort();
        url.setPort(port == -1 ? null : port);
      }

      String path = uri.getPath();
      if(!"".equals(path)) {
        List<String> pathSplit = Arrays.stream(path.split("/"))
                                       .filter(s -> !"".equals(s))
                                       .map(s -> postmanVariableHandler.replaceVariables(s))
                                       .collect(Collectors.toList());
        url.setPath(pathSplit);
      }

      url.setQuery(postmanQueryParametersCreator.create(QueryParameters.from(request)));

      return url;
    }

    return null;
  }
}
