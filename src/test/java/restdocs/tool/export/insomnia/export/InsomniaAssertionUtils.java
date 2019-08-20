package restdocs.tool.export.insomnia.export;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class InsomniaAssertionUtils {

  public static void assertIdMatches(String idMarker, String id) {
    assertTrue(id.matches(idMarker + "[0-9a-fA-F]{8}[0-9a-fA-F]{4}[0-9a-fA-F]{4}[0-9a-fA-F]{4}[0-9a-fA-F]{12}"));
  }
}
