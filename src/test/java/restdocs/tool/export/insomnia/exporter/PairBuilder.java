package restdocs.tool.export.insomnia.exporter;

import restdocs.tool.export.insomnia.exporter.utils.InsomniaExportUtils;

import java.util.UUID;

import static restdocs.tool.export.insomnia.exporter.InsomniaConstants.PAIR_ID;

public class PairBuilder {

  private String id;
  private String name;
  private String value;

  private PairBuilder() {
    this.id = InsomniaExportUtils.generateId(PAIR_ID);
    this.name = "Name" + UUID.randomUUID().toString();
    this.value = "Value" + UUID.randomUUID().toString();
  }

  public PairBuilder setId(String id) {
    this.id = id;
    return this;
  }

  public PairBuilder seName(String name) {
    this.name = name;
    return this;
  }

  public PairBuilder setValue(String value) {
    this.value = value;
    return this;
  }

  public static PairBuilder builder() {
    return new PairBuilder();
  }

  public Pair build() {
    Pair pair = new Pair();
    pair.setId(this.id);
    pair.setName(this.name);
    pair.setValue(this.value);
    return pair;
  }
}
