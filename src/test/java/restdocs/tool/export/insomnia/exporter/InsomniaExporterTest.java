package restdocs.tool.export.insomnia.exporter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Sets;
import org.springframework.restdocs.operation.Operation;
import restdocs.tool.export.insomnia.exporter.creators.ExportCreator;
import restdocs.tool.export.insomnia.exporter.creators.FolderResourceCreator;
import restdocs.tool.export.insomnia.exporter.creators.RequestResourceCreator;
import restdocs.tool.export.utils.TestFileUtils;

import java.io.File;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static restdocs.tool.export.insomnia.exporter.InsomniaConstants.*;
import static restdocs.tool.export.insomnia.exporter.utils.InsomniaExportUtils.generateId;

public class InsomniaExporterTest {

  private String appName;
  private String toolName;
  private File workingDirectory;
  private Export export;
  private ExportCreator exportCreator;
  private Resource folderResource;
  private FolderResourceCreator folderResourceCreator;

  @BeforeEach
  void setUp() throws Exception {
    appName = "app name " + UUID.randomUUID().toString();
    toolName = "ToolName" + UUID.randomUUID().toString().replaceAll("-", "");
    workingDirectory = TestFileUtils.creatTmpDirectory();

    export = new Export();
    exportCreator = mock(ExportCreator.class);
    when(exportCreator.create(appName)).thenReturn(export);

    folderResource = new Resource();
    folderResource.setId(generateId(FOLDER_ID));
    folderResource.setType(REQUEST_GROUP_TYPE);
    folderResourceCreator = mock(FolderResourceCreator.class);
    when(folderResourceCreator.create(appName)).thenReturn(folderResource);
  }

  @Test
  void initialize() throws Exception {
    InsomniaExporter insomniaExporter = new InsomniaExporter(exportCreator, folderResourceCreator, null);
    insomniaExporter.initialize(workingDirectory, appName, toolName);

    Export returnedExport = insomniaExporter.getExportData(Export.class);
    assertNotNull(returnedExport);
    assertEquals(folderResource.getId(), returnedExport.getResources().iterator().next().getId());
  }

  @Test
  void initializeWhenApplicationNameNull() throws Exception {
    InsomniaExporter insomniaExporter = new InsomniaExporter();
    insomniaExporter.initialize(workingDirectory, null, toolName);

    Export returnedExport = insomniaExporter.getExportData(Export.class);

    assertNull(returnedExport);
  }

  @Test
  void processOperation() throws Exception {
    Operation operation = mock(Operation.class);

    Resource operationResource = new Resource();
    operationResource.setId(generateId(REQUEST_ID));
    operationResource.setType(REQUEST_TYPE);
    RequestResourceCreator requestResourceCreator = mock(RequestResourceCreator.class);
    when(requestResourceCreator.create(operation)).thenReturn(operationResource);

    InsomniaExporter insomniaExporter = new InsomniaExporter(exportCreator, folderResourceCreator, requestResourceCreator);
    insomniaExporter.initialize(workingDirectory, appName, toolName);
    insomniaExporter.processOperation(operation);

    Export updatedExport = insomniaExporter.getExportData(Export.class);

    assertNotNull(updatedExport);

    Resource updatedOperationResource = updatedExport.getResources()
                                                     .stream()
                                                     .filter(resource -> resource.getType().equals(REQUEST_TYPE))
                                                     .findFirst()
                                                     .orElse(null);
    assertNotNull(updatedOperationResource);

    Resource updatedFolderResource = updatedExport.getResources()
                                                  .stream()
                                                  .filter(resource -> resource.getType().equals(REQUEST_GROUP_TYPE))
                                                  .findFirst()
                                                  .orElse(null);

    assertEquals(updatedFolderResource.getId(), updatedOperationResource.getParentId());
  }

  @Test
  void processOperationWhenOperationNull() throws Exception {
    RequestResourceCreator requestResourceCreator = mock(RequestResourceCreator.class);

    InsomniaExporter insomniaExporter = new InsomniaExporter(exportCreator, folderResourceCreator, requestResourceCreator);
    insomniaExporter.initialize(workingDirectory, appName, toolName);
    insomniaExporter.processOperation(null);

    Export updatedExport = insomniaExporter.getExportData(Export.class);

    assertNotNull(updatedExport);
    verify(requestResourceCreator, never()).create(any());
  }

  @Test
  void findExistingFolderResource() {
    Resource resource1 = mock(Resource.class);
    when(resource1.getType()).thenReturn(REQUEST_TYPE);

    Resource resource2 = mock(Resource.class);
    when(resource2.getType()).thenReturn(REQUEST_GROUP_TYPE);

    Resource folderResource = new InsomniaExporter().findExistingFolderResource(Sets.newSet(resource1, resource2));

    assertNotNull(folderResource);
    assertEquals(resource2, folderResource);
  }

  @Test
  void findExistingFolderResourceWhenNoneMatch() {
    Resource resource1 = mock(Resource.class);
    when(resource1.getType()).thenReturn(REQUEST_TYPE);

    Resource resource2 = mock(Resource.class);
    when(resource2.getType()).thenReturn(REQUEST_TYPE);

    Resource folderResource = new InsomniaExporter().findExistingFolderResource(Sets.newSet(resource1, resource2));

    assertNull(folderResource);
  }

  @Test
  void findExistingFolderResourceWhenNullResources() {
    assertNull(new InsomniaExporter().findExistingFolderResource(null));
  }
}
