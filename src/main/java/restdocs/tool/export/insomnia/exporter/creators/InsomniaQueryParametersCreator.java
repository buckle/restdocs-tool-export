package restdocs.tool.export.insomnia.exporter.creators;

import org.springframework.restdocs.operation.QueryParameters;
import restdocs.tool.export.common.creator.Creator;
import restdocs.tool.export.insomnia.exporter.Pair;
import restdocs.tool.export.insomnia.exporter.variable.InsomniaVariableHandler;

import java.util.HashSet;
import java.util.Set;

import static restdocs.tool.export.insomnia.exporter.InsomniaConstants.PAIR_ID;
import static restdocs.tool.export.insomnia.exporter.utils.InsomniaExportUtils.generateId;
import static restdocs.tool.export.insomnia.exporter.utils.PairUtils.findPairByNameValue;

public class InsomniaQueryParametersCreator implements Creator<Set<Pair>, QueryParameters> {

  private InsomniaVariableHandler insomniaVariableHandler;

  public InsomniaQueryParametersCreator(InsomniaVariableHandler insomniaVariableHandler) {
    this.insomniaVariableHandler = insomniaVariableHandler;
  }

  public InsomniaQueryParametersCreator() {
    this.insomniaVariableHandler = new InsomniaVariableHandler();
  }

  @Override
  public Set<Pair> create(QueryParameters parameters) {
    if(parameters != null && !parameters.isEmpty()) {
      Set<Pair> parameterPairs = new HashSet<>();

      parameters.forEach((name, values) -> values.forEach(value -> {
        if(findPairByNameValue(parameterPairs, name, value) == null) {
          Pair parameterPair = new Pair();
          parameterPair.setId(generateId(PAIR_ID));
          parameterPair.setName(insomniaVariableHandler.replaceVariables(name));
          parameterPair.setValue(insomniaVariableHandler.replaceVariables(value));
          parameterPairs.add(parameterPair);
        }
      }));

      return parameterPairs;
    }

    return null;
  }
}
