package restdocs.tool.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import restdocs.tool.export.application.PostData;
import restdocs.tool.export.application.TestApplication;
import restdocs.tool.export.common.handler.ToolHandlers;
import restdocs.tool.export.utils.DocumentationUtil;
import restdocs.tool.export.utils.ITTestUtils;

import java.util.UUID;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static restdocs.tool.export.CrossTestData.*;
import static restdocs.tool.export.insomnia.utils.InsomniaDocumentationAssertion.assertInsomnia;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {"spring.application.name=test_application"}, classes = TestApplication.class)
@AutoConfigureRestDocs
@AutoConfigureMockMvc
public class RestDocsIT {

  @Autowired protected ObjectMapper objectMapper;
  @Autowired protected MockMvc mockMvc;
  @Value("${spring.application.name}") protected String appName;

  private ToolExportSnippet toolExportSnippet;

  static {
    ITTestUtils.cleanSnippetsDir();
  }

  @BeforeEach
  void setUp() throws Exception {
    toolExportSnippet = ToolExportSnippet.initInstance(appName, ToolHandlers.INSOMNIA);
  }

  @Test
  public void postTest() throws Exception {
    mockMvc.perform(post("/post/test/{pathVariable}", pathVariable)
                        .param("param1", parameter)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Key", headerXKey)
                        .content(objectMapper.writeValueAsBytes(postData))
                        .secure(true))
           .andExpect(status().isOk())
           .andDo(DocumentationUtil.documentEcho("post-test", toolExportSnippet));

    assertInsomnia(new AssertionData(postData, pathVariable, parameter, headerXKey, "Post Test"));
  }

  /**
   * postTest2
   *
   * This only actually documents one API call, but asserts documentation on this API call
   * and the one from postTest. Verifies documentation persists across documentation calls.
   *
   * @throws Exception
   */
  @Test
  public void postTest2() throws Exception {
    PostData postData = new PostData();
    postData.setsField1(UUID.randomUUID().toString());
    postData.setiField2(RandomUtils.nextInt());

    String pathVariable = UUID.randomUUID().toString();
    String parameter = UUID.randomUUID().toString();
    String headerXKey = UUID.randomUUID().toString();

    mockMvc.perform(post("/post/test/{pathVariable}", pathVariable)
                        .param("param1", parameter)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Key", headerXKey)
                        .content(objectMapper.writeValueAsBytes(postData))
                        .secure(true))
           .andExpect(status().isOk())
           .andDo(DocumentationUtil.documentEcho("post-test-2", toolExportSnippet));

    assertInsomnia(new AssertionData(CrossTestData.postData, CrossTestData.pathVariable, CrossTestData.parameter, CrossTestData.headerXKey, "Post Test"),
                   new AssertionData(postData, pathVariable, parameter, headerXKey, "Post Test 2"));
  }
}
