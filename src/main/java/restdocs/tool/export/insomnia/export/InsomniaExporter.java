package restdocs.tool.export.insomnia.export;

import org.apache.commons.lang3.StringUtils;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.operation.OperationRequest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;
import java.util.UUID;

public class InsomniaExporter {

  public Export processOperation(Operation operation, Export export) {

    export = updateExport(export);
    Resource folderResource = getFolderResource(export);
    Resource operationResource = createResourceForOperation(operation);
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

  protected Resource createResourceForOperation(Operation operation) {
    OperationRequest request = operation.getRequest();

    Body body = new Body();
    body.setText(request.getContentAsString());

    Set<Pair> headers = new HeaderCreator(request.getHeaders()).create();
    Set<Pair> parameters = new ParameterCreator(request.getParameters()).create();

    String name = operation.getName().replaceAll("[^a-zA-Z0-9]", " ");
    long epochMili = LocalDateTime.now()
        .atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli();

    Resource resource = new Resource();
    resource.setId("req_" + UUID.randomUUID().toString().replaceAll("-", ""));
    resource.setType("request");
    resource.setName(StringUtils.capitalize(name));
    resource.setMethod(request.getMethod().toString().toUpperCase());
    resource.setUrl(request.getUri().toString());
    resource.setBody(body);
    resource.setHeaders(headers);
    resource.setParameters(parameters);
    resource.setCreated(epochMili);
    resource.setModified(epochMili);

    return resource;
  }
}
