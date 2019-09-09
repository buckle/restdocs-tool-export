package restdocs.tool.export.insomnia.exporter.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

public class InsomniaExportUtils {

  private InsomniaExportUtils() {}

  public static String generateId(String idMarker) {
    return idMarker + formatBaseId(getBaseId());
  }

  public static Long getEpochMillis() {
    return LocalDateTime.now()
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli();
  }

  protected static String formatBaseId(String baseId) {
    if(baseId != null) {
      return baseId.replaceAll("-", "");
    }

    return null;
  }

  protected static String getBaseId() {
    return UUID.randomUUID().toString();
  }
}
