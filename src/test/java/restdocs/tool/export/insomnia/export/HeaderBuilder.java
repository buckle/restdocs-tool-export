package restdocs.tool.export.insomnia.export;

import java.util.UUID;

import static restdocs.tool.export.insomnia.export.InsomniaConstants.PAIR_ID;

public class HeaderBuilder {

  private String id;
  private String name;
  private String value;

  private HeaderBuilder() {
    this.id = InsomniaExportUtils.generateId(PAIR_ID);
    this.name = "Name" + UUID.randomUUID().toString();
    this.value = "Value" + UUID.randomUUID().toString();
  }

  public HeaderBuilder setId(String id) {
    this.id = id;
    return this;
  }

  public HeaderBuilder seName(String name) {
    this.name = name;
    return this;
  }

  public HeaderBuilder setValue(String value) {
    this.value = value;
    return this;
  }

  public static HeaderBuilder builder() {
    return new HeaderBuilder();
  }

  public Header build() {
    Header header = new Header();
    header.setId(this.id);
    header.setName(this.name);
    header.setValue(this.value);
    return header;
  }
}
