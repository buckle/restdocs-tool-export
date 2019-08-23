package restdocs.tool.export.insomnia.export;

import org.springframework.restdocs.operation.Operation;
import restdocs.tool.export.insomnia.export.creators.RequestResourceCreator;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;
import java.util.UUID;

public class InsomniaExporter {

  public Export processOperation(Operation operation, Export export) {

    export = updateExport(export);
    Resource folderResource = getFolderResource(export);
    Resource operationResource = new RequestResourceCreator().create(operation);
    operationResource.setParentId(folderResource.getId());
    export.addResource(operationResource);

    return export;
  }

  protected Export updateExport(Export export) {
    export = export == null ? new Export() : export;
    export.setType("export");
    export.setExportFormat(4);
    export.setExportDate(LocalDateTime.now());
    export.setExportSource("spring.rest.docs.insomnia");

    return export;
  }

  protected Resource getFolderResource(Export export) {
    Set<Resource> resources = export.getResources();

    Resource folderResource = null;
    if(resources != null && !resources.isEmpty()) {
      folderResource = resources.stream()
          .filter(resourceIter -> "request_group".equals(resourceIter.getType()))
          .findFirst()
          .orElse(null);
    }

    long epochMili = LocalDateTime.now()
        .atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli();

    if(folderResource == null) {
      folderResource = new Resource();
      folderResource.setCreated(epochMili);
      folderResource.setType("request_group");
      folderResource.setName("Test Folder");
      folderResource.setId("fld_" + UUID.randomUUID().toString().replaceAll("-", ""));
      export.addResource(folderResource);
    }

    folderResource.setModified(epochMili);

    return folderResource;
  }
}
