package restdocs.tool.export.insomnia.export.creators;

import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.operation.OperationRequest;
import restdocs.tool.export.Creator;
import restdocs.tool.export.insomnia.export.Body;
import restdocs.tool.export.insomnia.export.Resource;

import static restdocs.tool.export.insomnia.export.InsomniaConstants.REQUEST_ID;
import static restdocs.tool.export.insomnia.export.InsomniaConstants.REQUEST_TYPE;
import static restdocs.tool.export.insomnia.export.utils.InsomniaExportUtils.*;

public class RequestResourceCreator implements Creator<Resource, Operation> {

  private HeadersCreator headersCreator;
  private ParametersCreator parametersCreator;

  public RequestResourceCreator(HeadersCreator headersCreator, ParametersCreator parametersCreator) {
    this.headersCreator = headersCreator;
    this.parametersCreator = parametersCreator;
  }

  public RequestResourceCreator() {
    this.headersCreator = new HeadersCreator();
    this.parametersCreator = new ParametersCreator();
  }

  @Override
  public Resource create(Operation operation) {
    Resource resource = new Resource();

    OperationRequest request = operation.getRequest();

    resource.setId(generateId(REQUEST_ID));
    resource.setType(REQUEST_TYPE);
    resource.setName(formatName(operation.getName()));
    resource.setUrl(request.getUri().toString());
    resource.setMethod(request.getMethod().toString());
    resource.setHeaders(headersCreator.create(request.getHeaders()));
    resource.setParameters(parametersCreator.create(request.getParameters()));

    Body body = new Body();
    body.setText(request.getContentAsString());
    resource.setBody(body);

    resource.setCreated(getEpochMillis());
    resource.setModified(getEpochMillis());

    return resource;
  }
}
