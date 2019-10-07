package restdocs.tool.export.postman.exporter.creators;

import org.springframework.restdocs.operation.Operation;
import restdocs.tool.export.common.creator.Creator;
import restdocs.tool.export.common.utils.ExportUtils;
import restdocs.tool.export.postman.exporter.Item;

public class ItemCreator implements Creator<Item, Operation> {

  private RequestCreator requestCreator;

  public ItemCreator() {
    this.requestCreator = new RequestCreator();
  }

  public ItemCreator(RequestCreator requestCreator) {
    this.requestCreator = requestCreator;
  }

  @Override
  public Item create(Operation operation) {
    if(operation != null &&
       operation.getName() != null &&
       operation.getRequest() != null) {

      Item item = new Item();
      item.setName(ExportUtils.formatNameReadably(operation.getName()));
      item.setRequest(requestCreator.create(operation.getRequest()));
      return item;
    }

    return null;
  }
}
