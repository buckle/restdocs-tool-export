package restdocs.tool.export.insomnia.exporter.creators;

import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.operation.OperationRequest;
import restdocs.tool.export.common.creator.Creator;
import restdocs.tool.export.insomnia.exporter.Resource;

import static restdocs.tool.export.common.utils.ExportUtils.formatNameReadably;
import static restdocs.tool.export.insomnia.exporter.InsomniaConstants.REQUEST_ID;
import static restdocs.tool.export.insomnia.exporter.InsomniaConstants.REQUEST_TYPE;
import static restdocs.tool.export.insomnia.exporter.utils.InsomniaExportUtils.generateId;
import static restdocs.tool.export.insomnia.exporter.utils.InsomniaExportUtils.getEpochMillis;

public class RequestResourceCreator implements Creator<Resource, Operation> {

  private UrlCreator urlCreator;
  private HeadersCreator headersCreator;
  private ParametersCreator parametersCreator;
  private BodyCreator bodyCreator;

  public RequestResourceCreator(UrlCreator urlCreator,
                                HeadersCreator headersCreator,
                                ParametersCreator parametersCreator,
                                BodyCreator bodyCreator) {

    this.urlCreator = urlCreator;
    this.headersCreator = headersCreator;
    this.parametersCreator = parametersCreator;
    this.bodyCreator = bodyCreator;
  }

  public RequestResourceCreator() {
    this.urlCreator = new UrlCreator();
    this.headersCreator = new HeadersCreator();
    this.parametersCreator = new ParametersCreator();
    this.bodyCreator = new BodyCreator();
  }

  @Override
  public Resource create(Operation operation) {
    if(operation != null && operation.getRequest() != null) {

      OperationRequest request = operation.getRequest();

      Resource resource = new Resource();
      resource.setId(generateId(REQUEST_ID));
      resource.setType(REQUEST_TYPE);
      resource.setName(formatNameReadably(operation.getName()));
      resource.setUrl(urlCreator.create(operation));
      resource.setMethod(request.getMethod() != null ? request.getMethod().toString() : null);
      resource.setHeaders(headersCreator.create(request.getHeaders()));
      resource.setParameters(parametersCreator.create(request.getParameters()));
      resource.setBody(bodyCreator.create(request));
      resource.setCreated(getEpochMillis());
      resource.setModified(getEpochMillis());

      return resource;
    }

    return null;
  }
}
