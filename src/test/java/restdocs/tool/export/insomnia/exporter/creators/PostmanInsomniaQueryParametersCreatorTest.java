package restdocs.tool.export.insomnia.exporter.creators;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.restdocs.operation.OperationRequestFactory;
import org.springframework.restdocs.operation.QueryParameters;
import restdocs.tool.export.insomnia.exporter.Pair;

import java.net.URI;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static restdocs.tool.export.insomnia.utils.InsomniaAssertionUtils.assertIdMatches;
import static restdocs.tool.export.insomnia.exporter.InsomniaConstants.PAIR_ID;
import static restdocs.tool.export.insomnia.exporter.utils.PairUtils.findPairByNameValue;

public class PostmanInsomniaQueryParametersCreatorTest {

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
