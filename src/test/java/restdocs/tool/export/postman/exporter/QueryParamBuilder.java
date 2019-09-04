package restdocs.tool.export.postman.exporter;

import java.util.UUID;

public class QueryParamBuilder {

  private String key;
  private String value;

  private QueryParamBuilder() {
    this.key = "Key" + UUID.randomUUID().toString().replaceAll("-", "");
    this.value = "Value" + UUID.randomUUID().toString().replaceAll("-", "");
  }

  public QueryParamBuilder key(String key) {
    this.key = key;
    return this;
  }

  public QueryParamBuilder value(String value) {
    this.value = value;
    return this;
  }

  public static QueryParamBuilder builder() {
    return new QueryParamBuilder();
  }

  public QueryParam build() {
    QueryParam queryParam = new QueryParam();
    queryParam.setKey(this.key);
    queryParam.setValue(this.value);
    return queryParam;
  }
}
