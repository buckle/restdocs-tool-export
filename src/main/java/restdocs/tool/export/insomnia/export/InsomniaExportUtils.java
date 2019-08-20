package restdocs.tool.export.insomnia.export;

import java.util.UUID;

public class InsomniaExportUtils {

  private InsomniaExportUtils() {}

  public static String generateId(String idMarker) {
    return idMarker + formatBaseId(getBaseId());
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
