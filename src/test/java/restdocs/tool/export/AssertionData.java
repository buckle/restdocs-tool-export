package restdocs.tool.export;

import restdocs.tool.export.application.PostData;

public class AssertionData {

  private PostData postData;
  private String pathVariable;
  private String parameter;
  private String header;
  private String resourceName;

  public AssertionData(PostData postData, String pathVariable, String parameter, String header, String resourceName) {
    this.postData = postData;
    this.pathVariable = pathVariable;
    this.parameter = parameter;
    this.header = header;
    this.resourceName = resourceName;
  }

  public PostData getPostData() {
    return postData;
  }

  public String getPathVariable() {
    return pathVariable;
  }

  public String getParameter() {
    return parameter;
  }

  public String getHeader() {
    return header;
  }

  public String getResourceName() {
    return resourceName;
  }
}
