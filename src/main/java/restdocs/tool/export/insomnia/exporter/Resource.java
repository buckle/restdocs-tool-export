package restdocs.tool.export.insomnia.exporter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Resource {

  @JsonProperty("_type")
  private String type;

  @JsonProperty("_id")
  private String id;

  private String parentId;

  @JsonProperty("created")
  private Long created;

  @JsonProperty("modified")
  private Long modified;

  private String name;
  private String method;
  private String url;
  private Body body;
  private Set<Pair> headers;
  private Set<Pair> parameters;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  public Long getCreated() {
    return created;
  }

  public void setCreated(Long created) {
    this.created = created;
  }

  public Long getModified() {
    return modified;
  }

  public void setModified(Long modified) {
    this.modified = modified;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Body getBody() {
    return body;
  }

  public void setBody(Body body) {
    this.body = body;
  }

  public Set<Pair> getHeaders() {
    return headers;
  }

  public void setHeaders(Set<Pair> headers) {
    this.headers = headers;
  }

  public Set<Pair> getParameters() {
    return parameters;
  }

  public void setParameters(Set<Pair> parameters) {
    this.parameters = parameters;
  }
}
