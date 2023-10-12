package restdocs.tool.export.utils;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import restdocs.tool.export.ToolExportSnippet;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

public class DocumentationUtil {

  public static RestDocumentationResultHandler documentEcho(String name, ToolExportSnippet toolExportSnippet) {
    return document(name,
                    pathParameters(parameterWithName("pathVariable")
                                       .description("path variable")),
                    queryParameters(parameterWithName("param1")
                                          .description("query parameter")),
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
                                       .description("integer field 2")),
                    toolExportSnippet
    );
  }

  public static RestDocumentationResultHandler documentForm(String name, ToolExportSnippet toolExportSnippet) {
    return document(name,
                    pathParameters(parameterWithName("pathVariable")
                                       .description("path variable")),
                    formParameters(parameterWithName("form1")
                                       .description("form parameter"),
                                   parameterWithName("form2")
                                       .description("form parameter 2")),
                    toolExportSnippet
    );
  }

}
