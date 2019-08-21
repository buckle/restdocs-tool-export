package restdocs.tool.export.insomnia.export;

import org.springframework.restdocs.operation.Parameters;

import java.util.HashSet;
import java.util.Set;

import static restdocs.tool.export.insomnia.export.InsomniaConstants.PAIR_ID;
import static restdocs.tool.export.insomnia.export.InsomniaExportUtils.generateId;
import static restdocs.tool.export.insomnia.export.PairUtils.findPairByNameValue;

public class ParametersCreator {

  private Parameters parameters;

  public ParametersCreator(Parameters parameters) {
    this.parameters = parameters;
  }

  public Set<Pair> create() {
    if(parameters != null && !parameters.isEmpty()) {
      Set<Pair> parameterPairs = new HashSet<>();

      parameters.forEach((name, values) -> values.forEach(value -> {
        if(findPairByNameValue(parameterPairs, name, value) == null) {
          Pair parameterPair = new Pair();
          parameterPair.setId(generateId(PAIR_ID));
          parameterPair.setName(name);
          parameterPair.setValue(value);
          parameterPairs.add(parameterPair);
        }
      }));

      return parameterPairs;
    }

    return null;
  }
}
