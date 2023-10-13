package restdocs.tool.export;

import org.springframework.util.MultiValueMap;
import restdocs.tool.export.application.PostData;

public class AssertionData {

  private PostData postData;
  private String pathVariable;
  private String queryParameter;
  private MultiValueMap<String, String> formParameter;
  private String header;
  private String resourceName;

  public AssertionData(PostData postData, String pathVariable, String queryParameter, MultiValueMap<String, String> formParameter, String header, String resourceName) {
    this.postData = postData;
    this.pathVariable = pathVariable;
    this.queryParameter = queryParameter;
    this.formParameter = formParameter;
    this.header = header;
    this.resourceName = resourceName;
  }

  public PostData getPostData() {
    return postData;
  }

  public String getPathVariable() {
    return pathVariable;
  }

  public String getQueryParameter() {
    return queryParameter;
  }

  public MultiValueMap<String, String> getFormParameter() {
    return formParameter;
  }

  public String getHeader() {
    return header;
  }

  public String getResourceName() {
    return resourceName;
  }
}
