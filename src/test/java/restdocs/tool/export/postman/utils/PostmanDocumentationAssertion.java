package restdocs.tool.export.postman.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.springframework.util.MultiValueMap;
import restdocs.tool.export.AssertionData;
import restdocs.tool.export.application.PostData;
import restdocs.tool.export.postman.exporter.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PostmanDocumentationAssertion {

  private static final String BASE_DOC_PATH = "build/generated-snippets";
  private static final ObjectMapper objectMapper;

  static {
    objectMapper = new ObjectMapper();
  }

  public static void assertPostman(AssertionData... assertionDataArray) throws IOException {

    for(AssertionData assertionData : assertionDataArray) {
      assertExportData(assertionData);
      assertDocData(assertionData);
    }

  }

  public static void assertPostmanResourcesIncluded(String... resourceNameArray) throws IOException {

    for(String resourceName : resourceNameArray) {
      assertExportDataResourceExists(resourceName);
      assertDocDataResourceExists(resourceName);
    }

  }

  private static Item assertExportDataResourceExists(String resourceName) throws IOException {
    File exportFile = new File(BASE_DOC_PATH + "/postman-download/postman.export");
    Collection collection = objectMapper.readValue(exportFile, Collection.class);
    assertNotNull(collection);

    return findItemByName(collection.getItems(), resourceName);
  }

  private static void assertExportData(AssertionData assertionData) throws IOException {
    Item itemByName = assertExportDataResourceExists(assertionData.getResourceName());
    if (assertionData.getPostData() != null) {
      assertBody(itemByName.getRequest(), assertionData.getPostData());
    }
    assertPathVariable(itemByName.getRequest(), assertionData.getPathVariable());
    assertQueryParameter(itemByName.getRequest(), assertionData.getQueryParameter());
    assertFormParameter(itemByName.getRequest(), assertionData.getFormParameter());
    assertHeader(itemByName.getRequest(), assertionData.getHeader());
  }

  private static Item assertDocDataResourceExists(String resourceName) throws IOException {
    File docFile = new File(BASE_DOC_PATH + "/postman-download/postman.adoc");
    String sAdoc = FileUtils.readFileToString(docFile, Charset.defaultCharset());
    sAdoc = sAdoc.replace("link:++data:application/json;base64,", "");
    sAdoc = sAdoc.replace("++[Download - Right Click And Save As]", "");

    byte[] decode = Base64.getDecoder().decode(sAdoc);
    Collection collection = objectMapper.readValue(decode, Collection.class);
    assertNotNull(collection);

    return findItemByName(collection.getItems(), resourceName);
  }

  private static void assertDocData(AssertionData assertionData) throws IOException {
    Item itemByName = assertDocDataResourceExists(assertionData.getResourceName());
    assertBody(itemByName.getRequest(), assertionData.getPostData());
    assertPathVariable(itemByName.getRequest(), assertionData.getPathVariable());
    assertQueryParameter(itemByName.getRequest(), assertionData.getQueryParameter());
    assertFormParameter(itemByName.getRequest(), assertionData.getFormParameter());
    assertHeader(itemByName.getRequest(), assertionData.getHeader());
  }

  private static void assertBody(Request request, PostData postData) throws IOException {
    if (postData != null) {
      PostData documentedPostData = objectMapper.readValue(request.getBody().getRaw(), PostData.class);
      assertNotNull(documentedPostData);
      assertEquals(postData.getsField1(), documentedPostData.getsField1());
      assertEquals(postData.getiField2(), documentedPostData.getiField2());
    }
  }

  private static void assertPathVariable(Request request, String pathVariable) {
    assertNotNull(request.getUrl());
    assertNotNull(request.getUrl().getPath());
    assertTrue(request.getUrl().getPath().contains(pathVariable));
  }

  private static void assertQueryParameter(Request request, String queryParameter) {
    if (queryParameter != null) {
      assertNotNull(request.getUrl());
      assertNotNull(request.getUrl().getQuery());
      QueryParam documentedQueryParam = request.getUrl()
                                               .getQuery()
                                               .stream()
                                               .filter(queryParam -> queryParam.getValue().equals(queryParameter))
                                               .findFirst()
                                               .orElse(null);

      assertNotNull(documentedQueryParam);
    }
  }

  private static void assertFormParameter(Request request, MultiValueMap<String, String> formParameter) {
    if (formParameter != null) {
      assertEquals("form1=" + formParameter.get("form1").get(0) + "&form1=" + formParameter.get("form1").get(1) + "&form2=" + formParameter.get("form2").get(0), request.getBody().getRaw());
    }
  }

  private static void assertHeader(Request request, String header) {
    assertNotNull(request.getHeaders());
    Header documentedHeader = request.getHeaders()
                                     .stream()
                                     .filter(header1 -> header1.getValue().equals(header))
                                     .findFirst()
                                     .orElse(null);

    assertNotNull(documentedHeader);
  }

  private static Item findItemByName(Set<Item> items, String name) {
    Item foundItem = items.stream()
                          .filter(item -> item.getName().equals(name))
                          .findFirst()
                          .orElse(null);

    assertNotNull(foundItem);

    return foundItem;
  }
}
