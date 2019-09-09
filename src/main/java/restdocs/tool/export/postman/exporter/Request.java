package restdocs.tool.export.postman.exporter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Request {

  private String method;
  private Set<Header> headers;
  private Url url;
  private Body body;

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public Set<Header> getHeaders() {
    return headers;
  }

  public void setHeaders(Set<Header> headers) {
    this.headers = headers;
  }

  public Url getUrl() {
    return url;
  }

  public void setUrl(Url url) {
    this.url = url;
  }

  public Body getBody() {
    return body;
  }

  public void setBody(Body body) {
    this.body = body;
  }
}
