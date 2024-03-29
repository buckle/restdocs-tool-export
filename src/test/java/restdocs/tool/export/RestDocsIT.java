package restdocs.tool.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import restdocs.tool.export.application.PostData;
import restdocs.tool.export.application.TestApplication;
import restdocs.tool.export.common.handler.ToolHandlers;
import restdocs.tool.export.insomnia.exporter.variable.InsomniaVariableHandler;
import restdocs.tool.export.postman.variable.PostmanVariableHandler;
import restdocs.tool.export.utils.DocumentationUtil;
import restdocs.tool.export.utils.ITTestUtils;

import java.io.IOException;
import java.util.UUID;

import static org.springframework.restdocs.cli.CliDocumentation.curlRequest;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyHeaders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static restdocs.tool.export.CrossTestData.*;
import static restdocs.tool.export.common.ExportConstants.HOST_VARIABLE_ENABLED;
import static restdocs.tool.export.common.variable.HeaderVariablePreprocessor.replaceHeaderValueWithVariable;
import static restdocs.tool.export.insomnia.utils.InsomniaDocumentationAssertion.assertInsomnia;
import static restdocs.tool.export.insomnia.utils.InsomniaDocumentationAssertion.assertInsomniaResourcesIncluded;
import static restdocs.tool.export.postman.utils.PostmanDocumentationAssertion.assertPostman;
import static restdocs.tool.export.postman.utils.PostmanDocumentationAssertion.assertPostmanResourcesIncluded;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {"spring.application.name=test_application"}, classes = TestApplication.class)
@AutoConfigureRestDocs
@AutoConfigureMockMvc
public class RestDocsIT {

  @Autowired protected ObjectMapper objectMapper;
  @Autowired protected MockMvc mockMvc;
  @Value("${spring.application.name}") protected String appName;
  @Autowired protected WebApplicationContext context;
  @Autowired protected RestDocumentationContextProvider restDocumentation;

  static {
    ITTestUtils.cleanSnippetsDir();
  }

  @BeforeEach
  void setUp() throws Exception {
    ToolExportSnippet.initInstance(appName, ToolHandlers.INSOMNIA, ToolHandlers.POSTMAN);
    ToolExportSnippet.setProperty(HOST_VARIABLE_ENABLED, true);
    mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                             .apply(documentationConfiguration(this.restDocumentation)
                                        .operationPreprocessors().withRequestDefaults(
                                            modifyHeaders().add("x-api-key", "anothersecret"),
                                            replaceHeaderValueWithVariable("x-api-key")
                                 )
                                        .and()
                                        .snippets().withDefaults(curlRequest()))
                             .build();
  }

  @Test
  public void postTest() throws Exception {
    mockMvc.perform(post("/post/test/{pathVariable}?param1={queryParam}", pathVariable, queryParam)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Key", headerXKey)
                        .content(objectMapper.writeValueAsBytes(postData))
                        .secure(true))
           .andExpect(status().isOk())
           .andDo(DocumentationUtil.documentEcho("post-test"));

    AssertionData assertionData = new AssertionData(postData, pathVariable, queryParam, null, headerXKey, "Post Test");
    assertInsomnia(assertionData);
    assertPostman(assertionData);
  }

  @Test
  public void postTestNoHostVariable() throws Exception {
    ToolExportSnippet.setProperty(HOST_VARIABLE_ENABLED, false);
    mockMvc.perform(post("/post/test/{pathVariable}?param1={queryParam}", pathVariable, queryParam)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Key", headerXKey)
                        .content(objectMapper.writeValueAsBytes(postData))
                        .secure(true))
           .andExpect(status().isOk())
           .andDo(DocumentationUtil.documentEcho("post-test-no-host-variable"));

    AssertionData assertionData = new AssertionData(postData, pathVariable, queryParam, null, headerXKey, "Post Test No Host Variable");
    assertInsomnia(assertionData);
    assertPostman(assertionData);
  }

