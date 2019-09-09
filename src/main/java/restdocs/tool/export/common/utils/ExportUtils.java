package restdocs.tool.export.common.utils;

import org.apache.commons.text.WordUtils;

public class ExportUtils {

  private ExportUtils() {}

  public static String formatName(String name) {
    if(name != null) {
      return WordUtils.capitalize(name.replaceAll("[^a-zA-Z0-9]", " ")
                                      .replaceAll(" +", " "));
    }

    return null;
  }

}
