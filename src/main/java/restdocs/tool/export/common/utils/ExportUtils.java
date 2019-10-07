package restdocs.tool.export.common.utils;

import org.apache.commons.text.WordUtils;

public class ExportUtils {

  private ExportUtils() {}

  public static String formatNameReadably(String name) {
    if(name != null) {
      return WordUtils.capitalize(name.trim()
                                      .replaceAll("[^a-zA-Z0-9]", " ")
                                      .replaceAll(" +", " "));
    }

    return null;
  }

}
