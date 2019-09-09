package restdocs.tool.export.postman.exporter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {

  private String name;
  private Request request;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Request getRequest() {
    return request;
  }

  public void setRequest(Request request) {
    this.request = request;
  }
}
