package restdocs.tool.export.insomnia.exporter.creators;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.FormParameters;
import org.springframework.restdocs.operation.OperationRequest;
import restdocs.tool.export.common.creator.Creator;
import restdocs.tool.export.insomnia.exporter.Body;
import restdocs.tool.export.insomnia.exporter.Pair;

import java.util.HashSet;
import java.util.Set;

import static restdocs.tool.export.insomnia.exporter.InsomniaConstants.PAIR_ID;
import static restdocs.tool.export.insomnia.exporter.utils.InsomniaExportUtils.generateId;

public class BodyCreator implements Creator<Body, OperationRequest> {

  @Override
  public Body create(OperationRequest request) {
    if(request != null) {
      HttpHeaders headers = request.getHeaders();
      MediaType contentType = headers != null ? headers.getContentType() : null;

      Body body = new Body();
      FormParameters formParameters = FormParameters.from(request);
      if (MediaType.APPLICATION_FORM_URLENCODED.equals(contentType) && !formParameters.isEmpty()) {
        Set<Pair> formParameterPairs = new HashSet<>();

        formParameters.forEach((name, values) -> values.forEach(value -> {
            Pair parameterPair = new Pair();
            parameterPair.setId(generateId(PAIR_ID));
            parameterPair.setName(name);
            parameterPair.setValue(value);
            formParameterPairs.add(parameterPair);
        }));
        body.setParams(formParameterPairs);
      } else {
        body.setText(request.getContentAsString());
      }
      body.setMimeType(contentType != null ? contentType.toString() : null);

      return body;
    }

    return null;
  }
}
