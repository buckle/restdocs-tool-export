package restdocs.tool.export.insomnia.export;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HeadersCreator {

  private HttpHeaders httpHeaders;

  public HeadersCreator(HttpHeaders httpHeaders) {
    this.httpHeaders = httpHeaders;
  }

  protected Set<Pair> create() {
    if(httpHeaders != null && httpHeaders.size() > 0) {
      Set<Pair> docPairs = new HashSet<>();

      for(Map.Entry<String, List<String>> stringListEntry : httpHeaders.entrySet()) {
        String key = stringListEntry.getKey();
        List<String> values = stringListEntry.getValue();

        Pair pair = new Pair();
        pair.setId(InsomniaExportUtils.generateId(InsomniaConstants.PAIR_ID));
        pair.setName(key);
        pair.setValue(StringUtils.join(values, ","));

        docPairs.add(pair);
      }

      return docPairs;
    }

    return null;
  }
}
