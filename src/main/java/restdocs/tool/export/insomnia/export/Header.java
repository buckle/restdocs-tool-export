package restdocs.tool.export.insomnia.export;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Header {

  private String id;
  private String name;
  private String value;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if(this == o) return true;
    if(o == null || getClass() != o.getClass()) return false;
    Header header = (Header) o;
    return Objects.equals(id, header.id) &&
           Objects.equals(name, header.name) &&
           Objects.equals(value, header.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, value);
  }
}
