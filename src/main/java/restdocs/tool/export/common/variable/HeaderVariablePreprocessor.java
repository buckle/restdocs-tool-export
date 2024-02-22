package restdocs.tool.export.common.variable;

import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.operation.OperationRequest;
import org.springframework.restdocs.operation.OperationRequestFactory;
import org.springframework.restdocs.operation.preprocess.OperationPreprocessorAdapter;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;

public class HeaderVariablePreprocessor extends OperationPreprocessorAdapter {

  private final OperationRequestFactory requestFactory = new OperationRequestFactory();

  List<String> headers;
  public HeaderVariablePreprocessor(String... header) {
    Assert.notEmpty(header, "At least one value must be provided");
    headers = Arrays.asList(header);
  }

  @Override
  public OperationRequest preprocess(OperationRequest request) {
    return this.requestFactory.createFrom(request, processHeaders(request.getHeaders()));
  }

  protected HttpHeaders processHeaders(HttpHeaders httpHeaders) {
    HttpHeaders modifiedHeaders = new HttpHeaders();
    modifiedHeaders.putAll(httpHeaders);
    for (String header : headers) {
      modifiedHeaders.forEach((k, v) -> {
        if (k.equalsIgnoreCase(header)) {
          modifiedHeaders.set(k, "<<" + header + VariableKeys.HEADER + ">>");
        }
      });
    }
    return modifiedHeaders;
  }

  public static HeaderVariablePreprocessor replaceHeaderValueWithVariable(String... header) {
    return new HeaderVariablePreprocessor(header);
  }
}
