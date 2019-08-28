package restdocs.tool.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
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
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import restdocs.tool.export.application.PostData;
import restdocs.tool.export.application.TestApplication;
import restdocs.tool.export.common.handler.ToolHandlers;
import restdocs.tool.export.insomnia.exporter.Export;
import restdocs.tool.export.utils.DocumentationUtil;

import java.io.File;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {"spring.application.name=test_application"}, classes = TestApplication.class)
@AutoConfigureRestDocs
@AutoConfigureMockMvc
public class ApplicationIT {

  @Autowired protected ObjectMapper objectMapper;
  @Autowired protected WebApplicationContext webApplicationContext;
  @Autowired protected RestDocumentationContextProvider restDocumentation;
  protected MockMvc mockMvc;

  @Value("${spring.application.name}")
  protected String appName;

  static {
    try {
      FileUtils.deleteDirectory(new File("build/generated-snippets"));
    } catch(Exception e) {}
  }

  @BeforeEach
  void setUp() throws Exception {
    this.mockMvc =
        MockMvcBuilders.webAppContextSetup(webApplicationContext)
                       .apply(documentationConfiguration(restDocumentation)
                                  .snippets()
                                  .withAdditionalDefaults(ToolExportSnippet.getInstance(appName, ToolHandlers.INSOMNIA)))
                       .build();
  }

  @Test
  public void echoTest() throws Exception {
    PostData postData = new PostData();
    postData.setsField1(UUID.randomUUID().toString());
    postData.setiField2(RandomUtils.nextInt());

    mockMvc.perform(post("/post/test/{pathVariable}", UUID.randomUUID().toString())
                        .param("param1", UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Key", UUID.randomUUID().toString())
                        .content(objectMapper.writeValueAsBytes(postData))
                        .secure(true))
           .andExpect(status().isOk())
           .andDo(DocumentationUtil.documentEcho());

    // TODO slightly better assertion
    File exportFile = new File("build/generated-snippets/insomnia-download/insomnia.export");
    Export export = objectMapper.readValue(exportFile, Export.class);
    assertNotNull(export);

    File docFile = new File("build/generated-snippets/insomnia-download/insomnia.adoc");
    assertTrue(docFile.length() > 0);
  }
}
