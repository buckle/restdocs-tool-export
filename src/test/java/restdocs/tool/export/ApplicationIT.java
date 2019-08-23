package restdocs.tool.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import restdocs.tool.export.utils.DocumentationUtil;

import java.util.UUID;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {"spring.application.name=test_application"})
@AutoConfigureRestDocs
@AutoConfigureMockMvc
public class ApplicationIT {

  @Autowired protected ObjectMapper objectMapper;
  @Autowired protected WebApplicationContext webApplicationContext;
  @Autowired protected RestDocumentationContextProvider restDocumentation;
  protected MockMvc mockMvc;

  @Value("${spring.application.name}")
  protected String appName;

  @BeforeEach
  void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                                  .apply(documentationConfiguration(restDocumentation)
                                             .snippets()
                                             .withAdditionalDefaults(ToolExportSnippet.get(appName)))
                                  .build();
  }

  @Test
  public void echoTest() throws Exception {
    mockMvc.perform(get("/{echo}", UUID.randomUUID().toString())
                        .secure(true))
           .andExpect(status().isOk())
           .andDo(DocumentationUtil.documentEcho());
  }

}
