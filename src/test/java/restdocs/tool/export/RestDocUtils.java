package restdocs.tool.export;

import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Map;

public class RestDocUtils {

  public static Map.Entry<String, List<String>> findHttpHeaderForName(HttpHeaders headers, String name) {
    if(headers != null) {
      return headers.entrySet()
                    .stream()
                    .filter(stringListEntry -> stringListEntry.getKey().equals(name))
                    .findFirst()
                    .orElse(null);
    }

    return null;
  }

}
