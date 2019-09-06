package restdocs.tool.export.postman.exporter.creators;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import restdocs.tool.export.common.creator.Creator;
import restdocs.tool.export.postman.exporter.Header;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HeadersCreator implements Creator<Set<Header>, HttpHeaders> {

  @Override
  public Set<Header> create(HttpHeaders httpHeaders) {
    if(httpHeaders != null && !httpHeaders.isEmpty()) {
      Set<Header> headers = new HashSet<>();

      for(Map.Entry<String, List<String>> stringListEntry : httpHeaders.entrySet()) {
        String key = stringListEntry.getKey();
        List<String> values = stringListEntry.getValue();

        Header header = new Header();
        header.setKey(key);
        header.setValue(StringUtils.join(values, ","));

        headers.add(header);
      }

      return headers;
    }

    return null;
  }
}
