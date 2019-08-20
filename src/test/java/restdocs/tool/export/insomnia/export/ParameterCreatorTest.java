package restdocs.tool.export.insomnia.export;

import org.junit.jupiter.api.Test;
import org.springframework.restdocs.operation.Parameters;
import org.springframework.restdocs.operation.QueryStringParser;

import java.net.URI;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ParameterCreatorTest {

  @Test
  void create() throws Exception {
    Parameters parameters = new QueryStringParser().parse(new URI("/?param1=prize1&param2=prize2&param1=surprise"));

    Set<Pair> parameterPairs = new ParameterCreator(parameters).create();

    assertNotNull(parameterPairs);
    // TODO
  }


}
