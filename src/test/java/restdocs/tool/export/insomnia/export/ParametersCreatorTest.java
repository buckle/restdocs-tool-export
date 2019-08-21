package restdocs.tool.export.insomnia.export;

import org.junit.jupiter.api.Test;
import org.springframework.restdocs.operation.Parameters;
import org.springframework.restdocs.operation.QueryStringParser;

import java.net.URI;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static restdocs.tool.export.insomnia.export.InsomniaAssertionUtils.assertIdMatches;
import static restdocs.tool.export.insomnia.export.InsomniaConstants.PAIR_ID;
import static restdocs.tool.export.insomnia.export.PairUtils.findPairByNameValue;

public class ParametersCreatorTest {

  @Test
  void create() throws Exception {
    Parameters parameters = new QueryStringParser().parse(new URI("/?param1=prize1&param2=prize2&param1=surprise"));

    Set<Pair> parameterPairs = new ParametersCreator(parameters).create();

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
    Parameters parameters = new QueryStringParser().parse(new URI("/?param1=prize1&param1=prize1"));

    Set<Pair> parameterPairs = new ParametersCreator(parameters).create();

    assertNotNull(parameterPairs);
    assertEquals(1, parameterPairs.size());
    Pair param1Value1 = findPairByNameValue(parameterPairs, "param1", "prize1");
    assertNotNull(param1Value1);
    assertIdMatches(PAIR_ID, param1Value1.getId());
  }

  @Test
  void createWhenNullParameters() {
    assertNull(new ParametersCreator(null).create());
  }

  @Test
  void createWhenEmptyParameters() {
    assertNull(new ParametersCreator(new Parameters()).create());
  }
}
