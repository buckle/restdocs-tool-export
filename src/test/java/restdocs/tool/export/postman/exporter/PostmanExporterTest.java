package restdocs.tool.export.postman.exporter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.operation.OperationRequest;
import restdocs.tool.export.postman.exporter.creators.CollectionCreator;
import restdocs.tool.export.postman.exporter.creators.ItemCreator;
import restdocs.tool.export.utils.TestFileUtils;

import java.io.File;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PostmanExporterTest {

  private String appName;
  private String toolName;
  private File workingDirectory;

  @BeforeEach
  void setUp() throws Exception {
    appName = "app name " + UUID.randomUUID().toString();
    toolName = "ToolName" + UUID.randomUUID().toString().replaceAll("-", "");
    workingDirectory = TestFileUtils.creatTmpDirectory();
  }

  @Test
  void initialize() throws Exception {
    Info info = new Info();
    info.setPostmanId(UUID.randomUUID().toString());
    Collection collection = new Collection();
    collection.setInfo(info);

    CollectionCreator collectionCreator = mock(CollectionCreator.class);
    when(collectionCreator.create(appName)).thenReturn(collection);
    PostmanExporter postmanExporter = new PostmanExporter(collectionCreator, null);

    postmanExporter.initialize(workingDirectory, appName, toolName);

    Collection returnedCollection = postmanExporter.getExportData(Collection.class);

    assertNotNull(returnedCollection);
    assertEquals(collection.getInfo().getPostmanId(), returnedCollection.getInfo().getPostmanId());
  }

  @Test
  void initializeWhenAppNameNull() throws Exception {
    PostmanExporter postmanExporter = new PostmanExporter();

    postmanExporter.initialize(workingDirectory, null, toolName);

    assertNull(postmanExporter.getExportData(Collection.class));
  }

  @Test
  void processOperation() throws Exception {
    OperationRequest operationRequest = mock(OperationRequest.class);
    Operation operation = mock(Operation.class);
    when(operation.getRequest()).thenReturn(operationRequest);

    Item item = new Item();
    item.setName(UUID.randomUUID().toString());
    ItemCreator itemCreator = mock(ItemCreator.class);
    when(itemCreator.create(operation)).thenReturn(item);

    Collection collection = new Collection();
    CollectionCreator collectionCreator = mock(CollectionCreator.class);
    when(collectionCreator.create(appName)).thenReturn(collection);
    PostmanExporter postmanExporter = new PostmanExporter(collectionCreator, itemCreator);

    postmanExporter.initialize(workingDirectory, appName, toolName);
    postmanExporter.processOperation(operation);

    Collection returnedCollection = postmanExporter.getExportData(Collection.class);

    assertNotNull(returnedCollection);
    assertEquals(item.getName(), returnedCollection.getItems().iterator().next().getName());
  }

  @Test
  void processOperationWhenNull() throws Exception {
    PostmanExporter postmanExporter = new PostmanExporter();

    postmanExporter.initialize(workingDirectory, appName, toolName);
    postmanExporter.processOperation(null);

    Collection returnedCollection = postmanExporter.getExportData(Collection.class);

    assertNotNull(returnedCollection);
    assertNull(returnedCollection.getItems());
  }
}
