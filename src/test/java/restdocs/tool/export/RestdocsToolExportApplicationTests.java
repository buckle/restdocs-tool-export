package restdocs.tool.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
public class RestdocsToolExportApplicationTests {

  @Autowired protected ObjectMapper objectMapper;
  @Autowired protected WebApplicationContext webApplicationContext;
  @Autowired protected RestDocumentationContextProvider restDocumentation;
  @Autowired protected InsomniaSnippet insomniaSnippet;
  protected MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                                  .apply(documentationConfiguration(restDocumentation)
                                             .snippets()
                                             .withAdditionalDefaults(insomniaSnippet))
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
