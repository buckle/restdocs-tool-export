package restdocs.tool.export.postman.exporter.creators;

import org.springframework.restdocs.operation.OperationRequest;
import restdocs.tool.export.common.creator.Creator;
import restdocs.tool.export.postman.exporter.Body;
import restdocs.tool.export.postman.variable.PostmanVariableHandler;

public class BodyCreator implements Creator<Body, OperationRequest> {

  PostmanVariableHandler postmanVariableHandler;

  public BodyCreator(PostmanVariableHandler postmanVariableHandler) {
    this.postmanVariableHandler = postmanVariableHandler;
  }

  public BodyCreator() {
    this.postmanVariableHandler = new PostmanVariableHandler();
  }

  @Override
  public Body create(OperationRequest request) {
    if(request != null) {
      Body body = new Body();

      body.setMode("raw");
      body.setRaw(postmanVariableHandler.replaceVariables(request.getContentAsString()));

      return body;
    }

    return null;
  }
}
