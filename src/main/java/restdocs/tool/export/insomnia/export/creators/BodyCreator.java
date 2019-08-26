package restdocs.tool.export.insomnia.export.creators;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.OperationRequest;
import restdocs.tool.export.common.creator.Creator;
import restdocs.tool.export.insomnia.export.Body;

public class BodyCreator implements Creator<Body, OperationRequest> {

  @Override
  public Body create(OperationRequest request) {
    if(request != null) {
      HttpHeaders headers = request.getHeaders();
      MediaType contentType = headers != null ? headers.getContentType() : null;

      Body body = new Body();
      body.setText(request.getContentAsString());
      body.setMimeType(contentType != null ? contentType.toString() : null);

      return body;
    }

    return null;
  }
}
