package restdocs.tool.export.postman.exporter;

import org.springframework.restdocs.operation.Operation;
import restdocs.tool.export.common.exporter.JSONAbstractFileToolExporter;
import restdocs.tool.export.postman.exporter.creators.CollectionCreator;
import restdocs.tool.export.postman.exporter.creators.ItemCreator;

import java.io.File;
import java.io.IOException;

public class PostmanExporter extends JSONAbstractFileToolExporter {

  private CollectionCreator collectionCreator;
  private ItemCreator itemCreator;

  private Collection collection;

  public PostmanExporter() {
    this.collectionCreator = new CollectionCreator();
    this.itemCreator = new ItemCreator();
  }

  public PostmanExporter(CollectionCreator collectionCreator, ItemCreator itemCreator) {
    this.collectionCreator = collectionCreator;
    this.itemCreator = itemCreator;
  }

  @Override
  public void initialize(File workingDirectory, String applicationName, String toolName) throws IOException {
    super.initialize(workingDirectory, applicationName, toolName);

    if(applicationName != null) {
      Collection collection = collectionCreator.create(applicationName);
      updateExportData(collection);
      this.collection = collection;
    }
  }

  @Override
  public void processOperation(Operation operation) throws IOException {
    if(operation != null) {
      Item item = itemCreator.create(operation);
      this.collection.addItem(item);
      updateExportData(collection);
    }
  }
}
