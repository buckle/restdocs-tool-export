package restdocs.tool.export.postman.exporter.creators;

import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.operation.OperationRequest;
import restdocs.tool.export.common.creator.Creator;
import restdocs.tool.export.postman.exporter.Request;

public class RequestCreator implements Creator<Request, Operation> {

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
  public Request create(Operation operation) {
    if(operation != null && operation.getRequest() != null && operation.getRequest().getMethod() != null) {
      OperationRequest operationRequest = operation.getRequest();
      Request request = new Request();

      request.setMethod(operationRequest.getMethod().toString());
      request.setHeaders(headersCreator.create(operationRequest.getHeaders()));
      request.setUrl(urlCreator.create(operation));
      request.setBody(bodyCreator.create(operationRequest));

      return request;
    }

    return null;
  }
}
