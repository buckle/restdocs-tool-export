package restdocs.tool.export.common.variable;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.operation.OperationRequest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static restdocs.tool.export.common.variable.HeaderVariablePreprocessor.replaceHeaderValueWithVariable;

public class HeaderVariablePreprocessorTest {

  @Test
  public void testHeaderVariablePreprocessor() {
    HeaderVariablePreprocessor headerVariablePreprocessor = spy(new HeaderVariablePreprocessor("x-api-key"));
    OperationRequest operationRequest = mock(OperationRequest.class);
    HttpHeaders httpHeaders = mock(HttpHeaders.class);
    when(operationRequest.getHeaders()).thenReturn(httpHeaders);

    HttpHeaders mockUpdatedHeaders = mock(HttpHeaders.class);
    doReturn(mockUpdatedHeaders).when(headerVariablePreprocessor).processHeaders(any());

    OperationRequest preprocessedRequest = headerVariablePreprocessor.preprocess(operationRequest);
    assertNotNull(preprocessedRequest);
    verify(headerVariablePreprocessor).processHeaders(httpHeaders);
  }

  @Test
  public void testHeaderVariables() {
    HeaderVariablePreprocessor headerVariablePreprocessor = replaceHeaderValueWithVariable("");
    assertNotNull(headerVariablePreprocessor);
  }

  @Test
  public void testHeaderVariablesRequiresOneOrMoreHeaders() {
    assertThrows(IllegalArgumentException.class, () -> replaceHeaderValueWithVariable());
  }

  @Test
  public void testProcessHeaders() {
    HeaderVariablePreprocessor headerVariablePreprocessor = replaceHeaderValueWithVariable("x-api-key");
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.set("x-api-key", "one-value");
    httpHeaders.set("test-header", "unaffected-value");
    HttpHeaders preprocessedHeaders = headerVariablePreprocessor.processHeaders(httpHeaders);
    assertNotNull(preprocessedHeaders);
    assertEquals(List.of("<<x-api-key_header>>"), preprocessedHeaders.get("x-api-key"));
    assertEquals(List.of("unaffected-value"), preprocessedHeaders.get("test-header"));
  }

  @Test
  public void testProcessHeaders_CaseInsensitive() {
    HeaderVariablePreprocessor headerVariablePreprocessor = replaceHeaderValueWithVariable("X-API-Key");
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.set("x-api-key", "one-value");
    httpHeaders.set("test-header", "unaffected-value");
    HttpHeaders preprocessedHeaders = headerVariablePreprocessor.processHeaders(httpHeaders);
    assertNotNull(preprocessedHeaders);
    assertEquals(List.of("<<X-API-Key_header>>"), preprocessedHeaders.get("x-api-key"));
    assertEquals(List.of("unaffected-value"), preprocessedHeaders.get("test-header"));
  }
}
