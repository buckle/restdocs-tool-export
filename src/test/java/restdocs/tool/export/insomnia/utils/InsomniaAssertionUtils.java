package restdocs.tool.export.insomnia.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InsomniaAssertionUtils {

  public static void assertIdMatches(String idMarker, String id) {
    assertTrue(id.matches(idMarker + "[0-9a-fA-F]{8}[0-9a-fA-F]{4}[0-9a-fA-F]{4}[0-9a-fA-F]{4}[0-9a-fA-F]{12}"));
  }

  public static void assertTimeEpoch(Long epoch) {
    assertNotNull(epoch);
    LocalDateTime time = LocalDateTime.ofInstant(Instant.ofEpochMilli(epoch), ZoneId.systemDefault());

    assertTrue(time.isAfter(LocalDateTime.now().minusSeconds(3)));
    assertTrue(time.isBefore(LocalDateTime.now().plusSeconds(3)));
  }
}
