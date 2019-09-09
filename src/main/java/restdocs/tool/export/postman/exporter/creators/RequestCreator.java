package restdocs.tool.export.postman.exporter.creators;

import org.springframework.restdocs.operation.OperationRequest;
import restdocs.tool.export.common.creator.Creator;
import restdocs.tool.export.postman.exporter.Request;

public class RequestCreator implements Creator<Request, OperationRequest> {

  private HeadersCreator headersCreator;
  private UrlCreator urlCreator;
  private BodyCreator bodyCreator;

  public RequestCreator() {
    this.headersCreator = new HeadersCreator();
    this.urlCreator = new UrlCreator();
    this.bodyCreator = new BodyCreator();
  }

  public RequestCreator(HeadersCreator headersCreator, UrlCreator urlCreator, BodyCreator bodyCreator) {
    this.headersCreator = headersCreator;
    this.urlCreator = urlCreator;
    this.bodyCreator = bodyCreator;
  }

  @Override
  public Request create(OperationRequest operationRequest) {
    if(operationRequest != null && operationRequest.getMethod() != null) {
      Request request = new Request();

      request.setMethod(operationRequest.getMethod().toString());
      request.setHeaders(headersCreator.create(operationRequest.getHeaders()));
      request.setUrl(urlCreator.create(operationRequest));
      request.setBody(bodyCreator.create(operationRequest));

      return request;
    }

    return null;
  }
}
