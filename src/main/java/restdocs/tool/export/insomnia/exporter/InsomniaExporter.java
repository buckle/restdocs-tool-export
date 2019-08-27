package restdocs.tool.export.insomnia.exporter;

import org.springframework.restdocs.operation.Operation;
import restdocs.tool.export.common.exporter.JSONAbstractFileToolExporter;
import restdocs.tool.export.insomnia.exporter.creators.ExportCreator;
import restdocs.tool.export.insomnia.exporter.creators.FolderResourceCreator;
import restdocs.tool.export.insomnia.exporter.creators.RequestResourceCreator;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import static restdocs.tool.export.insomnia.exporter.InsomniaConstants.REQUEST_GROUP_TYPE;

public class InsomniaExporter extends JSONAbstractFileToolExporter {

  private ExportCreator exportCreator;
  private FolderResourceCreator folderResourceCreator;
  private RequestResourceCreator requestResourceCreator;

  private Export export;

  public InsomniaExporter() {
    this.exportCreator = new ExportCreator();
    this.folderResourceCreator = new FolderResourceCreator();
    this.requestResourceCreator = new RequestResourceCreator();
  }

  public InsomniaExporter(ExportCreator exportCreator, FolderResourceCreator folderResourceCreator, RequestResourceCreator requestResourceCreator) {
    this.exportCreator = exportCreator;
    this.folderResourceCreator = folderResourceCreator;
    this.requestResourceCreator = requestResourceCreator;
  }

  @Override
  public void initialize(File workingDirectory, String applicationName, String toolName) throws IOException {
    super.initialize(workingDirectory, applicationName, toolName);

    if(applicationName != null) {
      Export export = exportCreator.create(applicationName);
      Resource folderResource = folderResourceCreator.create(applicationName);
      export.addResource(folderResource);
      updateExportData(export);
      this.export = export;
    }
  }

  @Override
  public void processOperation(Operation operation) throws IOException {
    if(operation != null) {
      Resource folderResource = findExistingFolderResource(export.getResources());
      Resource operationResource = requestResourceCreator.create(operation);
      operationResource.setParentId(folderResource.getId());
      export.addResource(operationResource);
      updateExportData(export);
    }
  }

  protected Resource findExistingFolderResource(Set<Resource> resources) {
    if(resources != null) {
      return resources.stream()
                      .filter(resourceIter -> REQUEST_GROUP_TYPE.equals(resourceIter.getType()))
                      .findFirst()
                      .orElse(null);
    }

    return null;
  }
}
