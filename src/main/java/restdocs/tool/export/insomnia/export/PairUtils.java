package restdocs.tool.export.insomnia.export;

import java.util.Set;

public class PairUtils {

  private PairUtils() {}

  public static Pair findPairByNameValue(Set<Pair> parameterPairs, String name, String value) {
    if(parameterPairs != null) {
      return parameterPairs.stream()
                           .filter(pair -> name.equals(pair.getName()) &&
                                           value.equals(pair.getValue()))
                           .findFirst()
                           .orElse(null);
    }

    return null;
  }

}
