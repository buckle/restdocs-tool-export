package restdocs.tool.export.postman.exporter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Collection {

  private Info info;

  @JsonProperty("item")
  private Set<Item> items = null;

  public Info getInfo() {
    return info;
  }

  public void setInfo(Info info) {
    this.info = info;
  }

  public Set<Item> getItems() {
    return items;
  }
}
