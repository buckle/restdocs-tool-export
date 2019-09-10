package restdocs.tool.export.common.assertion;

import org.apache.commons.text.WordUtils;
import org.junit.platform.commons.util.StringUtils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AssertionUtils {

  public static void assertName(String name, String key) {
    assertFalse(StringUtils.isBlank(name));
    assertNotNull(name);
    assertTrue(name.matches("[a-zA-Z0-9 ]+"));
    assertTrue(name.contains(WordUtils.capitalize(key)));
  }

}
