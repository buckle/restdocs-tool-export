package restdocs.tool.export.insomnia.export;

import org.springframework.restdocs.operation.Operation;
import restdocs.tool.export.insomnia.export.creators.ExportCreator;
import restdocs.tool.export.insomnia.export.creators.FolderResourceCreator;
import restdocs.tool.export.insomnia.export.creators.RequestResourceCreator;

import java.util.Set;


public class InsomniaExporter {

  public Export processOperation(Operation operation, Export export, String applicationName) {

    export = export == null ? new ExportCreator().create(operation) : export;

    Resource folderResource = findExistingFolderResource(export.getResources());
    if(folderResource == null) {
      folderResource = new FolderResourceCreator().create(applicationName);
      export.addResource(folderResource);
    }

    Resource operationResource = new RequestResourceCreator().create(operation);
    operationResource.setParentId(folderResource.getId());
    export.addResource(operationResource);

    return export;
  }

  protected Resource findExistingFolderResource(Set<Resource> resources) {
    if(resources != null) {
      return resources.stream()
                      .filter(resourceIter -> InsomniaConstants.REQUEST_GROUP_TYPE.equals(resourceIter.getType()))
                      .findFirst()
                      .orElse(null);
    }

    return null;
  }
}
