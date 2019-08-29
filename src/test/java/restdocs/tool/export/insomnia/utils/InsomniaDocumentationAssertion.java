package restdocs.tool.export.insomnia.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.io.FileUtils;
import restdocs.tool.export.AssertionData;
import restdocs.tool.export.application.PostData;
import restdocs.tool.export.insomnia.exporter.Export;
import restdocs.tool.export.insomnia.exporter.Pair;
import restdocs.tool.export.insomnia.exporter.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class InsomniaDocumentationAssertion {

  private static final String BASE_DOC_PATH = "build/generated-snippets";
  private static final ObjectMapper objectMapper;

  static {
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
  }

  public static void assertInsomnia(AssertionData... assertionDataArray) throws IOException {

    for(AssertionData assertionData : assertionDataArray) {
      assertExportData(assertionData);
      assertDocData(assertionData);
    }
  }

  private static void assertExportData(AssertionData assertionData) throws IOException {

    File exportFile = new File(BASE_DOC_PATH + "/insomnia-download/insomnia.export");
    Export export = objectMapper.readValue(exportFile, Export.class);
    assertNotNull(export);

    Resource postTestResource = findResourceForName(export, assertionData.getResourceName());
    assertBody(postTestResource, assertionData.getPostData());
    assertPathVariable(postTestResource, assertionData.getPathVariable());
    assertParameter(postTestResource, assertionData.getParameter());
    assertHeader(postTestResource, assertionData.getHeader());
  }

  private static void assertDocData(AssertionData assertionData) throws IOException {

    File docFile = new File(BASE_DOC_PATH + "/insomnia-download/insomnia.adoc");
    String sAdoc = FileUtils.readFileToString(docFile, Charset.defaultCharset());
    sAdoc = sAdoc.replace("link:++data:application/json;base64,", ""); // So elegant
    sAdoc = sAdoc.replace("++[Download - Right Click And Save As]", "");

    byte[] decode = Base64.getDecoder().decode(sAdoc);
    Export export = objectMapper.readValue(decode, Export.class);
    assertNotNull(export);

    Resource postTestResource = findResourceForName(export, assertionData.getResourceName());
    assertBody(postTestResource, assertionData.getPostData());
    assertPathVariable(postTestResource, assertionData.getPathVariable());
    assertParameter(postTestResource, assertionData.getParameter());
    assertHeader(postTestResource, assertionData.getHeader());
  }

  private static void assertBody(Resource testResource, PostData postData) throws IOException {
    PostData documentedPostData = objectMapper.readValue(testResource.getBody().getText(), PostData.class);
    assertNotNull(documentedPostData);
    assertEquals(postData.getsField1(), documentedPostData.getsField1());
    assertEquals(postData.getiField2(), documentedPostData.getiField2());
  }

  private static void assertPathVariable(Resource testResource, String pathVariable) {
    String documentedUrl = testResource.getUrl();
    assertNotNull(documentedUrl);
    assertTrue(documentedUrl.contains(pathVariable));
  }

  private static void assertParameter(Resource testResource, String parameter) {
    Set<Pair> documentedParameters = testResource.getParameters();
    assertNotNull(documentedParameters);
    Pair documentedParameter = documentedParameters.stream()
                                                   .filter(pair -> pair.getValue().equals(parameter))
                                                   .findFirst()
                                                   .orElse(null);
    assertNotNull(documentedParameter);
  }

  private static void assertHeader(Resource testResource, String header) {
    Set<Pair> documentedHeaders = testResource.getHeaders();
    assertNotNull(documentedHeaders);
    Pair documentedHeader = documentedHeaders.stream()
                                             .filter(pair -> pair.getValue().equals(header))
                                             .findFirst()
                                             .orElse(null);
    assertNotNull(documentedHeader);
  }

  private static Resource findResourceForName(Export export, String testName) {
    Resource postTestResource = export.getResources()
                 .stream()
                 .filter(resource -> resource.getName().equals(testName))
                 .findFirst()
                 .orElse(null);

    assertNotNull(postTestResource);

    return postTestResource;
  }
}
