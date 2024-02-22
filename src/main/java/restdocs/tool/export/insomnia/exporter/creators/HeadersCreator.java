package restdocs.tool.export.insomnia.exporter.creators;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import restdocs.tool.export.common.creator.Creator;
import restdocs.tool.export.insomnia.exporter.InsomniaConstants;
import restdocs.tool.export.insomnia.exporter.Pair;
import restdocs.tool.export.insomnia.exporter.utils.InsomniaExportUtils;
import restdocs.tool.export.insomnia.exporter.variable.InsomniaVariableHandler;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HeadersCreator implements Creator<Set<Pair>, HttpHeaders> {

  InsomniaVariableHandler insomniaVariableHandler;

  public HeadersCreator(InsomniaVariableHandler insomniaVariableHandler) {
    this.insomniaVariableHandler = insomniaVariableHandler;
  }

  public HeadersCreator() {
    this.insomniaVariableHandler = new InsomniaVariableHandler();
  }

  @Override
  public Set<Pair> create(HttpHeaders httpHeaders) {
    if(httpHeaders != null && !httpHeaders.isEmpty()) {
      Set<Pair> docPairs = new HashSet<>();

      for(Map.Entry<String, List<String>> stringListEntry : httpHeaders.entrySet()) {
        String key = stringListEntry.getKey();
        List<String> values = stringListEntry.getValue();

        Pair pair = new Pair();
        pair.setId(InsomniaExportUtils.generateId(InsomniaConstants.PAIR_ID));
        pair.setName(insomniaVariableHandler.replaceVariables(key));
        pair.setValue(insomniaVariableHandler.replaceVariables(StringUtils.join(values, ",")));

        docPairs.add(pair);
      }

      return docPairs;
    }

    return null;
  }
}