  @Test
  public void postTest2() throws Exception {
    PostData postData = new PostData();
    postData.setsField1(UUID.randomUUID().toString());
    postData.setiField2(RandomUtils.nextInt());

    String pathVariable = UUID.randomUUID().toString();
    String queryParameter = UUID.randomUUID().toString();
    String headerXKey = UUID.randomUUID().toString();

    mockMvc.perform(post("/post/test/{pathVariable}", pathVariable)
                        .queryParam("param1", queryParameter)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Key", headerXKey)
                        .content(objectMapper.writeValueAsBytes(postData))
                        .secure(true))
           .andExpect(status().isOk())
           .andDo(DocumentationUtil.documentEcho("post-test-2"));

    AssertionData assertionData = new AssertionData(postData, pathVariable, queryParameter, null, headerXKey, "Post Test 2");

    assertInsomnia(assertionData);
    assertPostman(assertionData);
  }

  @Test
  public void postTestWithForm() throws Exception {
    String pathVariable = UUID.randomUUID().toString();
    String headerXKey = UUID.randomUUID().toString();
    MultiValueMap<String, String> formParams = new LinkedMultiValueMap<>();
    formParams.add("form1",  UUID.randomUUID().toString());
    formParams.add("form1",  UUID.randomUUID().toString());
    formParams.add("form2",  UUID.randomUUID().toString());

    mockMvc.perform(post("/post/form/{pathVariable}", pathVariable)
                        .params(formParams)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .header("X-Key", headerXKey)
                        .secure(true))
           .andExpect(status().isOk())
           .andDo(DocumentationUtil.documentForm("post-test-form"));

    AssertionData assertionData = new AssertionData(null, pathVariable, null, formParams, headerXKey, "Post Test Form");

    assertInsomnia(assertionData);
    assertPostman(assertionData);
  }

  @Test
  public void postTestWithVariables() throws Exception {
    String variablePathVariable = "<<path-variable>>";
    String variableQueryParam = "<<query-param-variable>>";
    mockMvc.perform(post("/post/test/{pathVariable}?param1={queryParam}", variablePathVariable, variableQueryParam)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Key", "secret")
                        .content(objectMapper.writeValueAsBytes(postData))
                        .secure(true))
           .andExpect(status().isOk())
           .andDo(DocumentationUtil.documentWithVariableHeader("post-test-variables"));

    InsomniaVariableHandler insomniaVariableHandler = new InsomniaVariableHandler();
    AssertionData insomniaAssertionData = new AssertionData(postData, insomniaVariableHandler.replaceVariables(variablePathVariable),  insomniaVariableHandler.replaceVariables(variableQueryParam), null, insomniaVariableHandler.replaceVariables("<<x_key_header>>"), "Post Test Variables");
    assertInsomnia(insomniaAssertionData);
    insomniaAssertionData = new AssertionData(postData, insomniaVariableHandler.replaceVariables(variablePathVariable),  insomniaVariableHandler.replaceVariables(variableQueryParam), null, insomniaVariableHandler.replaceVariables("<<x_api_key_header>>"), "Post Test Variables");
    assertInsomnia(insomniaAssertionData);
    PostmanVariableHandler postmanVariableHandler = new PostmanVariableHandler();
    AssertionData postmanAssertionData = new AssertionData(postData, postmanVariableHandler.replaceVariables(variablePathVariable),  postmanVariableHandler.replaceVariables(variableQueryParam), null, postmanVariableHandler.replaceVariables("<<x_key_header>>"), "Post Test Variables");
    assertPostman(postmanAssertionData);
    postmanAssertionData = new AssertionData(postData, postmanVariableHandler.replaceVariables(variablePathVariable),  postmanVariableHandler.replaceVariables(variableQueryParam), null, postmanVariableHandler.replaceVariables("<<x_api_key_header>>"), "Post Test Variables");
    assertPostman(postmanAssertionData);
  }

  /**
   * ensureAllResources
   *
   * Verifies documentation persists across documentation calls, does a full assertion on cross test assertion data,
   * verifies the resources from the other two tests exist.
   *
   * @throws IOException
   */
  @AfterAll
  public static void ensureAllResources() throws IOException {
    AssertionData crossTestAssertionData = new AssertionData(CrossTestData.postData, CrossTestData.pathVariable, CrossTestData.queryParam, null, CrossTestData.headerXKey, "Post Test");
    assertInsomnia(crossTestAssertionData);
    assertPostman(crossTestAssertionData);
    assertInsomniaResourcesIncluded("Post Test", "Post Test No Host Variable", "Post Test 2", "Post Test Form", "Post Test Variables");
    assertPostmanResourcesIncluded("Post Test", "Post Test No Host Variable", "Post Test 2", "Post Test Form", "Post Test Variables");
  }
}
