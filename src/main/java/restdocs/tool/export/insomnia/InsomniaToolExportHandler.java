package restdocs.tool.export.insomnia;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.operation.OperationRequest;
import org.springframework.stereotype.Component;
import restdocs.tool.export.AbstractFileToolExportHandler;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class InsomniaToolExportHandler extends AbstractFileToolExportHandler {

  private ObjectMapper objectMapper;

  @Override
  public void initialize(File workingDirectory) throws IOException {
    super.initialize(workingDirectory);
    this.objectMapper = new ObjectMapper();
    this.objectMapper.registerModule(new JavaTimeModule());
    this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
  }

  @Override
  public String getToolName() {
    return "INSOMNIA";
  }

  @Override
  public void handleOperation(Operation operation) throws IOException {
    Export export = null;
    if(super.exportFile.length() > 0) {
      export = objectMapper.readValue(super.exportFile, Export.class);
    }
    export = initOrUpdateExport(export);

    Resource folderResource = getOrCreateFolderResource(export);
    Resource operationResource = createResourceForOperation(operation);
    operationResource.setParentId(folderResource.getId());
    export.addResource(operationResource);
    updateJSONFile(super.exportFile, export);
  }

  protected void updateJSONFile(File jsonFile, Export export) throws IOException {
    jsonFile.delete();
    jsonFile.createNewFile();

    objectMapper.writeValue(jsonFile, export);
  }

  protected Export initOrUpdateExport(Export export) {
    export = export == null ? new Export() : export;
    export.setType("export");
    export.setExportFormat(4);
    export.setExportDate(LocalDateTime.now());
    export.setExportSource("spring.rest.docs.insomnia");

    return export;
  }

  protected Resource getOrCreateFolderResource(Export export) {
    List<Resource> resources = export.getResources();

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

    HttpHeaders headers = request.getHeaders();
    List<Header> docHeaders = new ArrayList<>();

    for(Map.Entry<String, List<String>> stringListEntry : headers.entrySet()) {
      String key = stringListEntry.getKey();
      List<String> values = stringListEntry.getValue();

      Header header = new Header();
      header.setId("pair_" + UUID.randomUUID().toString().replaceAll("-", ""));
      header.setName(key);
      header.setValue(StringUtils.join(values, ","));

      docHeaders.add(header);
    }

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
    resource.setHeaders(docHeaders);
    resource.setCreated(epochMili);
    resource.setModified(epochMili);

    return resource;
  }

}
