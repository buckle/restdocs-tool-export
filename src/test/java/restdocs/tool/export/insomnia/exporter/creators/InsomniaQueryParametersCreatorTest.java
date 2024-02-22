package restdocs.tool.export.insomnia.exporter.creators;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.restdocs.operation.OperationRequestFactory;
import org.springframework.restdocs.operation.QueryParameters;
import restdocs.tool.export.insomnia.exporter.Pair;
import restdocs.tool.export.insomnia.exporter.variable.InsomniaVariableHandler;

import java.net.URI;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static restdocs.tool.export.insomnia.utils.InsomniaAssertionUtils.assertIdMatches;
import static restdocs.tool.export.insomnia.exporter.InsomniaConstants.PAIR_ID;
import static restdocs.tool.export.insomnia.exporter.utils.PairUtils.findPairByNameValue;

public class InsomniaQueryParametersCreatorTest {

  @Test
  void create() throws Exception {
    QueryParameters parameters = QueryParameters.from(new OperationRequestFactory().create(new URI("/?param1=prize1&param2=prize2&param1=surprise"), HttpMethod.GET, null, null, null));
    Set<Pair> parameterPairs = new InsomniaQueryParametersCreator().create(parameters);

    assertNotNull(parameterPairs);
    assertEquals(3, parameterPairs.size());
    Pair param1Value1 = findPairByNameValue(parameterPairs, "param1", "prize1");
    assertNotNull(param1Value1);
    assertIdMatches(PAIR_ID, param1Value1.getId());
    Pair param1Value2 = findPairByNameValue(parameterPairs, "param1", "surprise");
    assertNotNull(param1Value2);
    assertIdMatches(PAIR_ID, param1Value2.getId());
    Pair param2Value1 = findPairByNameValue(parameterPairs, "param2", "prize2");
    assertNotNull(param2Value1);
    assertIdMatches(PAIR_ID, param2Value1.getId());
  }

  @Test
  void createWhenDuplicateParamValue() throws Exception {
    QueryParameters parameters = QueryParameters.from(new OperationRequestFactory().create(new URI("/?param1=prize1&param1=prize1"), HttpMethod.GET, null, null, null));
    Set<Pair> parameterPairs = new InsomniaQueryParametersCreator().create(parameters);

    assertNotNull(parameterPairs);
    assertEquals(1, parameterPairs.size());
    Pair param1Value1 = findPairByNameValue(parameterPairs, "param1", "prize1");
    assertNotNull(param1Value1);
    assertIdMatches(PAIR_ID, param1Value1.getId());
  }

  @Test
  void createWhenVariableParamValue() throws Exception {
    URI uri = new URI("https", "localhost", "/", "param1=prize1&<<param2>>=<<prize2>>", null);
    QueryParameters parameters = QueryParameters.from(new OperationRequestFactory().create(uri, HttpMethod.GET, null, null, null));
    InsomniaVariableHandler insomniaVariableHandler = (spy(InsomniaVariableHandler.class));
    String replacedQueryKey = "replaced";
    String replacedQueryValue = "replaced-value";
    when(insomniaVariableHandler.replaceVariables(eq("<<param2>>"))).thenReturn(replacedQueryKey);
    when(insomniaVariableHandler.replaceVariables(eq("<<prize2>>"))).thenReturn(replacedQueryValue);


    Set<Pair> parameterPairs = new InsomniaQueryParametersCreator(insomniaVariableHandler).create(parameters);

    assertNotNull(parameterPairs);
    assertEquals(2, parameterPairs.size());
    Pair param1Value1 = findPairByNameValue(parameterPairs, "param1", "prize1");
    assertNotNull(param1Value1);
    assertIdMatches(PAIR_ID, param1Value1.getId());

    Pair param2Value2 = findPairByNameValue(parameterPairs, replacedQueryKey, replacedQueryValue);
    assertNotNull(param2Value2);
    assertIdMatches(PAIR_ID, param2Value2.getId());

    verify(insomniaVariableHandler).replaceVariables("param1");
    verify(insomniaVariableHandler).replaceVariables("prize1");
    verify(insomniaVariableHandler).replaceVariables("<<param2>>");
    verify(insomniaVariableHandler).replaceVariables("<<prize2>>");
  }

  @Test
  void createWhenNullParameters() {
    assertNull(new InsomniaQueryParametersCreator().create(null));
  }

  @Test
  void createWhenEmptyParameters() throws Exception {
    QueryParameters parameters = QueryParameters.from(new OperationRequestFactory().create(new URI("/test"), HttpMethod.GET, null, null, null));
    assertTrue(parameters.isEmpty());
    assertNull(new InsomniaQueryParametersCreator().create(parameters));
  }
}
