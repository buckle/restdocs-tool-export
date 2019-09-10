package restdocs.tool.export.postman.exporter.creators;

import org.springframework.restdocs.operation.OperationRequest;
import restdocs.tool.export.common.creator.Creator;
import restdocs.tool.export.postman.exporter.Url;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UrlCreator implements Creator<Url, OperationRequest> {

  private QueryParametersCreator queryParametersCreator;

  public UrlCreator(QueryParametersCreator queryParametersCreator) {
    this.queryParametersCreator = queryParametersCreator;
  }

  public UrlCreator() {
    this.queryParametersCreator = new QueryParametersCreator();
  }

  @Override
  public Url create(OperationRequest request) {
    if(request != null && request.getUri() != null) {
      Url url = new Url();

      URI uri = request.getUri();
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

      String path = uri.getPath();
      if(!"".equals(path)) {
        List<String> pathSplit = Arrays.stream(path.split("/"))
                                       .filter(s -> !"".equals(s))
                                       .collect(Collectors.toList());
        url.setPath(pathSplit);
      }

      url.setQueryParams(queryParametersCreator.create(request.getParameters()));

      return url;
    }

    return null;
  }
}
