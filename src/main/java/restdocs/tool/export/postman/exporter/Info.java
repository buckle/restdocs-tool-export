package restdocs.tool.export.postman.exporter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Info {

  @JsonProperty("_postman_id")
  private String postmanId;
  private String name;
  private String schema;

  public String getPostmanId() {
    return postmanId;
  }

  public void setPostmanId(String postmanId) {
    this.postmanId = postmanId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSchema() {
    return schema;
  }

  public void setSchema(String schema) {
    this.schema = schema;
  }
}
