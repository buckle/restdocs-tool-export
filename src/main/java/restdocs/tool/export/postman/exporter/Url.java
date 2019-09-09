package restdocs.tool.export.postman.exporter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Url {

  private String raw;
  private String protocol;
  private List<String> host;
  private Integer port;
  private List<String> path;
  private Set<QueryParam> queryParams;

  public String getRaw() {
    return raw;
  }

  public void setRaw(String raw) {
    this.raw = raw;
  }

  public String getProtocol() {
    return protocol;
  }

  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }

  public List<String> getHost() {
    return host;
  }

  public void setHost(List<String> host) {
    this.host = host;
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public List<String> getPath() {
    return path;
  }

  public void setPath(List<String> path) {
    this.path = path;
  }

  public Set<QueryParam> getQueryParams() {
    return queryParams;
  }

  public void setQueryParams(Set<QueryParam> queryParams) {
    this.queryParams = queryParams;
  }
}
