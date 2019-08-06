package restdocs.tool.export;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.RestDocumentationContext;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.operation.OperationRequest;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.stereotype.Component;
import restdocs.tool.export.insomnia.Body;
import restdocs.tool.export.insomnia.Export;
import restdocs.tool.export.insomnia.Header;
import restdocs.tool.export.insomnia.Resource;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class InsomniaSnippet implements Snippet {

  private ObjectMapper objectMapper;
  private boolean firstWrite;

  public InsomniaSnippet() {
    this.objectMapper = new ObjectMapper();
    this.objectMapper.registerModule(new JavaTimeModule());
    this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    this.firstWrite = true;
  }

  @Override
  public void document(Operation operation) throws IOException {
    RestDocumentationContext context = (RestDocumentationContext) operation.getAttributes().get(RestDocumentationContext.class.getName());

    File workingDirectory = getOrCreateDirectory(context.getOutputDirectory());
    File jsonFile = getJSONFile(workingDirectory);

    Export export = null;
    if(jsonFile.length() > 0) {
      export = objectMapper.readValue(jsonFile, Export.class);
    }
    export = initOrUpdateExport(export);

    Resource folderResource = getOrCreateFolderResource(export);
    Resource operationResource = createResourceForOperation(operation);
    operationResource.setParentId(folderResource.getId());
    export.addResource(operationResource);
    updateJSONFile(jsonFile, export);
    File aDocFile = getADocFile(workingDirectory);
    updateADockFile(aDocFile, jsonFile);

  }

  protected File getOrCreateDirectory(File outputDirectory) throws IOException {

    File file = new File(outputDirectory.getAbsolutePath() + "/insomnia-download");

    if(!file.exists()) {
      file.mkdir();
    }

    return file;
  }

  protected File getJSONFile(File workingDirectory) throws IOException {

    File file = new File(workingDirectory.getAbsolutePath() + "/insomnia.json");

    if(!file.exists()) {
      file.createNewFile();
    }

    if(firstWrite) {
      file.delete();
      file.createNewFile();
      firstWrite = false;
    }

    return file;
  }

  protected void updateJSONFile(File jsonFile, Export export) throws IOException {
    jsonFile.delete();
    jsonFile.createNewFile();

    objectMapper.writeValue(jsonFile, export);
  }

  protected File getADocFile(File workingDirectory) throws IOException {
    File file = new File(workingDirectory.getAbsolutePath() + "/insomnia.adoc");

    if(!file.exists()) {
      file.createNewFile();
    } else {
      file.delete();
      file.createNewFile();
    }

    return file;
  }

  protected void updateADockFile(File aDockFile, File jsonExport) throws IOException {

    StringBuilder stringBuilder = new StringBuilder();

    stringBuilder.append("link:++data:application/json;base64,");
    stringBuilder.append(Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(jsonExport)));
    stringBuilder.append("++[Download - Right Click And Save As]");
    FileUtils.writeByteArrayToFile(aDockFile, stringBuilder.toString().getBytes(), false);
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
