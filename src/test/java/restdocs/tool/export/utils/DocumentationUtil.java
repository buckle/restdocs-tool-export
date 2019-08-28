package restdocs.tool.export.utils;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

public class DocumentationUtil {

  public static RestDocumentationResultHandler documentEcho() {
    return document("echo-test",
                    pathParameters(parameterWithName("pathVariable")
                                       .description("path variable")),
                    requestParameters(parameterWithName("param1")
                                          .description("request parameter")),
                    requestFields(fieldWithPath("sField1")
                                      .description("string field 1"),
                                  fieldWithPath("iField2")
                                      .description("integer field 2")),
                    responseFields(fieldWithPath("pathVariable")
                                       .description("path variable"),
                                   fieldWithPath("requestParam")
                                       .description("request parameter"),
                                   fieldWithPath("postData.sField1")
                                       .description("string field 1"),
                                   fieldWithPath("postData.iField2")
                                       .description("integer field 2"))
    );
  }

}
