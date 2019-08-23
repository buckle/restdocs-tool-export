package restdocs.tool.export.insomnia.export.creators;

import org.springframework.restdocs.operation.Parameters;
import restdocs.tool.export.Creator;
import restdocs.tool.export.insomnia.export.Pair;

import java.util.HashSet;
import java.util.Set;

import static restdocs.tool.export.insomnia.export.InsomniaConstants.PAIR_ID;
import static restdocs.tool.export.insomnia.export.utils.InsomniaExportUtils.generateId;
import static restdocs.tool.export.insomnia.export.utils.PairUtils.findPairByNameValue;

public class ParametersCreator implements Creator<Set<Pair>, Parameters> {

  @Override
  public Set<Pair> create(Parameters parameters) {
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
