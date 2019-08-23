package restdocs.tool.export.insomnia.export;

import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Sets;
import org.springframework.restdocs.operation.Operation;
import restdocs.tool.export.insomnia.export.creators.ExportCreator;
import restdocs.tool.export.insomnia.export.creators.FolderResourceCreator;
import restdocs.tool.export.insomnia.export.creators.RequestResourceCreator;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static restdocs.tool.export.insomnia.export.InsomniaConstants.REQUEST_GROUP_TYPE;
import static restdocs.tool.export.insomnia.export.InsomniaConstants.REQUEST_TYPE;

public class InsomniaExporterTest {

  @Test
  void initialize() {
    String appName = "app name " + UUID.randomUUID().toString();
    Export export = spy(new Export());
    ExportCreator exportCreator = mock(ExportCreator.class);
    when(exportCreator.create(appName)).thenReturn(export);

    Resource folderResource = mock(Resource.class);
    FolderResourceCreator folderResourceCreator = mock(FolderResourceCreator.class);
    when(folderResourceCreator.create(appName)).thenReturn(folderResource);

    Export returnedExport = new InsomniaExporter(exportCreator, folderResourceCreator).initializeExport(appName);

    assertNotNull(returnedExport);
    assertEquals(export, returnedExport);
    assertTrue(returnedExport.getResources().contains(folderResource));
  }

  @Test
  void initializeWhenApplicationNameNull() {
    assertNull(new InsomniaExporter().initializeExport(null));
  }

  @Test
  void processOperation() {
    Resource folderResource = mock(Resource.class);
    when(folderResource.getId()).thenReturn(UUID.randomUUID().toString());
    when(folderResource.getType()).thenReturn(REQUEST_GROUP_TYPE);

    Export export = new Export();
    export.addResource(folderResource);

    Operation operation = mock(Operation.class);

    Resource operationResource = new Resource();
    RequestResourceCreator requestResourceCreator = mock(RequestResourceCreator.class);
    when(requestResourceCreator.create(operation)).thenReturn(operationResource);

    Export updatedExport = new InsomniaExporter(requestResourceCreator).processOperation(operation, export);

    assertNotNull(updatedExport);
    assertEquals(folderResource.getId(), operationResource.getParentId());
    assertTrue(updatedExport.getResources().contains(operationResource));
  }

  @Test
  void processOperationWhenOperationNull() {
    RequestResourceCreator requestResourceCreator = mock(RequestResourceCreator.class);
    Export export = new Export();

    Export updatedExport = new InsomniaExporter(requestResourceCreator).processOperation(null, export);

    assertNotNull(updatedExport);
    assertEquals(export, updatedExport);
    verify(requestResourceCreator, never()).create(any());
  }

  @Test
  void processOperationWhenExportNull() {
    RequestResourceCreator requestResourceCreator = mock(RequestResourceCreator.class);
    Operation operation = mock(Operation.class);
    Export export = null;

    Export updatedExport = new InsomniaExporter(requestResourceCreator).processOperation(operation, export);

    assertNull(updatedExport);
    assertEquals(export, updatedExport);
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
