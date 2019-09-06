package restdocs.tool.export.postman.exporter.creators;

import org.springframework.restdocs.operation.OperationRequest;
import restdocs.tool.export.common.creator.Creator;
import restdocs.tool.export.postman.exporter.Body;

public class BodyCreator implements Creator<Body, OperationRequest> {

  @Override
  public Body create(OperationRequest request) {
    if(request != null) {
      Body body = new Body();

      body.setMode("raw");
      body.setRaw(request.getContentAsString());

      return body;
    }

    return null;
  }
}
