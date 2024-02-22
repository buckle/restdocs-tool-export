package restdocs.tool.export.insomnia.exporter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Sets;
import org.springframework.restdocs.operation.Operation;
import restdocs.tool.export.insomnia.exporter.creators.EnvironmentResourceCreator;
import restdocs.tool.export.insomnia.exporter.creators.ExportCreator;
import restdocs.tool.export.insomnia.exporter.creators.FolderResourceCreator;
import restdocs.tool.export.insomnia.exporter.creators.RequestResourceCreator;
import restdocs.tool.export.utils.TestFileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static restdocs.tool.export.common.ExportConstants.APPLICATION_NAME;
import static restdocs.tool.export.common.ExportConstants.VARIABLES;
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

  private Resource environmentResource;

  private EnvironmentResourceCreator environmentResourceCreator;

  @BeforeEach
  void setUp() throws Exception {
    appName = "app name " + UUID.randomUUID();
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

    environmentResource = new Resource();
    environmentResource.setId(generateId(ENV_ID));
    environmentResource.setType(ENVIRONMENT_TYPE);
    environmentResourceCreator = mock(EnvironmentResourceCreator.class);
    when(environmentResourceCreator.create(any())).thenReturn(environmentResource);
  }

  @Test
  void initialize() throws Exception {
    InsomniaExporter insomniaExporter = new InsomniaExporter(exportCreator, folderResourceCreator, null, null);
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
    when(operation.getAttributes()).thenReturn(Map.of(VARIABLES, Set.of("testingVariable")));

    Resource operationResource = new Resource();
    operationResource.setId(generateId(REQUEST_ID));
    operationResource.setType(REQUEST_TYPE);
    RequestResourceCreator requestResourceCreator = mock(RequestResourceCreator.class);
    when(requestResourceCreator.create(operation)).thenReturn(operationResource);

    InsomniaExporter insomniaExporter = new InsomniaExporter(exportCreator, folderResourceCreator, requestResourceCreator, environmentResourceCreator);
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

    Resource updatedEnvironmentResource = updatedExport.getResources()
        .stream().filter(resource -> resource.getType().equals(ENVIRONMENT_TYPE))
        .findFirst().orElse(null);

    assertNotNull(updatedEnvironmentResource);
    assertTrue(updatedEnvironmentResource.getData().containsKey("testingVariable"));
  }

  @Test
  void processOperationWhenOperationNull() throws Exception {
    RequestResourceCreator requestResourceCreator = mock(RequestResourceCreator.class);

    InsomniaExporter insomniaExporter = new InsomniaExporter(exportCreator, folderResourceCreator, requestResourceCreator, environmentResourceCreator);
    insomniaExporter.initialize(workingDirectory, appName, toolName);
    insomniaExporter.processOperation(null);

    Export updatedExport = insomniaExporter.getExportData(Export.class);

    assertNotNull(updatedExport);
    verify(requestResourceCreator, never()).create(any());
    assertTrue(updatedExport.getResources().stream().noneMatch(s -> s.getType().equals(ENVIRONMENT_TYPE)));
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

  @Test
  void findExistingEnvironmentResource() {
    Resource resource1 = mock(Resource.class);
    when(resource1.getType()).thenReturn(REQUEST_TYPE);

    Resource resource2 = mock(Resource.class);
    when(resource2.getType()).thenReturn(ENVIRONMENT_TYPE);

    Resource environmentResource = new InsomniaExporter().findExistingEnvironmentResource(Sets.newSet(resource1, resource2));

    assertNotNull(environmentResource);
    assertEquals(resource2, environmentResource);
  }

  @Test
  void findExistingEnvironmentResourceWhenNoneMatch() {
    Resource resource1 = mock(Resource.class);
    when(resource1.getType()).thenReturn(REQUEST_TYPE);

    Resource resource2 = mock(Resource.class);
    when(resource2.getType()).thenReturn(REQUEST_TYPE);

    Resource environmentResource = new InsomniaExporter().findExistingEnvironmentResource(Sets.newSet(resource1, resource2));

    assertNull(environmentResource);
  }
  @Test
  void findExistingEnvironmentResourceWhenNullResources() {
    assertNull(new InsomniaExporter().findExistingEnvironmentResource(null));
  }

  @Test
  void findOrCreateEnvironmentResourceWhenExists() throws IOException {
    InsomniaExporter insomniaExporter = spy(new InsomniaExporter(exportCreator, folderResourceCreator, null, environmentResourceCreator));
    insomniaExporter.initialize(workingDirectory, appName, toolName);
    export.getResources().add(environmentResource);
    assertEquals(1, export.getResources().stream().filter(s -> s.getType().equals(ENVIRONMENT_TYPE)).count());
    Resource environmentResource = insomniaExporter.findOrCreateEnvironmentResource(appName);
    assertNotNull(environmentResource);
    verify(insomniaExporter).findExistingEnvironmentResource(anySet());
    verify(environmentResourceCreator, never()).create(appName);
    assertEquals(1, export.getResources().stream().filter(s -> s.getType().equals(ENVIRONMENT_TYPE)).count());
  }

  @Test
  void findOrCreateEnvironmentResourceWhenDoesNotExist() throws IOException {
    InsomniaExporter insomniaExporter = spy(new InsomniaExporter(exportCreator, folderResourceCreator, null, environmentResourceCreator));
    insomniaExporter.initialize(workingDirectory, appName, toolName);
    assertEquals(0, export.getResources().stream().filter(s -> s.getType().equals(ENVIRONMENT_TYPE)).count());
    when(insomniaExporter.findExistingEnvironmentResource(anySet())).thenReturn(null);
    Resource environmentResource = insomniaExporter.findOrCreateEnvironmentResource(appName);
    assertNotNull(environmentResource);
    verify(environmentResourceCreator).create(appName);
    assertEquals(1, export.getResources().stream().filter(s -> s.getType().equals(ENVIRONMENT_TYPE)).count());
  }

  @Test
  void updateEnvironmentResourceVariablesNullOperation() {
    InsomniaExporter insomniaExporter = spy(new InsomniaExporter());
    insomniaExporter.updateEnvironmentResourceVariables(null);
    verify(insomniaExporter, never()).findOrCreateEnvironmentResource(anyString());
  }
  @Test
  void updateEnvironmentResourceVariablesNullAttributes() {
    Operation operation = mock(Operation.class);
    when(operation.getAttributes()).thenReturn(null);

    InsomniaExporter insomniaExporter = spy(new InsomniaExporter());
    insomniaExporter.updateEnvironmentResourceVariables(operation);
    verify(insomniaExporter, never()).findOrCreateEnvironmentResource(anyString());
  }

  @Test
  void updateEnvironmentResourceVariablesNullVariablesAttribute() {
    Operation operation = mock(Operation.class);
    when(operation.getAttributes()).thenReturn(new HashMap<>());

    InsomniaExporter insomniaExporter = spy(new InsomniaExporter());
    insomniaExporter.updateEnvironmentResourceVariables(operation);
    verify(insomniaExporter, never()).findOrCreateEnvironmentResource(anyString());
  }

  @Test
  void updateEnvironmentResourceVariablesEmptyVariablesAttribute() {
    Operation operation = mock(Operation.class);
    when(operation.getAttributes()).thenReturn(Map.of(VARIABLES, new HashSet<>()));

    InsomniaExporter insomniaExporter = spy(new InsomniaExporter());
    insomniaExporter.updateEnvironmentResourceVariables(operation);
    verify(insomniaExporter, never()).findOrCreateEnvironmentResource(anyString());
  }

  @Test
  void updateEnvironmentResourceVariablesWhenHasVariables() {
    Operation operation = mock(Operation.class);
    String appName = "appName";
    when(operation.getAttributes()).thenReturn(Map.of(VARIABLES, Set.of("testVariable"), APPLICATION_NAME, "appName"));

    InsomniaExporter insomniaExporter = spy(new InsomniaExporter());
    Resource resource = mock(Resource.class);
    doReturn(resource).when(insomniaExporter).findOrCreateEnvironmentResource(anyString());
    insomniaExporter.updateEnvironmentResourceVariables(operation);
    verify(insomniaExporter).findOrCreateEnvironmentResource(appName);
    verify(resource).setData(anyMap());
  }
}
