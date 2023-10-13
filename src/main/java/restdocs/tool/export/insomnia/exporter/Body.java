package restdocs.tool.export.insomnia.exporter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Body {

  private String mimeType;
  private String text;
  private Set<Pair> params;

  public String getMimeType() {
    return mimeType;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Set<Pair> getParams() {
    return params;
  }

  public void setParams(Set<Pair> params) {
    this.params = params;
  }
}
