package restdocs.tool.export.insomnia.export;

import org.springframework.restdocs.operation.Operation;
import restdocs.tool.export.insomnia.export.creators.ExportCreator;
import restdocs.tool.export.insomnia.export.creators.FolderResourceCreator;
import restdocs.tool.export.insomnia.export.creators.RequestResourceCreator;

import java.util.Set;

import static restdocs.tool.export.insomnia.export.InsomniaConstants.REQUEST_GROUP_TYPE;

public class InsomniaExporter {

  private ExportCreator exportCreator;
  private FolderResourceCreator folderResourceCreator;
  private RequestResourceCreator requestResourceCreator;

  public InsomniaExporter() {
    this.exportCreator = new ExportCreator();
    this.folderResourceCreator = new FolderResourceCreator();
    this.requestResourceCreator = new RequestResourceCreator();
  }

  public InsomniaExporter(ExportCreator exportCreator, FolderResourceCreator folderResourceCreator) {
    this.exportCreator = exportCreator;
    this.folderResourceCreator = folderResourceCreator;
  }

  public InsomniaExporter(RequestResourceCreator requestResourceCreator) {
    this.requestResourceCreator = requestResourceCreator;
  }

  public Export initializeExport(String applicationName) {
    if(applicationName != null) {
      Export export = exportCreator.create(applicationName);
      Resource folderResource = folderResourceCreator.create(applicationName);
      export.addResource(folderResource);

      return export;
    }

    return null;
  }

  public Export processOperation(Operation operation, Export export) {

    if(operation != null && export != null) {
      Resource folderResource = findExistingFolderResource(export.getResources());
      Resource operationResource = requestResourceCreator.create(operation);
      operationResource.setParentId(folderResource.getId());
      export.addResource(operationResource);
    }

    return export;
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
