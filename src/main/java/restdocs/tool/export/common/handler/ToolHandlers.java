package restdocs.tool.export.common.handler;

import restdocs.tool.export.insomnia.handler.InsomniaToolHandler;
import restdocs.tool.export.postman.handler.PostmanToolHandler;

public enum ToolHandlers {

  INSOMNIA(InsomniaToolHandler.class),
  POSTMAN(PostmanToolHandler.class);

  private Class<? extends ToolHandler> handlerClass;

  ToolHandlers(Class<? extends ToolHandler> handlerClass) {
    this.handlerClass = handlerClass;
  }

  public Class<? extends ToolHandler> getHandlerClass() {
    return handlerClass;
  }
}
