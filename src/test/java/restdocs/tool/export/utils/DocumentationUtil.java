package restdocs.tool.export.utils;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

public class DocumentationUtil {

  public static RestDocumentationResultHandler documentEcho() {
    return document("echo-test",
                    pathParameters(parameterWithName("echo")
                                       .description("echo")),
                    responseFields(fieldWithPath("echo")
                                       .description("echo"))
    );
  }

}
